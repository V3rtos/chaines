package me.moonways.endpoint.auth;

import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.api.inject.PostConstruct;
import me.moonways.bridgenet.api.inject.bean.service.BeansService;
import me.moonways.bridgenet.jdbc.core.DatabaseConnection;
import me.moonways.bridgenet.jdbc.provider.DatabaseProvider;
import me.moonways.bridgenet.model.auth.Account;
import me.moonways.bridgenet.model.auth.AuthServiceModel;
import me.moonways.bridgenet.model.auth.AuthorizationResult;
import me.moonways.bridgenet.rsi.endpoint.AbstractEndpointDefinition;

import java.rmi.RemoteException;
import java.util.Optional;
import java.util.UUID;

public class AuthServiceEndpoint extends AbstractEndpointDefinition implements AuthServiceModel {

    private static final long serialVersionUID = -1339569131139297348L;

    @Inject
    private BeansService beansService;
    @Inject
    private DatabaseConnection databaseConnection;
    @Inject
    private DatabaseProvider databaseProvider;

    public AuthServiceEndpoint() throws RemoteException {
        super();
    }

    @PostConstruct
    private void init() {
        //todo
    }

    @Override
    public Optional<Account> findAccountById(UUID playerId) throws RemoteException {
        return Optional.empty(); //todo
    }

    @Override
    public AuthorizationResult tryLogin(UUID playerId, String inputPassword) throws RemoteException {
        return AuthorizationResult.FAILURE; //todo
    }

    @Override
    public AuthorizationResult tryRegistration(UUID playerId, String inputPassword) throws RemoteException {
        return AuthorizationResult.FAILURE; //todo
    }

    @Override
    public AuthorizationResult tryPasswordChange(UUID playerId, String actualPassword, String newPassword) throws RemoteException {
        return AuthorizationResult.FAILURE; //todo
    }

    @Override
    public AuthorizationResult tryLogOut(UUID playerId) throws RemoteException {
        return AuthorizationResult.FAILURE; //todo
    }

    @Override
    public AuthorizationResult tryAccountDelete(UUID playerId) throws RemoteException {
        return AuthorizationResult.FAILURE; //todo
    }
}
