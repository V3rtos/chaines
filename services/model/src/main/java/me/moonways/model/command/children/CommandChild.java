package me.moonways.model.command.children;

import java.lang.reflect.Method;

public interface CommandChild { //тут мог быть дрочилдрен

    Object getParent();

    Method getMethod();
}
