package me.moonways.bridgenet.model.audience;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Optional;
import java.util.WeakHashMap;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ComponentHolders {

    private static final String NULL_VALUE = "null";

    public static ComponentHolders begin() {
        return new ComponentHolders();
    }

    private final Map<String, IndividualObject<EntityAudience>> replacements = new WeakHashMap<>();

    public <A extends EntityAudience> ComponentHolders place(@NotNull String holder, @NotNull IndividualObject<A> value) {
        //noinspection unchecked
        replacements.put(holder.toLowerCase(), (IndividualObject<EntityAudience>) value);
        return this;
    }

    public ComponentHolders place(@NotNull String holder, @Nullable Object value) {
        return place(holder, IndividualObject.statical(
                Optional.ofNullable(value)
                        .orElse(NULL_VALUE)));
    }

    public Component apply(EntityAudience entity, Component component) {
        AtomicReference<String> stringRef = new AtomicReference<>(component.toString());
        replacements.forEach((s, individualObject) ->
        {
            String componentText = stringRef.get();
            stringRef.set(componentText
                    .replace(s, individualObject.apply(entity).toString()));
        });

        return Component.text(stringRef.get());
    }

    /**
     * Интерфейс отвечает за реализацию индивидуальных
     * объектов, создающихся относительно данных сущности.
     */
    public interface IndividualObject<A extends EntityAudience> extends Function<A, Object> {
        static IndividualObject<?> statical(Object object) {
            return (IndividualObject<EntityAudience>) entityAudience -> object;
        }
    }
}
