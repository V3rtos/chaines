package me.moonways.bridgenet.rest.api;

import lombok.*;

import java.net.InetAddress;

@Getter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class HttpHost {

    private static final String URL_FORMAT = "%s://%s";

    private static final String PREFIX = "http";
    private static final String SSL_PREFIX = "https";

    private static String toHost(boolean useSSL, String hostname) {
        return String.format(URL_FORMAT, (useSSL ? SSL_PREFIX : PREFIX), hostname);
    }

    public static HttpHost createLocalhost() {
        return createLocalhost(0);
    }

    @SneakyThrows
    public static HttpHost createLocalhost(int port) {
        return create(InetAddress.getLocalHost().getHostName(), port);
    }

    public static HttpHost createNative(String host, int port) {
        return new HttpHost(host, port);
    }

    public static HttpHost create(String hostname, int port) {
        return new HttpHost(toHost(false, hostname), port);
    }

    public static HttpHost create(String hostname) {
        return new HttpHost(toHost(false, hostname), 0);
    }

    public static HttpHost createSSL(String hostname) {
        return new HttpHost(toHost(true, hostname), 0);
    }

    private final String host;
    private final int port;

    public boolean isUseSSL() {
        return host.startsWith(SSL_PREFIX);
    }
}
