package me.moonways.bridgenet.test.api;

import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.metrics.BridgenetMetricsLogger;
import me.moonways.bridgenet.metrics.MetricType;
import me.moonways.bridgenet.test.data.junit.assertion.DataAssert;
import me.moonways.bridgenet.test.engine.ModernTestEngineRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

@Log4j2
@RunWith(ModernTestEngineRunner.class)
public class ProfilingDatabaseQueriesTest {

    @Inject
    private BridgenetMetricsLogger bridgenetMetricsLogger;

    @Test
    public void test_requestIllustration() {
        logDatabaseQueries();

        String illustrationURL = bridgenetMetricsLogger.requestIllustrationURL(MetricType.JDBC_QUERIES);
        log.debug("Completed metric illustration url: {} [click]", illustrationURL);

        DataAssert.assertIllustrationUrl(illustrationURL);
    }

    private void logDatabaseQueries() {
        // 2 success
        bridgenetMetricsLogger.logDatabaseSuccessQuery();
        bridgenetMetricsLogger.logDatabaseSuccessQuery();

        // 1 failure
        bridgenetMetricsLogger.logDatabaseFailureQuery();

        // 4 success
        bridgenetMetricsLogger.logDatabaseSuccessQuery();
        bridgenetMetricsLogger.logDatabaseSuccessQuery();
        bridgenetMetricsLogger.logDatabaseSuccessQuery();
        bridgenetMetricsLogger.logDatabaseSuccessQuery();

        // 1 rollback
        bridgenetMetricsLogger.logDatabaseRollbackQuery();

        // 3 failure
        bridgenetMetricsLogger.logDatabaseFailureQuery();
        bridgenetMetricsLogger.logDatabaseFailureQuery();
        bridgenetMetricsLogger.logDatabaseFailureQuery();

        // 2 transaction
        bridgenetMetricsLogger.logDatabaseTransaction();
        bridgenetMetricsLogger.logDatabaseTransaction();

        // result = [success=[1, 2, 3, 4, 5, 6], failure=[1, 2, 3, 4], rollback=[1], transaction=[1, 2]]
    }
}
