package me.moonways.bridgenet.profiler.quickchart;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.profiler.quickchart.dto.IllustrationRequest;
import me.moonways.bridgenet.profiler.quickchart.dto.IllustrationResponse;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

@Log4j2
public final class QuickChartApi {

    private static final String API_CHART_CREATE_URL = "https://quickchart.io/chart/create";

    private static final Gson GSON = new GsonBuilder()
            .setLenient()
            .create();

    /**
     * Запросить ссылку на готовое изображение метрики,
     * которые было сгенерировано по входным данным запроса.
     *
     * @param request - запрос с параметрами для генерации изображения.
     */
    public String requestIllustrationURL(IllustrationRequest request) {
        IllustrationResponse illustrationResponse = requestIllustration(request);
        if (illustrationResponse.isSuccess()) {
            return illustrationResponse.getImageURL();
        }

        return null;
    }

    public IllustrationResponse requestIllustration(IllustrationRequest request) {
        String requestJson = GSON.toJson(request);
        String responseJson = requestHttpClient(requestJson);

        log.debug("Metric-illustration was success requested by {}", requestJson);

        return GSON.fromJson(responseJson, IllustrationResponse.class);
    }

    private String requestHttpClient(String json) {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {

            HttpEntity responseEntity = httpClient.execute(createHttpPost(json)).getEntity();
            return EntityUtils.toString(responseEntity);
        } catch (IOException exception) {
            throw new QuickChartApiException(exception);
        }
    }

    private HttpPost createHttpPost(String json) {
        HttpPost httpPost = new HttpPost(API_CHART_CREATE_URL);
        httpPost.setEntity(new StringEntity(json, ContentType.APPLICATION_JSON));
        return httpPost;
    }
}
