package me.moonways.bridgenet.jdbc.core.compose.impl.pattern.type.query;

import me.moonways.bridgenet.jdbc.core.compose.impl.pattern.AbstractPattern;
import me.moonways.bridgenet.jdbc.core.compose.impl.pattern.PatternCollectionConfigurator;
import me.moonways.bridgenet.jdbc.core.compose.impl.pattern.PatternCollections;
import me.moonways.bridgenet.jdbc.core.compose.impl.pattern.verification.VerificationContext;
import me.moonways.bridgenet.jdbc.core.compose.impl.pattern.verification.VerificationResult;
import me.moonways.bridgenet.jdbc.core.compose.CombinedStructs;
import me.moonways.bridgenet.jdbc.core.compose.template.InsertionTemplate;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class InsertionTemplatedPattern extends AbstractPattern implements InsertionTemplate {

    private final List<CombinedStructs.CombinedField> values = new ArrayList<>();
    private final List<CombinedStructs.CombinedField> conflictValues = new ArrayList<>();

    public InsertionTemplatedPattern() {
        super(PatternCollections.fromPattern("insertion.pattern"));
    }

    @Override
    public InsertionTemplate container(String table) {
        PatternCollectionConfigurator.create("container")
                .pushStringOnly(table)
                .adjust(getTotals());
        return this;
    }

    @Override
    public InsertionTemplate withValue(CombinedStructs.CombinedField field) {
        values.add(field);
        return this;
    }

    @Override
    public InsertionTemplate updateOnConflict(CombinedStructs.CombinedField field) {
        conflictValues.add(field);
        return this;
    }

    @Override
    public InsertionTemplate useDuplicationReduce() {
        PatternCollectionConfigurator.create("has_reduce_duplication")
                .pushStringOnly("0")
                .adjust(getTotals());
        return this;
    }

    @Override
    protected void onPreprocess() {
        PatternCollectionConfigurator.create("values")
                .pushValuesOnly(values.toArray(new CombinedStructs.CombinedField[0]))
                .adjust(getTotals());

        PatternCollectionConfigurator.create("values_duplication")
                .pushValuesOnly(conflictValues.toArray(new CombinedStructs.CombinedField[0]))
                .adjust(getTotals());
    }

    @Override
    public VerificationResult verify(@NotNull VerificationContext context) {
        return context.newTransaction(getTotals())
                .markCollectionsTogether("has_reduce_duplication", "values_duplication")
                .commitVerificationResult();
    }
}
