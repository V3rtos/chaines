package me.moonways.bridgenet.rest.client;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Synchronized;
import me.moonways.rest.api.HttpHost;
import me.moonways.rest.api.exchange.entity.EntityWriter;
import me.moonways.rest.api.exchange.entity.ExchangeableEntity;
import me.moonways.rest.api.exception.RestClientException;
import me.moonways.rest.api.exception.RestEntityException;
import me.moonways.rest.api.exchange.message.RestMessageType;
import me.moonways.rest.api.exchange.response.RestResponse;
import me.moonways.rest.api.exchange.response.RestResponseBuilder;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLPeerUnverifiedException;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
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

    public void setHeaders(Properties properties) {
        properties.forEach((key, value) ->
                activeConnection.setRequestProperty(key.toString(), value.toString()));
    }

    public void setType(RestMessageType messageType) {
        try {
            activeConnection.setRequestMethod(messageType.name());
        }
        catch (IOException exception) {
            throw new RestEntityException(exception);
        }
    }

    public void writeEntity(ExchangeableEntity entity) {
        EntityWriter entityWriter = new EntityWriter();
        entity.write(entityWriter);

        byte[] byteArray = entityWriter.toByteArray();
        // todo - 25.07.2023 - encryption by SSL

        try {
            OutputStream outputStream = activeConnection.getOutputStream();
            outputStream.write(byteArray);
        }
        catch (IOException exception) {
            throw new RestEntityException(exception);
        }
    }

    @Synchronized
    public void sendRequest() {
        try {
            activeConnection.getOutputStream().flush();
        }
        catch (IOException exception) {
            throw new RestEntityException(exception);
        }
    }

    @Synchronized
    public RestResponse decodeActualityResponse() {
        try {
            int responseCode = activeConnection.getResponseCode();
            if (responseCode / 100 >= 4) {
                return RestResponseBuilder.create()
                        .setStatusCode(responseCode)
                        .setMethod(activeConnection.getRequestMethod())
                        .build();
            }

            return RestResponseBuilder.create()
                    .setInputContent(activeConnection.getInputStream())
                    .setStatusCode(responseCode)
                    .setMethod(activeConnection.getRequestMethod())
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
