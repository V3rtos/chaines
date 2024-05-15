package me.moonways.bridgenet.test.connections.services;

import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.model.auth.Account;
import me.moonways.bridgenet.model.auth.AuthServiceModel;
import me.moonways.bridgenet.model.auth.AuthenticationSession;
import me.moonways.bridgenet.model.auth.AuthorizationResult;
import me.moonways.bridgenet.test.engine.ModernTestEngineRunner;
import me.moonways.bridgenet.test.engine.module.impl.RmiServicesModule;
import me.moonways.bridgenet.test.engine.persistance.TestModules;
import me.moonways.bridgenet.test.engine.persistance.TestOrdered;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.rmi.RemoteException;
import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.*;

@RunWith(ModernTestEngineRunner.class)
@TestModules(RmiServicesModule.class)
public class AuthServiceEndpointTest {

    private static final String ACTUAL_PASSWORD = "123qweasdzxc";
    private static final String NEW_PASSWORD = "cxzdsaewq321";

    private static final UUID PLAYER_ID = UUID.randomUUID();

    @Inject
    private AuthServiceModel serviceModel;

    @Test
    @TestOrdered(0)
    public void test_accountRegistration() throws RemoteException {
        assertFalse(serviceModel.findAccountById(PLAYER_ID).isPresent());

        AuthorizationResult resultSuccess = serviceModel.tryRegistration(PLAYER_ID, ACTUAL_PASSWORD);
        AuthorizationResult resultAlreadyRegistered = serviceModel.tryRegistration(PLAYER_ID, ACTUAL_PASSWORD);

        assertTrue(serviceModel.findAccountById(PLAYER_ID).isPresent());
        assertEquals(resultSuccess, AuthorizationResult.SUCCESS);
        assertEquals(resultAlreadyRegistered, AuthorizationResult.FAILURE__ALREADY_REGISTERED);
    }

    @Test
    @TestOrdered(1)
    public void test_accountSession() throws RemoteException {
        Optional<Account> accountById = serviceModel.findAccountById(PLAYER_ID);
        assertTrue(accountById.isPresent());

        Account account = accountById.get();

        AuthenticationSession session = account.getSession();

        assertNotNull(session.getLastAuthenticationDate());
        assertNotNull(session.getLastAuthenticationIp());
        assertFalse(session.isExpired());
        assertTrue(session.isActive());
        assertFalse(session.isInactive());
    }

    @Test
    @TestOrdered(2)
    public void test_accountLogout() throws RemoteException {
        assertTrue(serviceModel.findAccountById(PLAYER_ID).isPresent());

        AuthorizationResult resultSuccess = serviceModel.tryLogOut(PLAYER_ID);
        AuthorizationResult resultNotLogged = serviceModel.tryLogOut(PLAYER_ID);

        assertEquals(resultSuccess, AuthorizationResult.SUCCESS);
        assertEquals(resultNotLogged, AuthorizationResult.FAILURE__NOT_LOGGED);
    }

    @Test
    @TestOrdered(3)
    public void test_accountLogging() throws RemoteException {
        assertTrue(serviceModel.findAccountById(PLAYER_ID).isPresent());

        AuthorizationResult resultSuccess = serviceModel.tryLogin(PLAYER_ID, ACTUAL_PASSWORD);
        AuthorizationResult resultAlreadyLogged = serviceModel.tryLogin(PLAYER_ID, ACTUAL_PASSWORD);

        assertEquals(resultSuccess, AuthorizationResult.SUCCESS);
        assertEquals(resultAlreadyLogged, AuthorizationResult.FAILURE__ALREADY_LOGGED);
    }

    @Test
    @TestOrdered(4)
    public void test_accountPasswordChange() throws RemoteException {
        assertTrue(serviceModel.findAccountById(PLAYER_ID).isPresent());

        AuthorizationResult resultUncorrectedPassword = serviceModel.tryPasswordChange(PLAYER_ID, NEW_PASSWORD, ACTUAL_PASSWORD);
        AuthorizationResult resultSimilarPreviousPassword = serviceModel.tryPasswordChange(PLAYER_ID, ACTUAL_PASSWORD, ACTUAL_PASSWORD);
        AuthorizationResult resultSuccess = serviceModel.tryPasswordChange(PLAYER_ID, ACTUAL_PASSWORD, NEW_PASSWORD);

        assertEquals(resultUncorrectedPassword, AuthorizationResult.FAILURE__UNCORRECTED_PASSWORD);
        assertEquals(resultSimilarPreviousPassword, AuthorizationResult.FAILURE__SIMILAR_PREVIOUS_PASSWORDS);
        assertEquals(resultSuccess, AuthorizationResult.SUCCESS);
    }

    @Test
    @TestOrdered(5)
    public void test_accountDelete() throws RemoteException {
        assertTrue(serviceModel.findAccountById(PLAYER_ID).isPresent());

        AuthorizationResult resultSuccess = serviceModel.tryAccountDelete(PLAYER_ID);
        AuthorizationResult resultNotFound = serviceModel.tryAccountDelete(PLAYER_ID);

        assertEquals(resultSuccess, AuthorizationResult.SUCCESS);
        assertEquals(resultNotFound, AuthorizationResult.FAILURE__ACCOUNT_NOT_FOUND);
    }
}
