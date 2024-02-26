package me.moonways.bridgenet.jdbc.core.compose.impl.pattern.type.query;

import me.moonways.bridgenet.jdbc.core.compose.impl.pattern.AbstractPattern;
import me.moonways.bridgenet.jdbc.core.compose.impl.pattern.PatternCollections;
import me.moonways.bridgenet.jdbc.core.compose.impl.pattern.verification.VerificationContext;
import me.moonways.bridgenet.jdbc.core.compose.impl.pattern.verification.VerificationResult;
import me.moonways.bridgenet.jdbc.core.compose.CombinedStructs;
import me.moonways.bridgenet.jdbc.core.compose.template.RestorationTemplate;
import org.jetbrains.annotations.NotNull;

public class RestorationTemplatedPattern extends AbstractPattern implements RestorationTemplate {

    public RestorationTemplatedPattern() {
        super(PatternCollections.fromPattern("restoration.pattern"));
    }

    @Override
    public RestorationTemplate container(String table) {
        return this;
    }

    @Override
    public RestorationTemplate update(CombinedStructs.CombinedStyledParameter parameter) {
        return this;
    }

    @Override
    public RestorationTemplate add(CombinedStructs.CombinedStyledParameter parameter) {
        return this;
    }

    @Override
    public RestorationTemplate delete(CombinedStructs.CombinedField field) {
        return this;
    }

    @Override
    public RestorationTemplate rename(CombinedStructs.CombinedRenaming renaming) {
        return this;
    }

    @Override
    public VerificationResult verify(@NotNull VerificationContext context) {
        return context.asError("not filled");
    }
}
