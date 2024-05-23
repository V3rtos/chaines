package me.moonways.bridgenet.test.api;

import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.profiler.BridgenetDataLogger;
import me.moonways.bridgenet.profiler.ProfilerType;
import me.moonways.bridgenet.test.data.junit.assertion.DataAssert;
import me.moonways.bridgenet.test.engine.ModernTestEngineRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

@Log4j2
@RunWith(ModernTestEngineRunner.class)
public class ProfilingDatabaseQueriesTest {

    @Inject
    private BridgenetDataLogger bridgenetDataLogger;

    @Test
    public void test_requestIllustration() {
        logDatabaseQueries();

        String illustrationURL = bridgenetDataLogger.renderProfilerChart(ProfilerType.JDBC_QUERIES);
        log.debug("Completed metric illustration url: {} [click]", illustrationURL);

        DataAssert.assertIllustrationUrl(illustrationURL);
    }

    private void logDatabaseQueries() {
        // 2 success
        bridgenetDataLogger.logJdbcQueryCompleted();
        bridgenetDataLogger.logJdbcQueryCompleted();

        // 1 failure
        bridgenetDataLogger.logJdbcQueryFailed();

        // 4 success
        bridgenetDataLogger.logJdbcQueryCompleted();
        bridgenetDataLogger.logJdbcQueryCompleted();
        bridgenetDataLogger.logJdbcQueryCompleted();
        bridgenetDataLogger.logJdbcQueryCompleted();

        // 1 rollback
        bridgenetDataLogger.logTransactionRollback();

        // 3 failure
        bridgenetDataLogger.logJdbcQueryFailed();
        bridgenetDataLogger.logJdbcQueryFailed();
        bridgenetDataLogger.logJdbcQueryFailed();

        // 2 transaction
        bridgenetDataLogger.logTransactionOpen();
        bridgenetDataLogger.logTransactionOpen();

        // result = [success=[1, 2, 3, 4, 5, 6], failure=[1, 2, 3, 4], rollback=[1], transaction=[1, 2]]
    }
}
