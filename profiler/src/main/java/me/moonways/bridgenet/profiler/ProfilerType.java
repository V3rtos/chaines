package me.moonways.bridgenet.profiler;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.moonways.bridgenet.profiler.chart.ChartType;

@Getter
@RequiredArgsConstructor
public enum ProfilerType {

    MTP_TRAFFIC("MTP Traffic", ChartType.SPARKLINE),
    MTP_CONNECTIONS("MTP Connections", ChartType.LINE),
    JDBC_QUERIES("JDBC Queries", ChartType.POLAR_AREA),
    HTTP_REST("HTTP Traffic", ChartType.SPARKLINE),
    MEMORY("Runtime Memory", ChartType.LINE),
    ;

    private final String displayName;

    private final ChartType defaultChartType;
}
