package me.moonways.bridgenet.metrics;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.moonways.bridgenet.metrics.chart.ChartType;

@RequiredArgsConstructor
public enum MetricType {

    MTP_TRAFFIC("MTP Network Traffic", ChartType.SPARKLINE),
    MTP_CONNECTIONS("MTP Network Connections", ChartType.LINE),
    JDBC_QUERIES("Java Database Connection Queries", ChartType.POLAR_AREA),
    HTTP_REST("HTTP Network Traffic", ChartType.SPARKLINE),
    MEMORY("System Memory", ChartType.LINE),
    ;

    @Getter
    private final String displayName;

    @Getter
    private final ChartType defaultChartType;
}
