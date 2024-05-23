package me.moonways.bridgenet.profiler;

import java.util.EnumMap;

@SuppressWarnings("SameParameterValue")
public class BridgenetDataLogger {

    private static final ProfilerProvider PROVIDER = new ProfilerProvider();

    private final EnumMap<ProfilerType, Profiler> metricsByTypeMap = new EnumMap<>(ProfilerType.class);

    {
        for (ProfilerType profilerType : ProfilerType.values()) {
            register(profilerType);
        }
    }

// ======================================================== // METRICS LOGGING FUNCTIONS // ======================================================== //

    /**
     * Логировать актуальное значение свободной памяти в системе
     * (измеряется в МБ).
     */
    public void logRuntimeMemoryFree() {
        logPut(ProfilerType.MEMORY, "Free (MB)", Runtime.getRuntime().freeMemory() / 1024 / 1024);
    }

    /**
     * Логировать актуальное значение общего количества памяти в системе
     * (измеряется в МБ).
     */
    public void logRuntimeMemoryTotal() {
        logPut(ProfilerType.MEMORY, "Total (MB)", Runtime.getRuntime().totalMemory() / 1024 / 1024);
    }

    /**
     * Логировать актуальное значение используемой памяти в системе
     * (измеряется в МБ).
     */
    public void logRuntimeMemoryUsed() {
        logPut(ProfilerType.MEMORY, "Used (MB)", (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1024 / 1024);
    }

    /**
     * Логировать изменение трафика в сети, которая производит чтение байтов.
     *
     * @param profilerType    - метрика, в которую логируем значение.
     * @param readableBytes - количество прочтенных байтов в сети.
     */
    public void logReadsCount(ProfilerType profilerType, long readableBytes) {
        logPut(profilerType, "Reads", readableBytes);
    }

    /**
     * Логировать изменение трафика в сети, которая производит запись байтов.
     *
     * @param profilerType    - метрика, в которую логируем значение.
     * @param writableBytes - количество записанных байтов в сети.
     */
    public void logWritesCount(ProfilerType profilerType, long writableBytes) {
        logPut(profilerType, "Writes", writableBytes);
    }

    /**
     * Логировать открытие соединения в рамках определенной сети.
     *
     * @param profilerType - метрика, в которую логируем значение.
     */
    public void logConnectionOpen(ProfilerType profilerType) {
        logAddAndPut(profilerType, "Connections", 1);
    }

    /**
     * Логировать закрытие соединения в рамках определенной сети.
     *
     * @param profilerType - метрика, в которую логируем значение.
     */
    public void logConnectionClose(ProfilerType profilerType) {
        logSubtractAndPut(profilerType, "Connections", 1);
    }

    /**
     * Логировать число успешно исполненных SQL запросов.
     */
    public void logJdbcQueryCompleted() {
        logAddAndPut(ProfilerType.JDBC_QUERIES, "Completed", 1);
    }

    /**
     * Логировать число провальных SQL запросов.
     */
    public void logJdbcQueryFailed() {
        logAddAndPut(ProfilerType.JDBC_QUERIES, "Failure", 1);
    }

    /**
     * Логировать число отмененных SQL запросов.
     */
    public void logTransactionRollback() {
        logAddAndPut(ProfilerType.JDBC_QUERIES, "Rollback", 1);
    }

    /**
     * Логировать число открытых когда-либо транзакций.
     */
    public void logTransactionOpen() {
        logAddAndPut(ProfilerType.JDBC_QUERIES, "Transaction", 1);
    }

// ======================================================== // METRICS LOGGING FUNCTIONS // ======================================================== //

    /**
     * Запросить ссылку на иллюстрацию графика метрики определенного типа.
     *
     * @param profilerType - тип метрики, иллюстрацию которой мы запрашиваем.
     */
    public String renderProfilerChart(ProfilerType profilerType) {
        Profiler profiler = getProfiler(profilerType);
        return PROVIDER.provideMetricIllustration(profilerType.getDefaultChartType(), profiler);
    }

// ================================================================================================================================================= //


    private void register(ProfilerType profilerType) {
        Profiler profiler = PROVIDER.createMetric(profilerType.getDisplayName());
        metricsByTypeMap.put(profilerType, profiler);
    }

    private Profiler getProfiler(ProfilerType profilerType) {
        Profiler profiler = metricsByTypeMap.get(profilerType);
        if (profiler == null) {
            throw new IllegalStateException("Metric " + profilerType + " is not registered");
        }
        return profiler;
    }

    private void logPut(ProfilerType profilerType, String label, long value) {
        Profiler profiler = getProfiler(profilerType);
        profiler.put(label, value);
    }

    private void logAddAndPut(ProfilerType profilerType, String label, long value) {
        Profiler profiler = getProfiler(profilerType);
        profiler.add(label, value);
    }

    private void logSubtractAndPut(ProfilerType profilerType, String label, long value) {
        Profiler profiler = getProfiler(profilerType);
        profiler.subtract(label, value);
    }
}
