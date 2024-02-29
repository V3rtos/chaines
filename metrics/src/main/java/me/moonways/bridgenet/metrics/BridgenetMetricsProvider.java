package me.moonways.bridgenet.metrics;

import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.metrics.chart.ChartType;
import me.moonways.bridgenet.metrics.chart.ChartTypeProvider;
import me.moonways.bridgenet.metrics.quickchart.QuickChartApi;
import me.moonways.bridgenet.metrics.quickchart.dto.ChartData;
import me.moonways.bridgenet.metrics.quickchart.dto.IllustrationRequest;
import me.moonways.bridgenet.metrics.quickchart.dto.Options;
import me.moonways.bridgenet.metrics.settings.MetricsSettings;
import me.moonways.bridgenet.metrics.settings.MetricsSettingsParser;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Log4j2
public final class BridgenetMetricsProvider {

    private static final String DEFAULT_IMAGE_BACKGROUND_COLOR = "transparent";
    private static final String DEFAULT_IMAGE_FORMAT = "png";

    private final QuickChartApi api = new QuickChartApi();

    private final MetricsSettings settings; {
        settings = new MetricsSettingsParser().readSettings();
    }

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

        String illustrationURL = api.requestIllustrationURL(request);
        storeImage(metric, chartType, illustrationURL);

        return illustrationURL;
    }

    /**
     * Обернуть параметры метрики в общий запрос на иллюстрацию.
     * @param chart - параметры метрики.
     */
    private IllustrationRequest wrapChartToRequestBody(ChartData chart) {
        return IllustrationRequest.builder()
                .chart(chart)
                .imageWidth(settings.getDimensions().getWidth())
                .imageHeight(settings.getDimensions().getHeight())
                .backgroundColor(DEFAULT_IMAGE_BACKGROUND_COLOR)
                .imageFormat(DEFAULT_IMAGE_FORMAT)
                .build();
    }

    /**
     * Воспроизвести сохранение сгенерированной иллюстрации метрики
     * в директорию, с условием, что если в настройках метрики
     * включена возможность сохранения их в файл.
     *
     * @param metric - метрика, от которой была сгенерирована иллюстрация.
     * @param url - https ссылка на иллюстрацию.
     */
    private void storeImage(Metric metric, ChartType chartType, String url) {
        try {
            MetricsSettings.ImagesStore imagesStore = settings.getImagesStore();

            if (imagesStore.isEnabled()) {
                BufferedImage bufferedImage = ImageIO.read(new URL(url));

                Path path = Paths.get(imagesStore.getFolderName());
                if (!Files.exists(path)) {
                    Files.createDirectories(path);
                }

                String imageFileName = getImageFileName(metric, chartType);
                ImageIO.write(bufferedImage, DEFAULT_IMAGE_FORMAT, path.resolve(imageFileName).toFile());

                log.info("The §7\"{}\" §rmetric illustration was successfully saved to the directory as §e{}", metric.getName(), imageFileName);
            }
        }
        catch (IOException exception) {
            log.error("§4An error occurred when trying to save an illustration of a metric", exception);
        }
    }

    /**
     * Сгенерировать и получить наименование файла, который будет
     * содержать сохраненную иллюстрацию метрики.
     *
     * @param metric - метрика, от которой была сгенерирована иллюстрация.
     */
    private String getImageFileName(Metric metric, ChartType chartType) {
        return String.format("%s_%s_%s.%s", metric.getName().replace(" ", "_"), chartType.name(),
                ThreadLocalRandom.current().nextInt(), DEFAULT_IMAGE_FORMAT);
    }
}
