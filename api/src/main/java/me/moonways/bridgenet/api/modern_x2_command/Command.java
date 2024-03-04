package me.moonways.bridgenet.api.modern_x2_command;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.moonways.bridgenet.api.inject.bean.Bean;
import me.moonways.bridgenet.api.inject.bean.BeanMethod;

import java.lang.reflect.Method;

@Getter
@RequiredArgsConstructor
public class Command {

    private final Bean bean;
    private final BeanMethod beanMethod;

    private final CommandInfo info;
}
