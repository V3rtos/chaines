package me.moonways.bridgenet.metrics.chart;

import lombok.RequiredArgsConstructor;
import me.moonways.bridgenet.metrics.Metric;
import me.moonways.bridgenet.metrics.MetricValue;
import me.moonways.bridgenet.metrics.quickchart.dto.Dataset;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Данный маппер необходим для генерации корректных
 * параметров относительно определенного типа графика
 * и метрики, которые в дальнейшем пойдут в тело запроса.
 */
@RequiredArgsConstructor
public class ChartDataMapper {

    private final ChartType chartType;
    private final Metric metric;

    /**
     * Сгенерировать и получить наименование типа графика,
     * которое в дальнейшем будет передаваться в запрос QuickChart.
     */
    public String getTypeName() {
        String lowerCase = chartType.name().toLowerCase();
        if (lowerCase.contains("_")) {
            int spliteratorIndex = lowerCase.indexOf("_");
            char nextChar = lowerCase.charAt(spliteratorIndex + 1);

            lowerCase = lowerCase.replace("_" + nextChar, ((Character)Character.toUpperCase(nextChar)).toString());
        }
        return lowerCase;
    }

    /**
     * Получить список данных, которые в дальнейшем будут
     * передаваться в запрос QuickChart.
     */
    public List<Dataset> getDatasets() {
        switch (chartType) {
            case POLAR_AREA:
            case PIE:
            case DOUGHNUT:
            case SPARKLINE:
                return getPieSimilarTypeDatasets();
            default:
                return getDefaultDatasets();
        }
    }

    /**
     * Сгенерировать список данных, образ которых пойдет
     * на типы графиков, похожих на "пирог".
     */
    private List<Dataset> getPieSimilarTypeDatasets() {
        return Collections.singletonList(
                Dataset.builder()
                        .data(getLabels().stream()
                                .map(metric::get)
                                .collect(Collectors.toList()))
                        .build()
        );
    }

    /**
     * Сгенерировать стандартный список данных
     * для запроса в QuickChart.
     */
    private List<Dataset> getDefaultDatasets() {
        Map<String, Dataset> datasets = new HashMap<>();

        for (MetricValue value : metric.getValues()) {
            String label = value.getLabel();

            Dataset dataset = datasets.computeIfAbsent(label.toLowerCase(),
                    f -> Dataset.builder()
                            .label(label)
                            .data(new ArrayList<>())
                            .build());

            dataset.getData().add(value.getValue());
        }

        return datasets.values()
                .stream()
                .map(dataset -> dataset.toBuilder()
                        .hasLineForeground(datasets.size() <= 1)
                        .build())
                .collect(Collectors.toList());
    }

    /**
     * Получить список наименований столбцов, которые будут
     * отображаться на иллюстрации и передаваться в запрос QuickChart.
     */
    public List<String> getLabels() {
        Stream<String> stringStream = metric.getValues().stream()
                .map(MetricValue::getLabel);

        if (chartType == ChartType.POLAR_AREA) {
            stringStream = stringStream.distinct();
        }

        return stringStream.collect(Collectors.toList());
    }
}
