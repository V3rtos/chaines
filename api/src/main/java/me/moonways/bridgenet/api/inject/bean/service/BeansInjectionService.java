package me.moonways.bridgenet.api.inject.bean.service;

import lombok.RequiredArgsConstructor;
import me.moonways.bridgenet.api.inject.WrappedProperty;
import me.moonways.bridgenet.api.inject.bean.Bean;
import me.moonways.bridgenet.api.inject.bean.BeanComponent;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public final class BeansInjectionService {

    private final Set<BeanComponent> definitionsQueueSet = Collections.synchronizedSet(new HashSet<>());
    private final Map<Class<?>, Consumer<Bean>> beansSubscriptionsOnQueueLeaveMap = new HashMap<>();

    private final BeansStore store;
    private final BeansScanningService scanner;

    /**
     * Проинициализировать значения компонентов (полей
     * объекта, нуждающиеся в инжекции) путем преобразования
     * его в бин и редактирования компонентов напрямую из бина.
     *
     * @param object - объект, который инициализируем.
     */
    public synchronized void injectComponents(Object object) {
        Bean bean = scanner.createBean(object.getClass(), object);

        doInject(bean);
        doInjectProperties(bean);
    }

    /**
     * Выполнить инжекцию только что преобразованного
     * бина из целевого объекта инжекции.
     *
     * @param bean - преобразованный бин.
     */
    private void doInject(Bean bean) {
        List<BeanComponent> components = bean.getType().getInjectComponents();

        for (BeanComponent component : components) {
            doInjectComponent(component);
        }
    }

    /**
     * Выполнить инжекцию относительно одного
     * целевого компонента бина.
     *
     * @param component - целевой компонент.
     */
    private void doInjectComponent(BeanComponent component) {
        Optional<Bean> componentTypeBeanOptional = store.find(component.getType());

        if (!componentTypeBeanOptional.isPresent()) {
            definitionsQueueSet.add(component);
            return;
        }

        component.setValue(componentTypeBeanOptional.get().getRoot());
        definitionsQueueSet.remove(component);

        if (!isQueued(component.getBean())) {
            callLeavesAtQueueSubscriptions(component.getBean());
        }
    }

    /**
     * Попытка воспроизвести процесс self-injection для
     * указанного компонента бина.
     *
     * @param component - компонент бина, которому производим self-injection.
     * @return - true, если удалось воспроизвести, и false если не удалось.
     */
    public synchronized boolean injectSelf(BeanComponent component) {
        Bean bean = component.getBean();

        if (component.getType().isAssignableFrom(bean.getType().getRoot())) {
            component.setValue(bean.getRoot());
            return true;
        }

        return false;
    }

    /**
     * Находится ли указанный класс бина в ожидании
     * полной инжекции всех необходимых компонентов.
     *
     * @param rootClass - класс корня бина.
     */
    public boolean isQueued(Class<?> rootClass) {
        return definitionsQueueSet.stream()
                .anyMatch(beanComponent -> beanComponent.getBean().getType()
                        .isSimilar(rootClass));
    }

    /**
     * Находится ли указанный бин в ожидании
     * полной инжекции всех необходимых компонентов.
     *
     * @param bean - бин.
     */
    public boolean isQueued(Bean bean) {
        return definitionsQueueSet.stream()
                .anyMatch(beanComponent -> beanComponent.getBean().isSimilar(bean));
    }

    /**
     * Данный метод вызывается в случае появления нового
     * кешированного бина для того, чтобы проинициализировать
     * поля, не успевшие получить свой бин из кеша вовремя.
     */
    public synchronized void touchQueue() {
        HashSet<BeanComponent> clone = new HashSet<>(definitionsQueueSet); // fix CME
        clone.forEach(this::doInjectComponent);
    }

    /**
     * Выполнить инициализацию полей, нуждающихся в значении
     * пропертей по указанному в аннотации Property ключу.
     *
     * @param bean - бин, в котором инициализируем проперти.
     */
    private void doInjectProperties(Bean bean) {
        List<BeanComponent> components = bean.getType().getPropertyComponents();

        for (BeanComponent component : components) {
            //noinspection DataFlowIssue
            String property = System.getProperty(component.getPropertyKey().orElse(null));

            if (component.getType().equals(String.class)) {
                component.setValue(property);
            }
            if (component.getType().equals(WrappedProperty.class)) {
                component.setValue(WrappedProperty.fromBean(component));
            }
        }
    }

    /**
     * Подписаться на событие о выходе из очереди
     * инжекции бина по его корневому классу.
     *
     * @param rootClass - корневой класс бина, по которому искать в очереди.
     * @param beanConsumer - обработчик события выхода из очереди ожидания.
     */
    public synchronized void subscribeLeaveAtQueue(Class<?> rootClass, Consumer<Bean> beanConsumer) {
        Consumer<Bean> subscription = beansSubscriptionsOnQueueLeaveMap.get(rootClass);
        if (subscription == null) {
            subscription = beanConsumer;
        } else {
            subscription = subscription
                    .andThen(beanConsumer);
        }
        beansSubscriptionsOnQueueLeaveMap.put(rootClass, subscription);
    }

    private void callLeavesAtQueueSubscriptions(Bean bean) {
        Class<?> rootClass = bean.getRoot().getClass();
        Consumer<Bean> consumer = beansSubscriptionsOnQueueLeaveMap.remove(rootClass);

        if (consumer != null) {
            consumer.accept(bean);
        }
    }
}
