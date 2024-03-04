package me.moonways.bridgenet.test.api.command.modern;

import me.moonways.bridgenet.test.engine.BridgenetJUnitTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;

@RunWith(BridgenetJUnitTestRunner.class)
public class TestCommandFormat {

    @Test
    public void parse() {
        String text = "$placeholder$=user_name[a-zA-Z0-9_]{3,16}$msg$=Строка должна содержать имя пользователя";
        System.out.println(Arrays.toString(text.split("(?<=\\\\])[=]")));
    }
}
