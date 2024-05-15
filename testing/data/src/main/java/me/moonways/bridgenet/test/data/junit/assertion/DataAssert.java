package me.moonways.bridgenet.test.data.junit.assertion;

import lombok.experimental.UtilityClass;
import me.moonways.bridgenet.test.data.ExampleUserEvent;
import me.moonways.bridgenet.test.data.TestConst;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@UtilityClass
public class DataAssert {

    public void assertEvent(ExampleUserEvent event) {
        assertNotNull(event);
        assertNotNull(event.getUser());

        assertEquals(event.getUser().getStatus().getName(), TestConst.Entity.STATUS_NAME);
    }

}
