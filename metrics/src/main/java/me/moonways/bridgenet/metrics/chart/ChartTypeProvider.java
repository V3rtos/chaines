package me.moonways.bridgenet.metrics.chart;

import lombok.RequiredArgsConstructor;
import me.moonways.bridgenet.metrics.Metric;
import me.moonways.bridgenet.metrics.MetricValue;
import me.moonways.bridgenet.metrics.quickchart.dto.Dataset;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class ChartTypeProvider {

    private final ChartType chartType;
    private final Metric metric;

    public String getTypeName() {
        String lowerCase = chartType.name().toLowerCase();
        if (lowerCase.contains("_")) {
            int spliteratorIndex = lowerCase.indexOf("_");
            char nextChar = lowerCase.charAt(spliteratorIndex + 1);

            lowerCase = lowerCase.replace("_" + nextChar, ((Character)Character.toUpperCase(nextChar)).toString());
        }
        return lowerCase;
    }

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

    private List<Dataset> getPieSimilarTypeDatasets() {
        return Collections.singletonList(
                Dataset.builder()
                        .data(getLabels().stream()
                                .map(metric::get)
                                .collect(Collectors.toList()))
                        .build()
        );
    }

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

    public List<String> getLabels() {
        Stream<String> stringStream = metric.getValues().stream()
                .map(MetricValue::getLabel);

        if (chartType == ChartType.POLAR_AREA) {
            stringStream = stringStream.distinct();
        }

        return stringStream.collect(Collectors.toList());
    }
}
