package me.moonways.bridgenet.api.inject.bean;

import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.api.inject.PostConstruct;
import me.moonways.bridgenet.api.inject.PreConstruct;
import me.moonways.bridgenet.api.util.reflection.ReflectionUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

@Log4j2
public class BeanMethod extends AnnotatedBeanComponent<Method> {
    public BeanMethod(Bean bean, Method root) {
        super(bean, root);
    }

    /**
     * Проверяем, должна ли функция выполняться после
     * инициализации и сохранения бина.
     */
    public boolean isBefore() {
        return isAnnotated(PreConstruct.class);
    }

    /**
     * Проверяем, должна ли функция выполняться перед
     * инициализацией и сохранением бина.
     */
    public boolean isAfter() {
        return isAnnotated(PostConstruct.class);
    }

    /**
     * Проверить функцию на наличие конфликтов.
     */
    public boolean hasConflicts() {
        return (isAfter() && isBefore()) || (isBefore() && !isStatic()) || getRoot().getParameterCount() != 0;
    }

    /**
     * Проверить функцию на статичность.
     */
    private boolean isStatic() {
        return (getRoot().getModifiers() & Modifier.STATIC) == Modifier.STATIC;
    }

    /**
     * Вызвать исполнение функции
     * инициализации бина.
     */
    public Object invoke(Object... args) {
        if ((isAfter() || isBefore()) && hasConflicts()) {
            throw new BeanException("Method was detected conflicts: " + this);
        }

        Method method = getRoot();
        Bean bean = getBean();

        ReflectionUtils.grantAccess(method);

        try {
            log.debug("invoke §2{}.{}() §7[{}]", bean.getClassName(), method.getName(), getSource());
            return method.invoke(bean.getRoot(), args);

        } catch (Throwable exception) {
            throw new BeanException(method.toString(), exception);
        }
    }

    private String getSource() {
        if (isAfter()) {
            return ":PostConstruct";
        }
        if (isBefore()) {
            return ":PreConstruct";
        }
        return ":native";
    }
}
