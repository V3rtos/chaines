package me.moonways.bridgenet.service.report;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public class Report {

    private final ReportReason reason;

    private final String whoReportedName;
    private final String intruderName;

    private final String comment;

    private final String whereServerName;

    private final long createdTimeMillis;
}
