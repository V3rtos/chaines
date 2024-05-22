package me.moonways.bridgenet.metrics;

import java.util.EnumMap;

@SuppressWarnings("SameParameterValue")
public class BridgenetMetricsLogger {

    private static final BridgenetMetricsProvider PROVIDER = new BridgenetMetricsProvider();

    private final EnumMap<MetricType, Metric> metricsByTypeMap = new EnumMap<>(MetricType.class);

    {
        for (MetricType metricType : MetricType.values()) {
            registerMetricType(metricType);
        }
    }

// ======================================================== // METRICS LOGGING FUNCTIONS // ======================================================== //

    /**
     * Логировать актуальное значение свободной памяти в системе
     * (измеряется в МБ).
     */
    public void logSystemMemoryFree() {
        doPut(MetricType.MEMORY, "Free (MB)", Runtime.getRuntime().freeMemory() / 1024 / 1024);
    }

    /**
     * Логировать актуальное значение общего количества памяти в системе
     * (измеряется в МБ).
     */
    public void logSystemMemoryTotal() {
        doPut(MetricType.MEMORY, "Total (MB)", Runtime.getRuntime().totalMemory() / 1024 / 1024);
    }

    /**
     * Логировать актуальное значение используемой памяти в системе
     * (измеряется в МБ).
     */
    public void logSystemMemoryUsed() {
        doPut(MetricType.MEMORY, "Used (MB)", (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1024 / 1024);
    }

    /**
     * Логировать изменение трафика в сети, которая производит чтение байтов.
     *
     * @param metricType    - метрика, в которую логируем значение.
     * @param readableBytes - количество прочтенных байтов в сети.
     */
    public void logNetworkTrafficBytesRead(MetricType metricType, long readableBytes) {
        doPut(metricType, "Reads", readableBytes);
    }

    /**
     * Логировать изменение трафика в сети, которая производит запись байтов.
     *
     * @param metricType    - метрика, в которую логируем значение.
     * @param writableBytes - количество записанных байтов в сети.
     */
    public void logNetworkTrafficBytesWrite(MetricType metricType, long writableBytes) {
        doPut(metricType, "Writes", writableBytes);
    }

    /**
     * Логировать открытие соединения в рамках определенной сети.
     *
     * @param metricType - метрика, в которую логируем значение.
     */
    public void logNetworkConnectionOpened(MetricType metricType) {
        doAddAndPut(metricType, "Connections", 1);
    }

    /**
     * Логировать закрытие соединения в рамках определенной сети.
     *
     * @param metricType - метрика, в которую логируем значение.
     */
    public void logNetworkConnectionClosed(MetricType metricType) {
        doSubtractAndPut(metricType, "Connections", 1);
    }

    /**
     * Логировать число успешно исполненных SQL запросов.
     */
    public void logDatabaseSuccessQuery() {
        doAddAndPut(MetricType.JDBC_QUERIES, "Completed", 1);
    }

    /**
     * Логировать число провальных SQL запросов.
     */
    public void logDatabaseFailureQuery() {
        doAddAndPut(MetricType.JDBC_QUERIES, "Failure", 1);
    }

    /**
     * Логировать число отмененных SQL запросов.
     */
    public void logDatabaseRollbackQuery() {
        doAddAndPut(MetricType.JDBC_QUERIES, "Rollback", 1);
    }

    /**
     * Логировать число открытых когда-либо транзакций.
     */
    public void logDatabaseTransaction() {
        doAddAndPut(MetricType.JDBC_QUERIES, "Transaction", 1);
    }

// ======================================================== // METRICS LOGGING FUNCTIONS // ======================================================== //

    /**
     * Запросить ссылку на иллюстрацию графика метрики определенного типа.
     *
     * @param metricType - тип метрики, иллюстрацию которой мы запрашиваем.
     */
    public String requestIllustrationURL(MetricType metricType) {
        Metric metric = getMetric(metricType);
        return PROVIDER.provideMetricIllustration(metricType.getDefaultChartType(), metric);
    }

// ================================================================================================================================================= //


    private void registerMetricType(MetricType metricType) {
        Metric metric = PROVIDER.createMetric(metricType.getDisplayName());
        metricsByTypeMap.put(metricType, metric);
    }

    private Metric getMetric(MetricType metricType) {
        Metric metric = metricsByTypeMap.get(metricType);
        if (metric == null) {
            throw new IllegalStateException("Metric " + metricType + " is not registered");
        }
        return metric;
    }

    private void doPut(MetricType metricType, String label, long value) {
        Metric metric = getMetric(metricType);
        metric.put(label, value);
    }

    private void doAddAndPut(MetricType metricType, String label, long value) {
        Metric metric = getMetric(metricType);
        metric.add(label, value);
    }

    private void doSubtractAndPut(MetricType metricType, String label, long value) {
        Metric metric = getMetric(metricType);
        metric.subtract(label, value);
    }
}
