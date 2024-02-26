package me.moonways.bridgenet.jdbc.core.compose.template;

import me.moonways.bridgenet.jdbc.core.compose.StorageType;
import me.moonways.bridgenet.jdbc.core.compose.TemplatedQuery;
import me.moonways.bridgenet.jdbc.core.compose.impl.collection.element.Encoding;
import me.moonways.bridgenet.jdbc.core.compose.template.completed.CompletedSignature;

public interface CreationTemplate extends TemplatedQuery {

    CreationTemplate entity(StorageType target);

    CreationTemplate name(String value);

    CreationTemplate signature(CompletedSignature signature);

    CreationTemplate encoding(Encoding encoding);
}
