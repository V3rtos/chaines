package me.moonways.endpoint.reports;

import me.moonways.bridgenet.rsi.endpoint.AbstractEndpointDefinition;
import me.moonways.model.reports.ReportsServiceModel;
import me.moonways.model.reports.Report;
import me.moonways.model.reports.ReportReason;
import me.moonways.model.reports.ReportedPlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.rmi.RemoteException;
import java.util.*;
import java.util.stream.Collectors;

public final class ReportsServiceEndpoint extends AbstractEndpointDefinition implements ReportsServiceModel {

    private static final long serialVersionUID = 8862521493276490323L;

    private final List<ReportedPlayer> reportedPlayersList = new ArrayList<>();

    public ReportsServiceEndpoint() throws RemoteException {
        super();
    }

    private void validateNull(ReportStub report) {
        if (report == null) {
            throw new NullPointerException("report");
        }
    }

    private void validateNull(ReportReason reportReason) {
        if (reportReason == null) {
            throw new NullPointerException("report reason");
        }
    }

    private void validateNameNull(String name, String errorMsg) {
        if (name == null) {
            throw new NullPointerException(errorMsg);
        }
    }

    public ReportStub createReport(@NotNull ReportReason reason,
                                   @NotNull String whoReportedName,
                                   @NotNull String intruderName,
                                   @Nullable String comment,
                                   @NotNull String whereServerName) {
        validateNull(reason);

        validateNameNull(whoReportedName, "reporter name");
        validateNameNull(whoReportedName, "intruder name");
        validateNameNull(whoReportedName, "server name");

        try {
            return new ReportStub(reason, whoReportedName, intruderName, comment, whereServerName, System.currentTimeMillis());
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public ReportStub createReport(@NotNull ReportReason reason,
                                   @NotNull String whoReportedName,
                                   @NotNull String intruderName,
                                   @NotNull String whereServerName) {
        validateNull(reason);

        validateNameNull(whoReportedName, "reporter name");
        validateNameNull(whoReportedName, "intruder name");
        validateNameNull(whoReportedName, "server name");

        return createReport(reason, whoReportedName, intruderName, null, whereServerName);
    }

    public int getTotalReportedPlayersCount() {
        return reportedPlayersList.size();
    }

    public int getTotalReportsCount() {
        return (int) reportedPlayersList.stream().mapToLong(reportedPlayer -> reportedPlayer.getTotalReports().size()).sum();
    }

    public List<ReportedPlayer> getTotalReportedPlayers() {
        return reportedPlayersList;
    }

    public List<Report> getTotalReports() {
        return reportedPlayersList.stream().flatMap(reportedPlayer -> reportedPlayer.getTotalReports().stream()).collect(Collectors.toList());
    }

    public List<Report> getTotalReportsByIntruder(@NotNull String intruderName) {
        ReportedPlayer reportedPlayer = reportedPlayersList
                .stream()
                .filter(intruder -> intruder.getName().equals(intruderName))
                .findFirst()
                .orElse(null);

        if (reportedPlayer == null) {
            return Collections.emptyList();
        }

        return reportedPlayer.getTotalReports();
    }

    public ReportedPlayer getReportedPlayer(@NotNull ReportStub report) {
        validateNull(report);
        ReportedPlayer cached = reportedPlayersList
                .stream()
                .filter(reportedPlayer -> reportedPlayer.getName().equalsIgnoreCase(report.getIntruderName()))
                .findFirst()
                .orElse(null);

        if (cached == null) {

            cached = new ReportedPlayer(report.getIntruderName());
            reportedPlayersList.add(cached);
        }

        return cached;
    }

    public void registerReport(@NotNull ReportStub report) {
        validateNull(report);

        ReportedPlayer reportedPlayer = getReportedPlayer(report);
        reportedPlayer.addReport(report);
    }

    public void unregisterReport(@NotNull ReportStub report) {
        validateNull(report);

        ReportedPlayer reportedPlayer = getReportedPlayer(report);
        reportedPlayer.removeReport(report);

        if (reportedPlayer.getTotalReportsCount() <= 0) {
            reportedPlayersList.remove(reportedPlayer);
        }
    }

    @Nullable
    public Report getLastReportByReporter(@NotNull String reporterName) {
        validateNameNull(reporterName, "reporter name");

        List<Report> collect = getTotalReports()
                .stream()
                .filter(report -> {
                    try {
                        return report.getWhoReportedName().equalsIgnoreCase(reporterName);
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());

        if (collect.isEmpty()) {
            return null;
        }

        collect = new ArrayList<>(collect);
        Collections.reverse(collect);

        return collect.get(0);
    }

    @Nullable
    public Report getLastReportByReporterAndIntruder(@NotNull String reporterName, @NotNull String intruderName) {
        validateNameNull(reporterName, "reporter name");
        validateNameNull(intruderName, "intruder name");

        List<Report> collect = getTotalReports()
                .stream()
                .filter(report -> {
                    try {
                        return report.getIntruderName().equalsIgnoreCase(intruderName);
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                })
                .filter(report -> {
                    try {
                        return report.getWhoReportedName().equalsIgnoreCase(reporterName);
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());

        if (collect.isEmpty()) {
            return null;
        }

        collect = new ArrayList<>(collect);
        Collections.reverse(collect);

        return collect.get(0);
    }
}
