package me.moonways.bridgenet.test.jdbc.transaction;

import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.jdbc.core.DatabaseConnection;
import me.moonways.bridgenet.jdbc.core.TransactionIsolation;
import me.moonways.bridgenet.jdbc.core.compose.*;
import me.moonways.bridgenet.jdbc.core.transaction.FailedTransactionPreprocessConsumer;
import me.moonways.bridgenet.jdbc.core.transaction.TransactionResult;
import me.moonways.bridgenet.jdbc.provider.DatabaseProvider;
import me.moonways.bridgenet.test.engine.BridgenetJUnitTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;

@RunWith(BridgenetJUnitTestRunner.class)
public class TransactionTest {

    @Inject
    private DatabaseProvider provider;
    @Inject
    private DatabaseConnection connection;
    @Inject
    private DatabaseComposer composer;

    @Test
    public void test_callAsTransaction() {
        TransactionResult transactionResult = provider.openTransaction()
                .isolation(TransactionIsolation.NONE)
                .onFailed(FailedTransactionPreprocessConsumer.create()
                        .onExceptionThrow(Throwable::printStackTrace)
                        .postprocess(result -> System.out.println("proceed requests count: " + result.getProceedQueriesCount())))
                .query(provider.openTransactionQuery()
                        .marksSavepoint()
                        .writeSql("CREATE SCHEMA core"))
                .query(provider.openTransactionQuery()
                        .intermediateIsolation(TransactionIsolation.SERIALIZABLE)
                        .write(composer.useCreationPattern()
                                .name("Players")
                                .entity(StorageType.CONTAINER)
                                .signature(composer.signature()
                                        .with(CombinedStructs.styledParameter("ID",
                                                ParameterStyle.builder()
                                                        .type(ParameterType.BIGINT)
                                                        .addons(Arrays.asList(
                                                                ParameterAddon.PRIMARY,
                                                                ParameterAddon.INCREMENTING,
                                                                ParameterAddon.NOTNULL
                                                        ))
                                                        .build()))
                                        .with(CombinedStructs.styledParameter("NAME",
                                                ParameterStyle.builder()
                                                        .type(ParameterType.STRING)
                                                        .addons(Arrays.asList(
                                                                ParameterAddon.PRIMARY,
                                                                ParameterAddon.NOTNULL
                                                        ))
                                                        .build()))
                                        .combine())
                                .combine()))
                .combine()
                .call(connection);

        System.out.println(transactionResult);
    }
}
