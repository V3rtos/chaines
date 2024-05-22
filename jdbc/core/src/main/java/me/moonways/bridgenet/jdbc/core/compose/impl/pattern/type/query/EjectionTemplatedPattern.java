package me.moonways.bridgenet.jdbc.core.compose.impl.pattern.type.query;

import me.moonways.bridgenet.jdbc.core.compose.StorageType;
import me.moonways.bridgenet.jdbc.core.compose.impl.pattern.AbstractPattern;
import me.moonways.bridgenet.jdbc.core.compose.impl.pattern.PatternCollectionConfigurator;
import me.moonways.bridgenet.jdbc.core.compose.impl.pattern.PatternCollections;
import me.moonways.bridgenet.jdbc.core.compose.impl.pattern.verification.VerificationContext;
import me.moonways.bridgenet.jdbc.core.compose.impl.pattern.verification.VerificationResult;
import me.moonways.bridgenet.jdbc.core.compose.template.EjectionTemplate;
import org.jetbrains.annotations.NotNull;

public class EjectionTemplatedPattern extends AbstractPattern implements EjectionTemplate {

    private static final String ENTITY = "entity";
    private static final String NAME = "name";

    public EjectionTemplatedPattern() {
        super(PatternCollections.fromPattern("ejection.pattern"));
    }

    @Override
    public EjectionTemplate entity(StorageType entity) {
        PatternCollectionConfigurator.create(ENTITY)
                .pushStringOnly(entity.toString())
                .adjust(getTotals());
        return this;
    }

    @Override
    public EjectionTemplate name(String specifiedName) {
        PatternCollectionConfigurator.create(NAME)
                .pushStringOnly(specifiedName)
                .adjust(getTotals());
        return this;
    }

    @Override
    public VerificationResult verify(@NotNull VerificationContext verificationContext) {
        return verificationContext.newTransaction(getTotals())
                .markRequiredCollections(ENTITY, NAME)
                .commitVerificationResult();
    }
}
