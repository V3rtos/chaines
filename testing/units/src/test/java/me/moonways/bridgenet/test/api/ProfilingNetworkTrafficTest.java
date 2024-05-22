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
public class ProfilingNetworkTrafficTest {

    @Inject
    private BridgenetDataLogger bridgenetDataLogger;

    @Test
    public void test_requestIllustration() {
        logNetworkConnections();

        String illustrationURL = bridgenetDataLogger.requestIllustrationURL(ProfilerType.MTP_CONNECTIONS);
        log.debug("Completed metric illustration url: {} [click]", illustrationURL);

        DataAssert.assertIllustrationUrl(illustrationURL);
    }

    private void logNetworkConnections() {
        ProfilerType profilerType = ProfilerType.MTP_CONNECTIONS;

        // 3 open
        bridgenetDataLogger.logNetworkConnectionOpened(profilerType);
        bridgenetDataLogger.logNetworkConnectionOpened(profilerType);
        bridgenetDataLogger.logNetworkConnectionOpened(profilerType);

        // 2 close
        bridgenetDataLogger.logNetworkConnectionClosed(profilerType);
        bridgenetDataLogger.logNetworkConnectionClosed(profilerType);

        // 1 open
        bridgenetDataLogger.logNetworkConnectionOpened(profilerType);

        // result = [1, 2, 3, 2, 1, 2]
    }
}
