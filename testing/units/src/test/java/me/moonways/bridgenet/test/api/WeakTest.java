package me.moonways.bridgenet.test.api;

import me.moonways.bridgenet.api.inject.Weak;
import me.moonways.bridgenet.test.data.ExampleText;
import me.moonways.bridgenet.test.engine.ModernTestEngineRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@RunWith(ModernTestEngineRunner.class)
public class WeakTest {

    private final Weak<ExampleText> weak1 = Weak.empty();

    private final Weak<ExampleText> weak2 = Weak.of(new ExampleText("Hello world!"));

    @Test
    public void test() {
        assertNull(weak1.get());

        assertNotNull(weak2.get());
        weak2.clear();
        assertNull(weak2.get());
    }
}
