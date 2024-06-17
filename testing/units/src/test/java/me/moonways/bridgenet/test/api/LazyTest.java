package me.moonways.bridgenet.test.api;

import me.moonways.bridgenet.api.inject.Lazy;
import me.moonways.bridgenet.api.inject.bean.factory.FactoryType;
import me.moonways.bridgenet.test.data.ExampleText;
import me.moonways.bridgenet.test.engine.ModernTestEngineRunner;
import me.moonways.bridgenet.test.engine.persistance.BeforeAll;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertNotNull;

@RunWith(ModernTestEngineRunner.class)
public class LazyTest {

    private final Lazy<ExampleText> lazy1 = Lazy.ofFactory(ExampleText.class);

    private final Lazy<ExampleText> lazy2 = Lazy.ofFactory(ExampleText.class, FactoryType.UNSAFE);

    private final Lazy<ExampleText> lazy3 = Lazy.of(ExampleText.class, () -> new ExampleText("Hello world!"));

    @BeforeAll
    public void setUp() {
        lazy1.whenInit(exampleText -> System.out.println("lazy1: " + exampleText));
        lazy2.whenInit(exampleText -> System.out.println("lazy2: " + exampleText));
        lazy3.whenInit(exampleText -> System.out.println("lazy3: " + exampleText));
    }

    @Test
    public void test_notNull() {
        assertNotNull(lazy1.get());
        assertNotNull(lazy2.get());
        assertNotNull(lazy3.get());
    }
}
