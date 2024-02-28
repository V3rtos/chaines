package me.moonways.bridgenet.metrics;

import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.metrics.chart.ChartType;
import me.moonways.bridgenet.metrics.chart.ChartTypeProvider;
import me.moonways.bridgenet.metrics.quickchart.QuickChartApi;
import me.moonways.bridgenet.metrics.quickchart.dto.ChartData;
import me.moonways.bridgenet.metrics.quickchart.dto.IllustrationRequest;
import me.moonways.bridgenet.metrics.quickchart.dto.Options;

import java.util.UUID;

@Log4j2
public final class BridgenetMetricsProvider {

    private static final int DEFAULT_IMAGE_WIDTH = 1200;
    private static final int DEFAULT_IMAGE_HEIGHT = 600;
    private static final String DEFAULT_IMAGE_BACKGROUND_COLOR = "transparent";
    private static final String DEFAULT_IMAGE_FORMAT = "png";

    private final QuickChartApi api = new QuickChartApi();

    /**
     * Создать объект метрики, через которую в дальнейшем
     * будут происходить записи и переформатирование в запросы
     * к QuickChart API.
     *
     * @param name - уникальное наименование метрики.
     */
    public Metric createMetric(String name) {
        return new Metric(UUID.randomUUID(), name);
    }

    /**
     * Запросить ссылку на изображение иллюстрации метрики
     * в виде столбцов, которые по своей высоте показывают
     * гибкость изменения данных в ходе определенного
     * временного промежутка.
     *
     * @param metric - метрика, из которой берем данные для генерации иллюстрации.
     *
     * @return - ссылка на готовую иллюстрацию с рисованными данными метрики.
     */
    public String provideMetricIllustration(ChartType chartType, Metric metric) {
        log.info("Requesting '{}' type of metrics illustration for §2{}", chartType, metric);

        ChartTypeProvider chartTypeProvider = new ChartTypeProvider(chartType, metric);
        IllustrationRequest request = wrapChartToRequestBody(
                ChartData.builder()
                        .type(chartTypeProvider.getTypeName())
                        .data(ChartData.Data.builder()
                                .labels(chartTypeProvider.getLabels())
                                .datasets(chartTypeProvider.getDatasets())
                                .build())
                        .options(Options.builder()
                                .title(Options.Title.builder()
                                        .isDisplay(true)
                                        .text(metric.getName())
                                        .build())
                                .build())
                        .build());

        return api.requestIllustrationURL(request);
    }

    /**
     * Обернуть параметры метрики в общий запрос на иллюстрацию.
     * @param chart - параметры метрики.
     */
    private IllustrationRequest wrapChartToRequestBody(ChartData chart) {
        return IllustrationRequest.builder()
                .chart(chart)
                .backgroundColor(DEFAULT_IMAGE_BACKGROUND_COLOR)
                .imageFormat(DEFAULT_IMAGE_FORMAT)
                .imageWidth(DEFAULT_IMAGE_WIDTH)
                .imageHeight(DEFAULT_IMAGE_HEIGHT)
                .build();
    }
}
