package me.moonways.bridgenet.jdbc.core.compose;

import me.moonways.bridgenet.jdbc.core.compose.template.*;
import me.moonways.bridgenet.jdbc.core.compose.template.collection.*;

public interface DatabaseComposer {

    RestorationTemplate useRestorationPattern();

    CreationTemplate useCreationPattern();

    EjectionTemplate useEjectionPattern();

    DeletionTemplate useDeletionPattern();

    SearchTemplate useSearchPattern();

    InsertionTemplate useInsertionPattern();

    MergesTemplate merges();

    GroupsTemplate groups();

    SubjectsTemplate subjects();

    SignatureTemplate signature();

    PredicatesTemplate predicates();
}
