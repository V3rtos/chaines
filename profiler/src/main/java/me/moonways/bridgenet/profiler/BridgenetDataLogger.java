package me.moonways.bridgenet.profiler;

import java.util.EnumMap;

@SuppressWarnings("SameParameterValue")
public class BridgenetDataLogger {

    private static final ProfilerProvider PROVIDER = new ProfilerProvider();

    private final EnumMap<ProfilerType, Profiler> metricsByTypeMap = new EnumMap<>(ProfilerType.class);

    {
        for (ProfilerType profilerType : ProfilerType.values()) {
            registerMetricType(profilerType);
        }
    }

// ======================================================== // METRICS LOGGING FUNCTIONS // ======================================================== //

    /**
     * Логировать актуальное значение свободной памяти в системе
     * (измеряется в МБ).
     */
    public void logSystemMemoryFree() {
        doPut(ProfilerType.MEMORY, "Free (MB)", Runtime.getRuntime().freeMemory() / 1024 / 1024);
    }

    /**
     * Логировать актуальное значение общего количества памяти в системе
     * (измеряется в МБ).
     */
    public void logSystemMemoryTotal() {
        doPut(ProfilerType.MEMORY, "Total (MB)", Runtime.getRuntime().totalMemory() / 1024 / 1024);
    }

    /**
     * Логировать актуальное значение используемой памяти в системе
     * (измеряется в МБ).
     */
    public void logSystemMemoryUsed() {
        doPut(ProfilerType.MEMORY, "Used (MB)", (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1024 / 1024);
    }

    /**
     * Логировать изменение трафика в сети, которая производит чтение байтов.
     *
     * @param profilerType    - метрика, в которую логируем значение.
     * @param readableBytes - количество прочтенных байтов в сети.
     */
    public void logNetworkTrafficBytesRead(ProfilerType profilerType, long readableBytes) {
        doPut(profilerType, "Reads", readableBytes);
    }

    /**
     * Логировать изменение трафика в сети, которая производит запись байтов.
     *
     * @param profilerType    - метрика, в которую логируем значение.
     * @param writableBytes - количество записанных байтов в сети.
     */
    public void logNetworkTrafficBytesWrite(ProfilerType profilerType, long writableBytes) {
        doPut(profilerType, "Writes", writableBytes);
    }

    /**
     * Логировать открытие соединения в рамках определенной сети.
     *
     * @param profilerType - метрика, в которую логируем значение.
     */
    public void logNetworkConnectionOpened(ProfilerType profilerType) {
        doAddAndPut(profilerType, "Connections", 1);
    }

    /**
     * Логировать закрытие соединения в рамках определенной сети.
     *
     * @param profilerType - метрика, в которую логируем значение.
     */
    public void logNetworkConnectionClosed(ProfilerType profilerType) {
        doSubtractAndPut(profilerType, "Connections", 1);
    }

    /**
     * Логировать число успешно исполненных SQL запросов.
     */
    public void logDatabaseSuccessQuery() {
        doAddAndPut(ProfilerType.JDBC_QUERIES, "Completed", 1);
    }

    /**
     * Логировать число провальных SQL запросов.
     */
    public void logDatabaseFailureQuery() {
        doAddAndPut(ProfilerType.JDBC_QUERIES, "Failure", 1);
    }

    /**
     * Логировать число отмененных SQL запросов.
     */
    public void logDatabaseRollbackQuery() {
        doAddAndPut(ProfilerType.JDBC_QUERIES, "Rollback", 1);
    }

    /**
     * Логировать число открытых когда-либо транзакций.
     */
    public void logDatabaseTransaction() {
        doAddAndPut(ProfilerType.JDBC_QUERIES, "Transaction", 1);
    }

// ======================================================== // METRICS LOGGING FUNCTIONS // ======================================================== //

    /**
     * Запросить ссылку на иллюстрацию графика метрики определенного типа.
     *
     * @param profilerType - тип метрики, иллюстрацию которой мы запрашиваем.
     */
    public String requestIllustrationURL(ProfilerType profilerType) {
        Profiler profiler = getMetric(profilerType);
        return PROVIDER.provideMetricIllustration(profilerType.getDefaultChartType(), profiler);
    }

// ================================================================================================================================================= //


    private void registerMetricType(ProfilerType profilerType) {
        Profiler profiler = PROVIDER.createMetric(profilerType.getDisplayName());
        metricsByTypeMap.put(profilerType, profiler);
    }

    private Profiler getMetric(ProfilerType profilerType) {
        Profiler profiler = metricsByTypeMap.get(profilerType);
        if (profiler == null) {
            throw new IllegalStateException("Metric " + profilerType + " is not registered");
        }
        return profiler;
    }

    private void doPut(ProfilerType profilerType, String label, long value) {
        Profiler profiler = getMetric(profilerType);
        profiler.put(label, value);
    }

    private void doAddAndPut(ProfilerType profilerType, String label, long value) {
        Profiler profiler = getMetric(profilerType);
        profiler.add(label, value);
    }

    private void doSubtractAndPut(ProfilerType profilerType, String label, long value) {
        Profiler profiler = getMetric(profilerType);
        profiler.subtract(label, value);
    }
}
