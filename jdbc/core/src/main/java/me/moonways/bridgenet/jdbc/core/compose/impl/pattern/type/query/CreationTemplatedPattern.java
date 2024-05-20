package me.moonways.bridgenet.jdbc.core.compose.impl.pattern.type.query;

import me.moonways.bridgenet.jdbc.core.compose.CombinedStructs;
import me.moonways.bridgenet.jdbc.core.compose.ParameterSignature;
import me.moonways.bridgenet.jdbc.core.compose.impl.collection.element.Encoding;
import me.moonways.bridgenet.jdbc.core.compose.impl.pattern.AbstractPattern;
import me.moonways.bridgenet.jdbc.core.compose.impl.pattern.PatternCollectionConfigurator;
import me.moonways.bridgenet.jdbc.core.compose.impl.pattern.PatternCollections;
import me.moonways.bridgenet.jdbc.core.compose.impl.pattern.verification.VerificationContext;
import me.moonways.bridgenet.jdbc.core.compose.impl.pattern.verification.VerificationResult;
import me.moonways.bridgenet.jdbc.core.compose.template.completed.CompletedSignature;
import me.moonways.bridgenet.jdbc.core.compose.StorageType;
import me.moonways.bridgenet.jdbc.core.compose.template.CreationTemplate;
import org.jetbrains.annotations.NotNull;

public class CreationTemplatedPattern extends AbstractPattern implements CreationTemplate {

    public CreationTemplatedPattern() {
        super(PatternCollections.fromPattern("creation.pattern"));
    }

    @Override
    public CreationTemplate entity(StorageType target) {
        PatternCollectionConfigurator.create("entity")
                .pushStringOnly(target.toString())
                .adjust(getTotals());
        return this;
    }

    @Override
    public CreationTemplate name(String value) {
        PatternCollectionConfigurator.create("name")
                .pushStringOnly(value)
                .adjust(getTotals());
        return this;
    }

    @Override
    public CreationTemplate signature(CompletedSignature signature) {
        PatternCollectionConfigurator.create("parameters")
                .pushSignatureOnly(signature)
                .adjust(getTotals());

        setSignatureSupportsEnabled();

        PatternCollectionConfigurator primaryKeysConfigurator = PatternCollectionConfigurator.create("primary");
        boolean hasPrimaryKeys = false;

        for (CombinedStructs.CombinedStyledParameter parameter : signature.parameters()) {
            if (parameter.getStyle().getAddons().contains(ParameterSignature.PRIMARY)) {
                hasPrimaryKeys = true;
                primaryKeysConfigurator.pushPrimaryKey(parameter);
            }
        }

        if (hasPrimaryKeys) {
            setPrimaryKeysSupportsEnabled();
            primaryKeysConfigurator.adjust(getTotals());
        }

        return this;
    }

    @Override
    public CreationTemplate encoding(Encoding encoding) {
        PatternCollectionConfigurator.create("encoding")
                .pushEncodingOnly(encoding)
                .adjust(getTotals());
        return this;
    }

    @Override
    public VerificationResult verify(@NotNull VerificationContext context) {
        return context.newTransaction(getTotals())
                //.markCollectionsTogether("has_signature", "parameters")
                .commitVerificationResult();
    }

    private void setSignatureSupportsEnabled() {
        PatternCollectionConfigurator.create("has_signature")
                .pushStringOnly("0")
                .adjust(getTotals());
    }

    private void setPrimaryKeysSupportsEnabled() {
        PatternCollectionConfigurator.create("has_primary")
                .pushStringOnly("0")
                .adjust(getTotals());
    }
}
