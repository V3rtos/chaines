package me.moonways.bridgenet.service.report;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import me.moonways.service.api.reports.Report;
import me.moonways.service.api.reports.ReportReason;

@ToString
@Getter
@EqualsAndHashCode
@RequiredArgsConstructor
public class ReportImpl implements Report {

    private final ReportReason reason;

    private final String whoReportedName;
    private final String intruderName;

    private final String comment;

    private final String serverName;

    private final long createdTimeMillis;
}
