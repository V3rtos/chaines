package me.moonways.service.api.reports;

import me.moonways.bridgenet.rsi.service.RemoteService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.rmi.RemoteException;

public interface BridgenetReportsService extends RemoteService {

    Report createReport(@NotNull ReportReason reason,
                        @NotNull String whoReportedName,
                        @NotNull String intruderName,
                        @Nullable String comment,
                        @NotNull String whereServerName) throws RemoteException;

    Report createReport(@NotNull ReportReason reason,
                        @NotNull String whoReportedName,
                        @NotNull String intruderName,
                        @NotNull String whereServerName) throws RemoteException;

    int getTotalReportedPlayersCount() throws RemoteException;

    int getTotalReportsCount() throws RemoteException;
}
