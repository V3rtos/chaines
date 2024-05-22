package me.moonways.bridgenet.model.event;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import me.moonways.bridgenet.api.event.Event;
import me.moonways.bridgenet.model.service.reports.Report;

@Getter
@Builder
@ToString
@EqualsAndHashCode
public class ReportCreateEvent implements Event {

    private final Report report;
}
