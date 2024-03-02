package me.moonways.bridgenet.test.engine.jdbc.entity;

import lombok.*;
import me.moonways.bridgenet.jdbc.dao.entity.EntityAccessible;
import me.moonways.bridgenet.jdbc.dao.entity.EntityAutoPersistence;

@EntityAutoPersistence
@EntityAccessible(name = "Groups")
@Getter
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode
public class Status {

    private int id;

    private final String name;
}
