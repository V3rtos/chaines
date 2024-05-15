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
public class ProfilingNetworkTrafficTest {

    @Inject
    private BridgenetMetricsLogger bridgenetMetricsLogger;

    @Test
    public void test_requestIllustration() {
        logNetworkConnections();

        String illustrationURL = bridgenetMetricsLogger.requestIllustrationURL(MetricType.MTP_CONNECTIONS);
        log.debug("Completed metric illustration url: {} [click]", illustrationURL);

        DataAssert.assertIllustrationUrl(illustrationURL);
    }

    private void logNetworkConnections() {
        MetricType metricType = MetricType.MTP_CONNECTIONS;

        // 3 open
        bridgenetMetricsLogger.logNetworkConnectionOpened(metricType);
        bridgenetMetricsLogger.logNetworkConnectionOpened(metricType);
        bridgenetMetricsLogger.logNetworkConnectionOpened(metricType);

        // 2 close
        bridgenetMetricsLogger.logNetworkConnectionClosed(metricType);
        bridgenetMetricsLogger.logNetworkConnectionClosed(metricType);

        // 1 open
        bridgenetMetricsLogger.logNetworkConnectionOpened(metricType);

        // result = [1, 2, 3, 2, 1, 2]
    }
}
