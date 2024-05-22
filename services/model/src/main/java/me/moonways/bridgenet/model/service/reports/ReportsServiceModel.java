package me.moonways.bridgenet.model.service.reports;

import me.moonways.bridgenet.rmi.service.RemoteService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.rmi.RemoteException;
import java.util.List;

public interface ReportsServiceModel extends RemoteService {

    Report createReport(@NotNull ReportReason reason,
                        @NotNull String whoReportedName,
                        @NotNull String intruderName,
                        @Nullable String comment,
                        @NotNull String whereServerName) throws RemoteException;

    Report createReport(@NotNull ReportReason reason,
                        @NotNull String whoReportedName,
                        @NotNull String intruderName,
                        @NotNull String whereServerName) throws RemoteException;

    List<Report> getTotalReports() throws RemoteException;

    List<ReportedPlayer> getTotalReportedPlayers() throws RemoteException;

    int getTotalReportedPlayersCount() throws RemoteException;

    int getTotalReportsCount() throws RemoteException;
}
