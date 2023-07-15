package me.moonways.bridgenet.service.report;

import me.moonways.bridgenet.injection.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

@Component
public final class ReportsService {

    private final List<ReportedPlayer> reportedPlayersList = Collections.synchronizedList(new ArrayList<>());

    private void validateNull(Report report) {
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

    public Report createReport(@NotNull ReportReason reason,
                               @NotNull String whoReportedName,
                               @NotNull String intruderName,
                               @Nullable String comment,
                               @NotNull String whereServerName) {
        validateNull(reason);

        validateNameNull(whoReportedName, "reporter name");
        validateNameNull(whoReportedName, "intruder name");
        validateNameNull(whoReportedName, "server name");

        return new Report(reason, whoReportedName, intruderName, comment, whereServerName, System.currentTimeMillis());
    }

    public Report createReport(@NotNull ReportReason reason,
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

    public ReportedPlayer getReportedPlayer(@NotNull Report report) {
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

    public void registerReport(@NotNull Report report) {
        validateNull(report);

        ReportedPlayer reportedPlayer = getReportedPlayer(report);
        reportedPlayer.addReport(report);
    }

    public void unregisterReport(@NotNull Report report) {
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
    public Report getLastReportByReporterAndIntruder(@NotNull String reporterName, @NotNull String intruderName) {
        validateNameNull(reporterName, "reporter name");
        validateNameNull(intruderName, "intruder name");

        List<Report> collect = getTotalReports()
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
