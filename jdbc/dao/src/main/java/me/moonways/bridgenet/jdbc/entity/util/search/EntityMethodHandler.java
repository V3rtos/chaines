package me.moonways.bridgenet.jdbc.entity.util.search;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.moonways.bridgenet.jdbc.entity.util.EntityPersistenceUtil;

import java.lang.reflect.Method;
import java.util.*;

@RequiredArgsConstructor
public class EntityMethodHandler {

    @Getter
    private final List<String> expectedParametersByName = Collections.synchronizedList(new LinkedList<>());

    public Object handle(Object source, Method method, Object[] args) {
        if (EntityPersistenceUtil.isParameter(method)) {
            expectedParametersByName.add(EntityPersistenceUtil.getParameterId(method));
        }
        return null;
    }
}
