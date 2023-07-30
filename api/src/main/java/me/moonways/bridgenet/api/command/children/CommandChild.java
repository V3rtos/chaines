package me.moonways.bridgenet.api.command.children;

import java.lang.reflect.Method;

public interface CommandChild { //тут мог быть дрочилдрен

    Object getParent();

    Method getMethod();
}
