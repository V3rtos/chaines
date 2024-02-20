package me.moonways.bridgenet.rest.server;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.api.inject.Autobind;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.api.inject.PostConstruct;
import me.moonways.bridgenet.api.inject.bean.factory.BeanFactory;
import me.moonways.bridgenet.api.inject.bean.factory.UnsafeFactory;
import me.moonways.bridgenet.api.inject.bean.service.BeansService;
import me.moonways.bridgenet.api.inject.decorator.EnableDecorators;
import me.moonways.bridgenet.api.inject.decorator.persistence.Async;
import me.moonways.bridgenet.api.inject.decorator.persistence.KeepTime;
import me.moonways.bridgenet.api.util.jaxb.XmlJaxbParser;
import me.moonways.bridgenet.rest.api.HttpHost;
import me.moonways.bridgenet.rest.server.controller.HttpContextPattern;
import me.moonways.bridgenet.rest.server.controller.HttpController;
import me.moonways.bridgenet.rest.server.controller.WrappedHttpRequestHandler;
import me.moonways.bridgenet.rest.server.controller.undefined.UndefinedHttpController;
import me.moonways.bridgenet.rest.server.controller.undefined.UndefinedHttpRequestInterceptor;
import me.moonways.bridgenet.rest.server.controller.verify.HttpExpectationVerifyHandler;
import me.moonways.bridgenet.rest.server.controller.verify.VerifyHelper;
import me.moonways.bridgenet.rest.server.jaxb.JaxbHttpController;
import me.moonways.bridgenet.rest.server.jaxb.JaxbServerConnection;
import me.moonways.bridgenet.rest.server.jaxb.JaxbServerContext;
import me.moonways.bridgenet.rest.server.jaxb.JaxbServerCredentials;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.impl.bootstrap.HttpServer;
import org.apache.http.impl.bootstrap.ServerBootstrap;
import org.apache.http.protocol.HttpProcessorBuilder;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@Log4j2
@RequiredArgsConstructor
@Autobind
@EnableDecorators
public class WrappedHttpServer {

    private static final int BUFFER_SIZE = (5 * 1024);

    private static final String CONFIG_FILENAME = "restserver.xml";
    private static final BeanFactory UNSAFE_FACTORY = new UnsafeFactory();

    private HttpServerConfig config;
    private HttpServer httpServer;

    private HttpServerExceptionHandler exceptionHandler;

    @Inject
    private BeansService beansService;
    @Inject
    private XmlJaxbParser jaxbParser;

    @Inject // self-inject
    private WrappedHttpServer current;

    @PostConstruct
    private void initServer() {
        initConfig();
        initHttpServer();

        current.bindSync();
    }

    @KeepTime
    public void bindSync() {
        try {
            httpServer.start();
            log.info("HttpServer was success started and listening on §6{}", config.getHost());
        }
        catch (IOException exception) {
            exceptionHandler.log(exception);
        }
    }

    @Async
    @KeepTime
    public void bind() {
        bindSync();
    }

    private void initConfig() {
        JaxbServerContext jaxbServerContext = jaxbParser.parseCopiedResource(getClass().getClassLoader(),
                CONFIG_FILENAME, JaxbServerContext.class);

        this.exceptionHandler =
                new HttpServerExceptionHandler(jaxbServerContext.getPrintExceptions());

        initConfigInstance(jaxbServerContext);
        initConfigHttpControllers(jaxbServerContext);
    }

    private void initHttpServer() {
        ServerBootstrap bootstrap = ServerBootstrap.bootstrap();
        initServerProperties(bootstrap);

        httpServer = bootstrap.create();
    }

