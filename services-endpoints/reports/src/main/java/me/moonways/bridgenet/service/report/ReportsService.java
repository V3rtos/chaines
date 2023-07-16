package me.moonways.bridgenet.service.report;

import me.moonways.bridgenet.injection.Component;
import me.moonways.service.api.reports.ReportReason;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

@Component
public final class ReportsService {

    private final List<ReportedPlayer> reportedPlayersList = Collections.synchronizedList(new ArrayList<>());

    private void validateNull(ReportImpl report) {
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

    public ReportImpl createReport(@NotNull ReportReason reason,
                                   @NotNull String whoReportedName,
                                   @NotNull String intruderName,
                                   @Nullable String comment,
                                   @NotNull String whereServerName) {
        validateNull(reason);

        validateNameNull(whoReportedName, "reporter name");
        validateNameNull(whoReportedName, "intruder name");
        validateNameNull(whoReportedName, "server name");

        return new ReportImpl(reason, whoReportedName, intruderName, comment, whereServerName, System.currentTimeMillis());
    }

    public ReportImpl createReport(@NotNull ReportReason reason,
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

    public List<ReportImpl> getTotalReports() {
        return reportedPlayersList.stream().flatMap(reportedPlayer -> reportedPlayer.getTotalReports().stream()).collect(Collectors.toList());
    }

    public List<ReportImpl> getTotalReportsByIntruder(@NotNull String intruderName) {
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

    public ReportedPlayer getReportedPlayer(@NotNull ReportImpl report) {
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

    public void registerReport(@NotNull ReportImpl report) {
        validateNull(report);

        ReportedPlayer reportedPlayer = getReportedPlayer(report);
        reportedPlayer.addReport(report);
    }

    public void unregisterReport(@NotNull ReportImpl report) {
        validateNull(report);

        ReportedPlayer reportedPlayer = getReportedPlayer(report);
        reportedPlayer.removeReport(report);

        if (reportedPlayer.getTotalReportsCount() <= 0) {
            reportedPlayersList.remove(reportedPlayer);
        }
    }

    @Nullable
    public ReportImpl getLastReportByReporter(@NotNull String reporterName) {
        validateNameNull(reporterName, "reporter name");

        List<ReportImpl> collect = getTotalReports()
                .stream()
                .filter(report -> report.getWhoReportedName().equalsIgnoreCase(reporterName))
                .collect(Collectors.toList());

        if (collect.isEmpty()) {
            return null;
        }

        collect = new ArrayList<>(collect);
        Collections.reverse(collect);

        return collect.get(0);
    }

    @Nullable
    public ReportImpl getLastReportByReporterAndIntruder(@NotNull String reporterName, @NotNull String intruderName) {
        validateNameNull(reporterName, "reporter name");
        validateNameNull(intruderName, "intruder name");

        List<ReportImpl> collect = getTotalReports()
                .stream()
                .filter(report -> report.getIntruderName().equalsIgnoreCase(intruderName))
                .filter(report -> report.getWhoReportedName().equalsIgnoreCase(reporterName))
                .collect(Collectors.toList());

        if (collect.isEmpty()) {
            return null;
        }

        collect = new ArrayList<>(collect);
        Collections.reverse(collect);

        return collect.get(0);
    }
}
