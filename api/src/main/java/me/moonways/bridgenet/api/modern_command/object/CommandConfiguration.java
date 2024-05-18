package me.moonways.bridgenet.api.modern_command.object;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.moonways.bridgenet.api.inject.bean.Bean;
import me.moonways.bridgenet.api.inject.bean.BeanMethod;
import me.moonways.bridgenet.api.modern_command.CommandElementType;

import java.util.ArrayList;
import java.util.List;

@Getter
public class CommandConfiguration extends NativeCommandConfiguration {

    private final String name;

    private final String usage;
    private final String description;

    private final CommandElementType elementType;

    private final List<CommandExecutionEntityType> allowedEntities = new ArrayList<>();

    public CommandConfiguration(Bean bean, BeanMethod beanMethod, String name, String usage, String description, CommandElementType elementType) {
        super(bean, beanMethod);

        this.name = name;
        this.usage = usage;
        this.description = description;
        this.elementType = elementType;
    }

    public static Builder builder(Bean bean, BeanMethod beanMethod) {
        return new Builder(bean, beanMethod);
    }

    public void grantAccess(CommandExecutionEntityType entityType) {
        allowedEntities.add(entityType);
    }

    public void denyAccess(CommandExecutionEntityType entityType) {
        allowedEntities.remove(entityType);
    }

    public boolean isExternalElement() {
        return elementType.equals(CommandElementType.EXTERNAL);
    }

    public boolean isInternalElement() {
        return elementType.equals(CommandElementType.INTERNAL);
    }

    @RequiredArgsConstructor
    public static class Builder {

        private final Bean bean;
        private final BeanMethod beanMethod;

        private String name;
        private String usage;
        private String description;
        private CommandElementType elementType;

        public Builder name(String value) {
            this.name = value;
            return this;
        }

        public Builder usage(String value) {
            this.usage = value;
            return this;
        }

        public Builder description(String value) {
            this.description = value;
            return this;
        }

        public Builder elementType(CommandElementType value) {
            this.elementType = value;
            return this;
        }

        public CommandConfiguration build() {
            return new CommandConfiguration(bean, beanMethod, name, usage, description, elementType);
        }
    }
}