    private void initServerProperties(ServerBootstrap bootstrap) {
        bootstrap.setConnectionConfig(
                ConnectionConfig.custom()
                        .setCharset(StandardCharsets.UTF_8)

                        .setUnmappableInputAction(CodingErrorAction.REPORT)
                        .setMalformedInputAction(CodingErrorAction.REPORT)

                        .setBufferSize(BUFFER_SIZE)
                        .build());

        final HttpHost httpHost = config.getHost();

        bootstrap.setListenerPort(httpHost.getPort());
        bootstrap.setLocalAddress(InetSocketAddress.createUnresolved(httpHost.getHost(), httpHost.getPort())
                .getAddress());

        bootstrap.setExceptionLogger(exceptionHandler);
        registerPatterns(bootstrap);
    }

    private void registerPatterns(ServerBootstrap bootstrap) {
        VerifyHelper verifyHelper = new VerifyHelper(config);
        verifyHelper.initVerifiers();

        // register undefined requests.
        UndefinedHttpRequestInterceptor undefinedInterceptor
                = new UndefinedHttpRequestInterceptor(new UndefinedHttpController(config, false));

        beansService.inject(undefinedInterceptor);

        bootstrap.setHttpProcessor(
                HttpProcessorBuilder.create()
                        .add((HttpResponseInterceptor) undefinedInterceptor)
                        .add((HttpRequestInterceptor) undefinedInterceptor)
                        .build());

        // register exists pattern-contexts.
        UndefinedHttpController forcedUndefinedController = new UndefinedHttpController(config, true);
        beansService.inject(forcedUndefinedController);

        config.getControllerPatternsMap()
                .values()
                .forEach(pattern -> {

                    WrappedHttpRequestHandler context
                            = new WrappedHttpRequestHandler(forcedUndefinedController, config, pattern, verifyHelper);

                    bootstrap.registerHandler(pattern.getName(), context);

                    log.info("HTTP Controller §2'{}' §rwas registered §7(method='{}', pattern='{}')",
                            pattern.getController().getClass().getSimpleName(),
                            pattern.getMethod(), pattern.getName());

                    beansService.inject(context);
                });

        // set expectation verifier
        bootstrap.setExpectationVerifier(new HttpExpectationVerifyHandler(verifyHelper));
    }

    private void initConfigInstance(JaxbServerContext jaxbServerContext) {
        JaxbServerConnection jaxbConnection = jaxbServerContext.getConnection();
        JaxbServerCredentials jaxbCredentials = jaxbConnection.getCredentials();

        //noinspection ConstantValue
        this.config = new HttpServerConfig(
                HttpHost.create(
                        Optional.ofNullable(jaxbConnection.getHost())
                                .orElse("127.0.0.1"),

                        Optional.ofNullable(jaxbConnection.getPort())
                                .map(s -> s == null ? "0" : s).map(Integer::parseInt)
                                .orElse(0)
                ),
                new HttpCredentials(
                        jaxbCredentials.getUsername(), jaxbCredentials.getPassword())
        );
    }

    private void initConfigHttpControllers(JaxbServerContext jaxbServerContext) {
        Map<String, HttpContextPattern> collectMap = jaxbServerContext.getControllersList()
                .stream()
                .collect(
                        Collectors.toMap(
                                JaxbHttpController::getPattern,
                                this::createControllerPattern
                        ));

        config.getControllerPatternsMap().putAll(collectMap);
    }

    private HttpContextPattern createControllerPattern(JaxbHttpController httpContext) {
        HttpController controller = loadClassUnsafeInstance(httpContext.getHandlerClasspath());
        if (controller == null) {
            log.error(new NullPointerException("jaxb context pattern - " + httpContext.getPattern() + ".handler"));
        }

        return new HttpContextPattern(
                httpContext.getPattern(),
                httpContext.getMethod(), controller
        );
    }

    @SuppressWarnings("unchecked")
    private <T> T loadClassUnsafeInstance(String classpath) {
        try {
            Class<?> aClass = Class.forName(classpath);
            return (T) UNSAFE_FACTORY.create(aClass);
        }
        catch (ClassNotFoundException exception) {
            exceptionHandler.log(exception);
        }

        return null;
    }
}
