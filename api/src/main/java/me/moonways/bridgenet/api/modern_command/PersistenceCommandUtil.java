package me.moonways.bridgenet.api.modern_command;

import lombok.experimental.UtilityClass;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.api.inject.bean.Bean;
import me.moonways.bridgenet.api.inject.bean.BeanMethod;
import me.moonways.bridgenet.api.inject.bean.service.BeansScanningService;
import me.moonways.bridgenet.api.modern_command.object.CommandConfiguration;
import me.moonways.bridgenet.api.modern_command.object.EntityCommand;
import me.moonways.bridgenet.api.modern_command.persistance.CommandElement;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@UtilityClass
public class PersistenceCommandUtil {

    @Inject
    private BeansScanningService scanningService;

    public UUID generateUid(Object object) {
    }

    public CommandConfiguration parseConfiguration(Object object) {
        CommandConfiguration
                .builder(lookupBean(object), lookupBeanMethod(object))
                .name("test")
                .usage("test")
                .description("test")
                .elementType(CommandElementType.EXTERNAL)
                .build();
    }

    public List<EntityCommand> lookupInternalElements(Object object) {
        Bean bean = lookupBean(object);

        List<EntityCommand> entityCommands = new ArrayList<>();

        List<BeanMethod> beanMethods = bean.getType().getAllDeclaredFunctionsByAnnotation(CommandElement.class)
                .stream().filter(element -> element.getAnnotation(CommandElement.class)
                        .get().value()
                        .equals(CommandElementType.INTERNAL))
                .collect(Collectors.toList());

        for (BeanMethod beanMethod : beanMethods) {
            EntityCommand entityCommand = EntityCommand
                    .builder()
                    .id(lookupId(beanMethod))
                    .configuration(lookupConfiguration(beanMethod))
                    .build();

            entityCommands.add(entityCommand);
        }

        return entityCommands;
    }

    private CommandConfiguration lookupConfiguration(BeanMethod beanMethod) {

    }

    private UUID lookupId(BeanMethod beanMethod) {

    }

    private String lookupAlias(BeanMethod beanMethod) {

    }


    private Bean lookupBean(Object object) {

    }

    private BeanMethod lookupBeanMethod(Object object) {

    }
}
