package me.moonways.endpoint.auth;

import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.jdbc.core.DatabaseConnection;
import me.moonways.bridgenet.jdbc.provider.DatabaseProvider;
import me.moonways.bridgenet.model.service.auth.Account;
import me.moonways.bridgenet.model.service.auth.AuthServiceModel;
import me.moonways.bridgenet.model.service.auth.AuthorizationResult;
import me.moonways.bridgenet.rmi.endpoint.persistance.EndpointRemoteContext;
import me.moonways.bridgenet.rmi.endpoint.persistance.EndpointRemoteObject;

import java.rmi.RemoteException;
import java.util.Optional;
import java.util.UUID;

public class AuthServiceEndpoint extends EndpointRemoteObject implements AuthServiceModel {

    private static final long serialVersionUID = -1339569131139297348L;

    @Inject
    private DatabaseConnection databaseConnection;
    @Inject
    private DatabaseProvider databaseProvider;

    public AuthServiceEndpoint() throws RemoteException {
        super();
    }

    @Override
    protected void construct(EndpointRemoteContext context) {
        // todo
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
