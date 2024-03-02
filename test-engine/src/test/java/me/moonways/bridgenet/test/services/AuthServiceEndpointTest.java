package me.moonways.bridgenet.test.services;

import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.model.auth.Account;
import me.moonways.bridgenet.model.auth.AuthServiceModel;
import me.moonways.bridgenet.model.auth.AuthenticationSession;
import me.moonways.bridgenet.model.auth.AuthorizationResult;
import me.moonways.bridgenet.test.engine.BridgenetJUnitTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.rmi.RemoteException;
import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.*;

@RunWith(BridgenetJUnitTestRunner.class)
public class AuthServiceEndpointTest {

    private static final UUID PLAYER_ID = UUID.randomUUID();
    private static final String PLAYER_PASSWORD = "123qweasdzxc";

    @Inject
    private AuthServiceModel serviceModel;

    @Test
    public void test_accountRegistration() throws RemoteException {
        assertFalse(serviceModel.findAccountById(PLAYER_ID).isPresent());

        AuthorizationResult resultSuccess = serviceModel.tryRegistration(PLAYER_ID, PLAYER_PASSWORD);
        AuthorizationResult resultAlreadyRegistered = serviceModel.tryRegistration(PLAYER_ID, PLAYER_PASSWORD);

        assertTrue(serviceModel.findAccountById(PLAYER_ID).isPresent());
        assertEquals(resultSuccess, AuthorizationResult.SUCCESS);
        assertEquals(resultAlreadyRegistered, AuthorizationResult.FAILURE__ALREADY_REGISTERED);
    }

    @Test
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
    public void test_accountLogout() throws RemoteException {
        assertTrue(serviceModel.findAccountById(PLAYER_ID).isPresent());

        AuthorizationResult resultSuccess = serviceModel.tryLogOut(PLAYER_ID);
        AuthorizationResult resultNotLogged = serviceModel.tryLogOut(PLAYER_ID);

        assertEquals(resultSuccess, AuthorizationResult.SUCCESS);
        assertEquals(resultNotLogged, AuthorizationResult.FAILURE__NOT_LOGGED);
    }

    @Test
    public void test_accountLogging() throws RemoteException {
        assertTrue(serviceModel.findAccountById(PLAYER_ID).isPresent());

        AuthorizationResult resultSuccess = serviceModel.tryLogin(PLAYER_ID, PLAYER_PASSWORD);
        AuthorizationResult resultAlreadyLogged = serviceModel.tryLogin(PLAYER_ID, PLAYER_PASSWORD);

        assertEquals(resultSuccess, AuthorizationResult.SUCCESS);
        assertEquals(resultAlreadyLogged, AuthorizationResult.FAILURE__ALREADY_LOGGED);
    }

    @Test
    public void test_accountPasswordChange() throws RemoteException {
        final String newPassword = "cxzdsaewq321";

        assertTrue(serviceModel.findAccountById(PLAYER_ID).isPresent());

        AuthorizationResult resultUncorrectedPassword = serviceModel.tryPasswordChange(PLAYER_ID, newPassword, PLAYER_PASSWORD);
        AuthorizationResult resultSimilarPreviousPassword = serviceModel.tryPasswordChange(PLAYER_ID, PLAYER_PASSWORD, PLAYER_PASSWORD);
        AuthorizationResult resultSuccess = serviceModel.tryPasswordChange(PLAYER_ID, PLAYER_PASSWORD, newPassword);

        assertEquals(resultUncorrectedPassword, AuthorizationResult.FAILURE__UNCORRECTED_PASSWORD);
        assertEquals(resultSimilarPreviousPassword, AuthorizationResult.FAILURE__SIMILAR_PREVIOUS_PASSWORDS);
        assertEquals(resultSuccess, AuthorizationResult.SUCCESS);
    }

    @Test
    public void test_accountDelete() throws RemoteException {
        assertTrue(serviceModel.findAccountById(PLAYER_ID).isPresent());

        AuthorizationResult resultSuccess = serviceModel.tryAccountDelete(PLAYER_ID);
        AuthorizationResult resultNotFound = serviceModel.tryAccountDelete(PLAYER_ID);

        assertEquals(resultSuccess, AuthorizationResult.SUCCESS);
        assertEquals(resultNotFound, AuthorizationResult.FAILURE__ACCOUNT_NOT_FOUND);
    }
}
