package me.moonways.bridgenet.jdbc.core.compose.impl;

import me.moonways.bridgenet.jdbc.core.compose.impl.pattern.type.collection.*;
import me.moonways.bridgenet.jdbc.core.compose.impl.pattern.type.query.*;
import me.moonways.bridgenet.jdbc.core.compose.template.*;
import me.moonways.bridgenet.jdbc.core.compose.template.collection.*;
import me.moonways.bridgenet.jdbc.core.compose.DatabaseComposer;

public class PatternDatabaseComposerImpl implements DatabaseComposer {

    @Override
    public RestorationTemplate useRestorationPattern() {
        return new RestorationTemplatedPattern();
    }

    @Override
    public CreationTemplate useCreationPattern() {
        return new CreationTemplatedPattern();
    }

    @Override
    public EjectionTemplate useEjectionPattern() {
        return new EjectionTemplatedPattern();
    }

    @Override
    public DeletionTemplate useDeletionPattern() {
        return new DeletionTemplatedPattern();
    }

    @Override
    public SearchTemplate useSearchPattern() {
        return new SearchTemplatedPattern();
    }

    @Override
    public InsertionTemplate useInsertionPattern() {
        return new InsertionTemplatedPattern();
    }

    @Override
    public MergesTemplate merges() {
        return new MergesPattern();
    }

    @Override
    public GroupsTemplate groups() {
        return new GroupsPattern();
    }

    @Override
    public SubjectsTemplate subjects() {
        return new SubjectsPattern();
    }

    @Override
    public SignatureTemplate signature() {
        return new SignaturePattern();
    }

    @Override
    public PredicatesTemplate predicates() {
        return new PredicatesPattern();
    }
}
