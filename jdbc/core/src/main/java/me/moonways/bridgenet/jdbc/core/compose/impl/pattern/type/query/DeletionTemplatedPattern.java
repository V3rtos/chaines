package me.moonways.bridgenet.jdbc.core.compose.impl.pattern.type.query;

import lombok.var;
import me.moonways.bridgenet.jdbc.core.compose.impl.pattern.AbstractPattern;
import me.moonways.bridgenet.jdbc.core.compose.impl.pattern.PatternCollectionConfigurator;
import me.moonways.bridgenet.jdbc.core.compose.impl.pattern.PatternCollections;
import me.moonways.bridgenet.jdbc.core.compose.impl.pattern.verification.VerificationContext;
import me.moonways.bridgenet.jdbc.core.compose.impl.pattern.verification.VerificationResult;
import me.moonways.bridgenet.jdbc.core.compose.template.DeletionTemplate;
import me.moonways.bridgenet.jdbc.core.compose.template.completed.CompletedPredicates;
import org.jetbrains.annotations.NotNull;

public class DeletionTemplatedPattern extends AbstractPattern implements DeletionTemplate {

    public DeletionTemplatedPattern() {
        super(PatternCollections.fromPattern("deletion.pattern"));
    }

    @Override
    public DeletionTemplate container(String containerName) {
        PatternCollectionConfigurator.create("container")
                .pushStringOnly(containerName)
                .adjust(getTotals());
        return this;
    }

    @Override
    public DeletionTemplate predicates(CompletedPredicates predicates) {
        PatternCollectionConfigurator.create("predicates")
                .pushConditionsOnly(predicates)
                .adjust(getTotals());
        return this;
    }

    @Override
    public VerificationResult verify(@NotNull VerificationContext verificationContext) {
        var containerCollectionName = "container";
        var container = getTotals().get(containerCollectionName);

        if (container == null || container.isEmpty()) {
            return verificationContext.asNotEnoughData(containerCollectionName);
        }
        return verificationContext.asSuccessful();
    }
}
