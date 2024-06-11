package me.moonways.bridgenet.api.inject.bean.service;

import lombok.RequiredArgsConstructor;
import me.moonways.bridgenet.api.inject.BeanPropertyWrapper;
import me.moonways.bridgenet.api.inject.bean.Bean;
import me.moonways.bridgenet.api.inject.bean.BeanComponent;

import java.util.*;

@RequiredArgsConstructor
public class BeansInjectionService {

    private final Set<BeanComponent> componentsInjectionQueueSet = Collections.synchronizedSet(new HashSet<>());

    private final BeansStore store;
    private final BeansScanningService scanner;

    /**
     * Проинициализировать значения компонентов (полей
     * объекта, нуждающиеся в инжекции) путем преобразования
     * его в бин и редактирования компонентов напрямую из бина.
     *
     * @param object - объект, который инициализируем.
     */
    public void injectComponents(Object object) {
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
        Optional<Bean> beanOptional = store.find(component.getType());

        if (!beanOptional.isPresent()) {
            componentsInjectionQueueSet.add(component);
            return;
        }

        Bean bean = beanOptional.get();
        component.setValue(bean.getRoot());

        componentsInjectionQueueSet.remove(component);
    }

    /**
     * Попытка воспроизвести процесс self-injection для
     * указанного компонента бина.
     *
     * @param component - компонент бина, которому производим self-injection.
     * @return - true, если удалось воспроизвести, и false если не удалось.
     */
    public boolean injectSelf(BeanComponent component) {
        Bean bean = component.getBean();

        if (component.getType().isAssignableFrom(bean.getType().getRoot())) {
            component.setValue(bean.getRoot());
            return true;
        }

        return false;
    }

    /**
     * Данный метод вызывается в случае появления нового
     * кешированного бина для того, чтобы проинициализировать
     * поля, не успевшие получить свой бин из кеша вовремя.
     */
    public void touchInjectionQueue() {
        HashSet<BeanComponent> clone = new HashSet<>(componentsInjectionQueueSet); // fix CME
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
            if (component.getType().equals(BeanPropertyWrapper.class)) {
                component.setValue(BeanPropertyWrapper.from(component));
            }
        }
    }
}
