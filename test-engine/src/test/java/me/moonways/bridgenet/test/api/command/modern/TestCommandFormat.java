package me.moonways.bridgenet.test.api.command.modern;

import me.moonways.bridgenet.test.engine.BridgenetJUnitTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.stream.Collectors;

@RunWith(BridgenetJUnitTestRunner.class)
public class TestCommandFormat {

    @Test
    public void parse() {
        String text = "{placeholder}=user_name>{regex}=[a-zA-Z0-9_]{3,16}>{error_msg}=Строка должна содержать имя пользователя";

        // Разделить строку на части, используя регулярное выражение
        String[] split = text.split(">");
    }
}
