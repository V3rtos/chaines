package me.moonways.bridgenet.api.modern_command.object;

import lombok.Builder;
import lombok.Getter;
import me.moonways.bridgenet.api.modern_command.PersistenceCommandUtil;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@Builder
public class EntityCommand  {

    private final UUID id;
    private final CommandConfiguration configuration;

    private final List<EntityCommand> internalElements;

    public static EntityCommand of(Object object) {
        return EntityCommand.builder()
                .id(PersistenceCommandUtil.generateUid(object))
                .configuration(PersistenceCommandUtil.parseConfiguration(object))
                .internalElements(PersistenceCommandUtil.lookupInternalElements(object))
                .build();
    }

    public EntityCommand lookupInternalElement(String name) {
        return internalElements.stream()
                .filter(element -> element.getConfiguration().getName().equalsIgnoreCase(name))
                .collect(Collectors.toList())
                .stream()
                .findFirst()
                .orElse(null);
    }

    public boolean hasInternalElement(String name) {
        return (long) (int) internalElements.stream()
                .filter(element -> element.getConfiguration().getName().equalsIgnoreCase(name)).count() > 0;
    }

    public boolean isEmptyInternalElements() {
        return internalElements.isEmpty();
    }
}
