package me.moonways.bridgenet.test.data.junit.assertion;

import lombok.experimental.UtilityClass;
import me.moonways.bridgenet.test.data.ExampleUserEvent;
import me.moonways.bridgenet.test.data.TestConst;

import static org.junit.Assert.*;

@UtilityClass
public class DataAssert {

    public void assertEvent(ExampleUserEvent event) {
        assertNotNull(event);
        assertNotNull(event.getUser());

        assertEquals(event.getUser().getStatus().getName(), TestConst.JdbcEntity.STATUS_NAME);
    }

    public void assertIllustrationUrl(String illustrationURL) {
        assertNotNull(illustrationURL);
        assertNotEquals("", illustrationURL);
    }
}
