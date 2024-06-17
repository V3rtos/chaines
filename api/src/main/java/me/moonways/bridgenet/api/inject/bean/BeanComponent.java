package me.moonways.bridgenet.api.inject.bean;

import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.api.inject.Property;
import me.moonways.bridgenet.api.util.reflection.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Optional;

public class BeanComponent extends AnnotatedBeanComponent<Field> {

    public BeanComponent(Bean bean, Field root) {
        super(bean, root);
    }

    /**
     * Получить используемый тип компонента бина.
     */
    public Class<?> getType() {
        return getRoot().getType();
    }

    /**
     * Проверить компонент на наличие аннотации
     * инжекции, чтобы совершить над ним дальнейшую
     * инжекцию нужного инстанса бина.
     */
    public boolean isInject() {
        return isAnnotated(Inject.class);
    }

    /**
     * Проверить компонент на наличие аннотации
     * проперти, чтобы инициализировать его как значение,
     * полученное из системных проперти по указанному
     * ключу в аннотации.
     */
    public boolean isProperty() {
        return isAnnotated(Property.class);
    }

    /**
     * Получить значение из системных проперти
     * по значению аннотации проперти как ключа.
     */
    public Optional<String> getPropertyKey() {
        return getAnnotation(Property.class).map(Property::value);
    }

    /**
     * Переустановить значение компонента на новое.
     *
     * @param value - новое значение компонента.
     */
    public void setValue(Object value) {
        Field field = getRoot();
        Bean bean = getBean();

        ReflectionUtils.grantAccess(field);

        try {
            field.set(bean.getRoot(), value);
        } catch (IllegalAccessException exception) {
            throw new BeanException(exception);
        }
    }
}
