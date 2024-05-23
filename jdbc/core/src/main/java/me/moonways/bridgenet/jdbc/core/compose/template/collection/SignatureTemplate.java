package me.moonways.bridgenet.jdbc.core.compose.template.collection;

import me.moonways.bridgenet.jdbc.core.Combinable;
import me.moonways.bridgenet.jdbc.core.compose.CombinedStructs;
import me.moonways.bridgenet.jdbc.core.compose.template.completed.CompletedSignature;

public interface SignatureTemplate extends Combinable<CompletedSignature> {

    SignatureTemplate with(CombinedStructs.CombinedStyledParameter parameter);
}
