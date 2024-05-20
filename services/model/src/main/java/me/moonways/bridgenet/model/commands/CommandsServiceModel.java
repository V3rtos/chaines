package me.moonways.bridgenet.model.commands;

import me.moonways.bridgenet.rsi.service.RemoteService;

import java.rmi.RemoteException;
import java.util.function.Consumer;

public interface CommandsServiceModel extends RemoteService {

    void register(Command command) throws RemoteException;

    void setCommandExecutor(CommandExecutor commandExecutor) throws RemoteException;

    void setRegistrationExecutor(RegistrationExecutor registrationExecutor) throws RemoteException;
}
