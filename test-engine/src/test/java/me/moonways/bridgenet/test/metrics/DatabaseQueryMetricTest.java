package me.moonways.bridgenet.test.metrics;

import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.metrics.BridgenetMetricsLogger;
import me.moonways.bridgenet.metrics.MetricType;
import me.moonways.bridgenet.test.engine.BridgenetJUnitTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

@Log4j2
@RunWith(BridgenetJUnitTestRunner.class)
public class DatabaseQueryMetricTest {

    @Inject
    private BridgenetMetricsLogger bridgenetMetricsLogger;

    @Test
    public void test_requestIllustration() {
        logDatabaseQueries();

        String illustrationURL = bridgenetMetricsLogger.requestIllustrationURL(MetricType.JDBC_QUERIES);

        assertIllustration(illustrationURL);
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

    private void assertIllustration(String illustrationURL) {
        log.debug("Completed metric illustration url: {} [click]", illustrationURL);

        assertNotNull(illustrationURL);
        assertNotEquals("", illustrationURL);
    }
}
