package me.moonways.bridgenet.model.reports;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public class ReportedPlayer implements Serializable {

    private static final long serialVersionUID = 3816836769485556160L;

    private final List<Report> reportsList = Collections.synchronizedList(new ArrayList<>());

    @Getter
    private final String name;

    private void validateNull(Report report) {
        if (report == null) {
            throw new NullPointerException("report");
        }
    }

    public void addReport(@NotNull Report report) {
        validateNull(report);
        reportsList.add(report);
    }

    public void removeReport(@NotNull Report report) {
        validateNull(report);
        reportsList.remove(report);
    }

    public boolean hasReport(@NotNull Report report) {
        validateNull(report);
        return reportsList.contains(report);
    }

    public int getTotalReportsCount() {
        return reportsList.size();
    }

    public List<Report> getTotalReports() {
        return Collections.unmodifiableList(reportsList);
    }
}
