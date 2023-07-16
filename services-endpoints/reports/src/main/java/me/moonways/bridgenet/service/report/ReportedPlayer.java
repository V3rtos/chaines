package me.moonways.bridgenet.service.report;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public class ReportedPlayer {

    private final List<ReportImpl> reportsList = Collections.synchronizedList(new ArrayList<>());

    @Getter
    private final String name;

    private void validateNull(ReportImpl report) {
        if (report == null) {
            throw new NullPointerException("report");
        }
    }

    public void addReport(@NotNull ReportImpl report) {
        validateNull(report);
        reportsList.add(report);
    }

    public void removeReport(@NotNull ReportImpl report) {
        validateNull(report);
        reportsList.remove(report);
    }

    public boolean hasReport(@NotNull ReportImpl report) {
        validateNull(report);
        return reportsList.contains(report);
    }

    public int getTotalReportsCount() {
        return reportsList.size();
    }

    public List<ReportImpl> getTotalReports() {
        return Collections.unmodifiableList(reportsList);
    }
}
