package me.moonways.bridgenet.jdbc.core.compose.impl.pattern.type.collection;

import me.moonways.bridgenet.jdbc.core.compose.CombinedStructs;
import me.moonways.bridgenet.jdbc.core.compose.template.collection.SignatureTemplate;
import me.moonways.bridgenet.jdbc.core.compose.template.completed.CompletedSignature;

import java.util.ArrayList;
import java.util.List;

public class SignaturePattern implements SignatureTemplate, CompletedSignature {

    private final List<CombinedStructs.CombinedStyledParameter> list = new ArrayList<>();

    @Override
    public CompletedSignature combine() {
        return this;
    }

    @Override
    public SignatureTemplate with(CombinedStructs.CombinedStyledParameter parameter) {
        list.add(parameter);
        return this;
    }

    @Override
    public CombinedStructs.CombinedStyledParameter[] parameters() {
        return list.toArray(new CombinedStructs.CombinedStyledParameter[0]);
    }
}
