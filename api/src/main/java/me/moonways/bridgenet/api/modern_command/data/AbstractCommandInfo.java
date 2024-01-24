package me.moonways.bridgenet.api.modern_command.data;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import me.moonways.bridgenet.api.modern_command.cooldown.info.CooldownInfo;
import me.moonways.bridgenet.api.modern_command.entity.EntityType;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Getter
@Setter
public abstract class AbstractCommandInfo implements CommandInfo {

    private final Map<Object, Object> property = new HashMap<>();

    private final Object parent;
    private final Method handle;
    private final String[] aliases;

    private String description;
    private String permission;

    private EntityType entityType;
    private CooldownInfo cooldown;

    @Override
    public abstract CommandType getCommandType();

    @Override
    public void addProperty(@NotNull Object key, @NotNull Object value) {
        property.put(key, value);
    }

    @Override
    public void removeProperty(@NotNull Object key) {
        property.remove(key);
    }

    @Override
    public Object getPropertyValue(@NotNull Object key) {
        return property.get(key);
    }

    @Override
    public boolean isExistsProperty(@NotNull Object key) {
        return property.containsKey(key);
    }
}
