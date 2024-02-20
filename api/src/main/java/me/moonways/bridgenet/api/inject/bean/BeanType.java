package me.moonways.bridgenet.api.inject.bean;

import lombok.Getter;
import lombok.ToString;
import me.moonways.bridgenet.api.inject.Autobind;
import me.moonways.bridgenet.api.inject.bean.factory.BeanFactoryProvider;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@ToString
public class BeanType extends AnnotatedBeanComponent<Class<?>> {

    @Getter
    private final Class<?> root;
    @Getter
    private final Class<?>[] interfaces;

    @ToString.Exclude
    private final transient AtomicReference<Bean> beanRef;

    public BeanType(AtomicReference<Bean> beanRef, Class<?> root, Class<?>[] interfaces) {
        super(null, root);

        this.beanRef = beanRef;
        this.root = root;
        this.interfaces = interfaces;
    }

    /**
     * Проверить на схожесть бина по классам,
     * принадлежащим данному бину.
     *
     * @param clazz - класс, по которому сверяем данный бин.
     */
    public boolean isSimilar(Class<?> clazz) {
        return isSimilarExact(clazz) || Stream.of(interfaces).anyMatch(aClass -> aClass.isAssignableFrom(clazz));
    }

    /**
     * Проверить на схожесть бина по классам,
     * принадлежащим данному бину.
     *
     * @param clazz - класс, по которому сверяем данный бин.
     */
    public boolean isSimilarExact(Class<?> clazz) {
        return root.equals(clazz);
    }

    /**
     * Создать и получить список всех ресурсов,
     * которые принадлежат данному бину.
     */
    public Class<?>[] getTotalResources() {
        Set<Class<?>> result = new HashSet<>();

        result.add(root);
        result.addAll(Arrays.asList(interfaces));
        return result.toArray(new Class[0]);
    }

    /**
     * Получить установленную фабрику инстансов бина, прописанную
     * в аннотации автоматического бинда.
     */
    public BeanFactoryProvider getAutoFactoryProvider() {
        return getAnnotation(Autobind.class).map(autobind -> autobind.provider().getImpl())
                .orElse(null);
    }

    /**
     * Проверить на наличие аннотации
     * автоматического бинда текущего бина.
     */
    public boolean isAuto() {
        return isAnnotated(Autobind.class);
    }

    /**
     * Получить массив всех объявленных полей класса,
     * включая поля в наследниках.
     *
     * @param root - класс, в котором пытаемся обнаружить поля.
     */
    private Field[] getDeclaredFields(Class<?> root) {
        Set<Field> result = new HashSet<>(Arrays.asList(root.getDeclaredFields()));

        Class<?> superclass = lookupSuperclass(root);

        if (superclass != null) {
            result.addAll(Arrays.asList(getDeclaredFields(superclass)));
        }

        return result.toArray(new Field[0]);
    }

    /**
     * Получить массив всех функций класса,
     * включая функции в наследниках.
     *
     * @param root - класс, в котором пытаемся обнаружить функции.
     */
    private Method[] getDeclaredMethods(Class<?> root) {
        Set<Method> result = new HashSet<>(Arrays.asList(root.getDeclaredMethods()));

        Class<?> superclass = lookupSuperclass(root);

        if (superclass != null) {
            result.addAll(Arrays.asList(getDeclaredMethods(superclass)));
        }

        return result.toArray(new Method[0]);
    }

    /**
     * Найти и получить корректный класс наследник,
     * который не будет являться базовыми экземплярами,
     * а также относится к проекту, где
     * и был найден указанный класс.
     *
     * @param root - класс, в котором ищем наследник.
     */
    private Class<?> lookupSuperclass(Class<?> root) {
        Class<?> superclass = root.getSuperclass();

        if (!superclass.equals(Object.class)) {
            Package rootPackage = root.getPackage();

            if (rootPackage != null) {
                String[] packagesNames = rootPackage.getName().split("\\.");

                String packageName;
                if (packagesNames.length > 1) {
                    packageName = packagesNames[0] + "." + packagesNames[1];
                } else {
                    packageName = String.join(".", packagesNames);
                }

                if (superclass.getName().contains(packageName)) {
                    return superclass;
                }
            }
        }

        return null;
    }

    /**
     * Получить весь список вложенных компонентов (полей)
     * бина, нуждающихся в применении автоматической инжекции.
     */
    public List<BeanComponent> getInjectComponents() {
        Class<?> root = beanRef.get().getRoot().getClass();
        return Stream.of(getDeclaredFields(root)).map(this::toComponent)
                .filter(BeanComponent::isInject).collect(Collectors.toList());
    }

    /**
     * Получить весь список вложенных компонентов (полей)
     * бина, нуждающихся в применении автоматической инжекции
     * инстанса экземпляра, в котором они и находятся.
     */
    public List<BeanComponent> getInjectSelfComponents() {
        Class<?> root = beanRef.get().getRoot().getClass();
        return Stream.of(getDeclaredFields(root)).map(this::toComponent)
                .filter(BeanComponent::isInject).filter(component -> component.getType().isAssignableFrom(root))
                .collect(Collectors.toList());
    }

    /**
     * Получить весь список вложенных компонентов (полей)
     * бина, нуждающихся в применении автоматической
     * инициализации пропертей.
     */
    public List<BeanComponent> getPropertyComponents() {
        Class<?> root = beanRef.get().getRoot().getClass();
        return Stream.of(getDeclaredFields(root)).map(this::toComponent)
                .filter(BeanComponent::isProperty).collect(Collectors.toList());
    }

    /**
     * Получить весь список вложенных компонентов бина.
     */
    public List<BeanComponent> getAllComponents() {
        Class<?> root = beanRef.get().getRoot().getClass();
        return Stream.of(getDeclaredFields(root)).map(this::toComponent)
                .collect(Collectors.toList());
    }

    /**
     * Получить список функций бина, которые обязаны будут
     * быть выполнены перед инициализацией и конструированием
     * инстанса бина.
     */
    public List<BeanMethod> getPreConstructFunctions() {
        Class<?> root = beanRef.get().getRoot().getClass();
        return Stream.of(root.getMethods()).map(this::toBeanMethod).filter(BeanMethod::isBefore)
                .collect(Collectors.toList());
    }

    /**
     * Получить список функций бина, которые обязаны будут
     * быть выполнены после инициализации и конструирования
     * инстанса бина.
     */
    public List<BeanMethod> getPostConstructFunctions() {
        Class<?> root = beanRef.get().getRoot().getClass();
        return Stream.of(getDeclaredMethods(root)).map(this::toBeanMethod).filter(BeanMethod::isAfter)
                .collect(Collectors.toList());
    }

    /**
     * Преобразовать поле из класса бина в компонент
     * с удобными утилитами для использования системы
     * бинов.
     *
     * @param field - поле из класса бина.
     */
    private BeanComponent toComponent(Field field) {
        return new BeanComponent(beanRef.get(), field);
    }

    /**
     * Преобразовать метод из класса бина в функцию
     * конструирования и инициализации бина.
     *
     * @param method - метод из класса бина.
     */
    private BeanMethod toBeanMethod(Method method) {
        return new BeanMethod(beanRef.get(), method);
    }
}
