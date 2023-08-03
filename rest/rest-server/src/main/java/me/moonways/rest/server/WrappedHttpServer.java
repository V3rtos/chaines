package me.moonways.rest.server;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.api.inject.Component;
import me.moonways.bridgenet.api.inject.DependencyInjection;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.api.inject.PostFactoryMethod;
import me.moonways.bridgenet.api.inject.decorator.DecoratedObject;
import me.moonways.bridgenet.api.inject.decorator.definition.Async;
import me.moonways.bridgenet.api.inject.decorator.definition.KeepTime;
import me.moonways.bridgenet.api.inject.factory.UnsafeObjectFactory;
import me.moonways.bridgenet.api.jaxb.XmlJaxbParser;
import me.moonways.rest.api.HttpHost;
import me.moonways.rest.server.controller.verify.VerifyHelper;
import me.moonways.rest.server.controller.HttpContextPattern;
import me.moonways.rest.server.controller.WrappedHttpRequestHandler;
import me.moonways.rest.server.controller.HttpController;
import me.moonways.rest.server.controller.undefined.UndefinedHttpController;
import me.moonways.rest.server.controller.undefined.UndefinedHttpRequestInterceptor;
import me.moonways.rest.server.jaxb.JaxbHttpController;
import me.moonways.rest.server.jaxb.JaxbServerConnection;
import me.moonways.rest.server.jaxb.JaxbServerContext;
import me.moonways.rest.server.jaxb.JaxbServerCredentials;
import me.moonways.rest.server.controller.verify.HttpExpectationVerifyHandler;
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
@Component
@DecoratedObject
public class WrappedHttpServer {

    private static final int BUFFER_SIZE = (5 * 1024);

    private static final String CONFIG_FILENAME = "restserver.xml";
    private static final UnsafeObjectFactory UNSAFE_FACTORY = new UnsafeObjectFactory();

    private HttpServerConfig config;
    private HttpServer httpServer;

    private HttpServerExceptionHandler exceptionHandler;

    @Inject
    private DependencyInjection dependencyInjection;

    @KeepTime
    public synchronized void bindSync() {
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

    @PostFactoryMethod
    private void initConfig() {
        final XmlJaxbParser parser = new XmlJaxbParser();

        JaxbServerContext jaxbServerContext = parser.parseCopiedResource(getClass().getClassLoader(),
                CONFIG_FILENAME, JaxbServerContext.class);

        this.exceptionHandler =
                new HttpServerExceptionHandler(jaxbServerContext.getPrintExceptions());

        initConfigInstance(jaxbServerContext);
        initConfigHttpControllers(jaxbServerContext);
    }

    @PostFactoryMethod
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

        dependencyInjection.injectFields(undefinedInterceptor);

        bootstrap.setHttpProcessor(
                HttpProcessorBuilder.create()
                        .add((HttpResponseInterceptor) undefinedInterceptor)
                        .add((HttpRequestInterceptor) undefinedInterceptor)
                        .build());

        // register exists pattern-contexts.
        UndefinedHttpController forcedUndefinedController = new UndefinedHttpController(config, true);
        dependencyInjection.injectFields(forcedUndefinedController);

        config.getControllerPatternsMap()
                .values()
                .forEach(pattern -> {

                    WrappedHttpRequestHandler context
                            = new WrappedHttpRequestHandler(forcedUndefinedController, config, pattern, verifyHelper);

                    bootstrap.registerHandler(pattern.getName(), context);
                    log.info("HTTP Controller §2'{}' §rwas registered §7(method='{}', pattern='{}')",
                            pattern.getController().getClass().getSimpleName(),
                            pattern.getMethod(), pattern.getName());

                    dependencyInjection.injectFields(context);
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
            throw new NullPointerException("jaxb context pattern - " + httpContext.getPattern() + ".handler");
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
