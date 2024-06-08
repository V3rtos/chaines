package me.moonways.bridgenet.profiler;

import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.assembly.ResourcesAssembly;
import me.moonways.bridgenet.assembly.ResourcesTypes;
import me.moonways.bridgenet.profiler.chart.ChartDataMapper;
import me.moonways.bridgenet.profiler.chart.ChartType;
import me.moonways.bridgenet.profiler.quickchart.QuickChartApi;
import me.moonways.bridgenet.profiler.quickchart.dto.ChartData;
import me.moonways.bridgenet.profiler.quickchart.dto.IllustrationRequest;
import me.moonways.bridgenet.profiler.quickchart.dto.Options;

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
public final class ProfilerProvider {

    private static final String DEFAULT_IMAGE_BACKGROUND_COLOR = "transparent";
    private static final String DEFAULT_IMAGE_FORMAT = "png";

    private static final ResourcesAssembly ASSEMBLY = new ResourcesAssembly();

    private final QuickChartApi api = new QuickChartApi();

    private final ProfilerSettings settings;

    {
        settings = ASSEMBLY.readJsonAtEntity(ResourcesTypes.PROFILER_ATTRIBUTES_JSON,
                ProfilerSettings.class);
    }

    /**
     * Создать объект метрики, через которую в дальнейшем
     * будут происходить записи и переформатирование в запросы
     * к QuickChart API.
     *
     * @param name - уникальное наименование метрики.
     */
    public Profiler createMetric(String name) {
        return new Profiler(UUID.randomUUID(), name);
    }

    /**
     * Запросить ссылку на изображение иллюстрации метрики
     * в виде столбцов, которые по своей высоте показывают
     * гибкость изменения данных в ходе определенного
     * временного промежутка.
     *
     * @param profiler - метрика, из которой берем данные для генерации иллюстрации.
     * @return - ссылка на готовую иллюстрацию с рисованными данными метрики.
     */
    public String provideMetricIllustration(ChartType chartType, Profiler profiler) {
        log.debug("Requesting '{}' type of metrics illustration for §2{}", chartType, profiler);

        ChartDataMapper chartDataMapper = new ChartDataMapper(chartType, profiler);
        IllustrationRequest request = wrapChartToRequestBody(
                ChartData.builder()
                        .type(chartDataMapper.getTypeName())
                        .data(ChartData.Data.builder()
                                .labels(chartDataMapper.getLabels())
                                .datasets(chartDataMapper.getDatasets())
                                .build())
                        .options(Options.builder()
                                .title(Options.Title.builder()
                                        .isDisplay(true)
                                        .text(profiler.getName())
                                        .build())
                                .build())
                        .build());

        String illustrationURL = api.requestIllustrationURL(request);
        storeImage(profiler, chartType, illustrationURL);

        return illustrationURL;
    }

    /**
     * Обернуть параметры метрики в общий запрос на иллюстрацию.
     *
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
     * @param profiler - метрика, от которой была сгенерирована иллюстрация.
     * @param url    - https ссылка на иллюстрацию.
     */
    private void storeImage(Profiler profiler, ChartType chartType, String url) {
        try {
            ProfilerSettings.ImagesStore imagesStore = settings.getImagesStore();

            if (imagesStore.isEnabled()) {
                BufferedImage bufferedImage = ImageIO.read(new URL(url));

                Path path = Paths.get(imagesStore.getFolderName());
                if (!Files.exists(path)) {
                    Files.createDirectories(path);
                }

                String imageFileName = getImageFileName(profiler, chartType);
                ImageIO.write(bufferedImage, DEFAULT_IMAGE_FORMAT, path.resolve(imageFileName).toFile());

                log.info("The §7\"{}\" §rmetric illustration was successfully saved to the directory as §e{}", profiler.getName(), imageFileName);
            }
        } catch (IOException exception) {
            log.error("§4An error occurred when trying to save an illustration of a metric", exception);
        }
    }

    /**
     * Сгенерировать и получить наименование файла, который будет
     * содержать сохраненную иллюстрацию метрики.
     *
     * @param profiler - метрика, от которой была сгенерирована иллюстрация.
     */
    private String getImageFileName(Profiler profiler, ChartType chartType) {
        return String.format("%s_%s_%s.%s", profiler.getName().replace(" ", "_"), chartType.name(),
                ThreadLocalRandom.current().nextInt(), DEFAULT_IMAGE_FORMAT);
    }
}
