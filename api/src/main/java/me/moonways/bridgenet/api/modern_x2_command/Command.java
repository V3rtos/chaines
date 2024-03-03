package me.moonways.bridgenet.api.modern_x2_command;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.moonways.bridgenet.api.inject.bean.Bean;
import me.moonways.bridgenet.api.inject.bean.BeanMethod;

import java.lang.reflect.Method;

@RequiredArgsConstructor
public class Command {

    @Getter
    private final Bean bean;
    @Getter
    private final BeanMethod beanMethod;

    @Getter
    private final CommandInfo info;
}
