package me.moonways.bridgenet.rest.api.http.client;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Synchronized;
import me.moonways.bridgenet.rest.api.HttpHost;
import me.moonways.bridgenet.rest.api.exchange.entity.EntityWriter;
import me.moonways.bridgenet.rest.api.exchange.entity.ExchangeableEntity;
import me.moonways.bridgenet.rest.api.exception.RestClientException;
import me.moonways.bridgenet.rest.api.exception.RestEntityException;
import me.moonways.bridgenet.rest.api.exchange.response.RestResponse;
import me.moonways.bridgenet.rest.api.exchange.response.RestResponseBuilder;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLPeerUnverifiedException;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.security.cert.Certificate;
import java.util.Properties;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class HttpChannel {

    private final HttpHost host;

    @Getter
    private HttpURLConnection activeConnection;

    @Synchronized
    public void openConnection() {
        try {
            URL url = new URL(host.getHost());

            activeConnection = openConnection(url, host.isUseSSL());
            initOptions(activeConnection);

        }
        catch (IOException exception) {
            throw new RestClientException(exception);
        }
    }

    private HttpURLConnection openConnection(URL url, boolean useSSL) {
        HttpsURLConnection https;
        try {
            URLConnection urlConnection = url.openConnection();
            if (useSSL) {
                https = (HttpsURLConnection) urlConnection;
            }
            else return (HttpURLConnection) urlConnection;
        }
        catch (IOException exception) {
            throw new RestClientException(exception);
        }

        //initSSLCertificates(https);
        return https;
    }

    private void initOptions(HttpURLConnection connection) {
        connection.setDoOutput(true);
        connection.setDoInput(true);
    }

    private void initSSLCertificates(HttpsURLConnection https) {
        try {
            Certificate[] serverCertificates = https.getServerCertificates();
            // todo
        }
        catch (SSLPeerUnverifiedException e) {
            throw new RestClientException(e);
        }
    }

    public void setHeaders(HttpURLConnection connection, Properties properties) {
        properties.forEach((key, value) ->
                connection.setRequestProperty(key.toString(), value.toString()));
    }

    @Synchronized
    public void sendAndFlush(HttpURLConnection connection, ExchangeableEntity entity) {
        EntityWriter entityWriter = new EntityWriter();
        entity.write(entityWriter);

        byte[] byteArray = entityWriter.toByteArray();
        // todo - 25.07.2023 - encryption by SSL

        try {
            OutputStream outputStream = connection.getOutputStream();

            outputStream.write(byteArray);
            outputStream.flush();
        }
        catch (IOException exception) {
            throw new RestEntityException(exception);
        }
    }

    @Synchronized
    public RestResponse decodeActualityResponse(HttpURLConnection connection) {
        try {
            return RestResponseBuilder.create()
                    .setInputContent(connection.getInputStream())
                    .setStatusCode(connection.getResponseCode())
                    .setMethod(connection.getRequestMethod())
                    .build();
        }
        catch (IOException exception) {
            throw new RestClientException(exception);
        }
    }

    @Synchronized
    public void close(HttpURLConnection connection) {
        if (connection != null) {
            connection.disconnect();
        }
    }
}
