package me.moonways.bridgenet.test.api.util.autorun;

import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.test.engine.BridgenetJUnitTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Properties;

import static java.lang.Thread.sleep;
import static org.junit.Assert.assertNotEquals;

@RunWith(BridgenetJUnitTestRunner.class)
public class AutoRunnerTest {

    @Inject
    private Properties props;

    @Test
    public void test_successAssert() throws InterruptedException {
        String property1 = props.getProperty(RandomValueRunner.KEY);

        sleep(1500);

        String property2 = props.getProperty(RandomValueRunner.KEY);

        assertNotEquals(property1, property2);
    }
}
