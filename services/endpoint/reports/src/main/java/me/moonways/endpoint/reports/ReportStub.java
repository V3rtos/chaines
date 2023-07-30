package me.moonways.endpoint.reports;

import lombok.Getter;
import lombok.ToString;
import me.moonways.bridgenet.rsi.endpoint.AbstractEndpointDefinition;
import me.moonways.model.reports.Report;
import me.moonways.model.reports.ReportReason;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.rmi.RemoteException;

@ToString
@Getter
public class ReportStub extends AbstractEndpointDefinition implements Report {

    private static final long serialVersionUID = -3931465978117678586L;

    private final ReportReason reason;

    private final String whoReportedName;
    private final String intruderName;

    private final String comment;

    private final String serverName;

    private final long createdTimeMillis;

    public ReportStub(@NotNull ReportReason reason,
                      @NotNull String whoReportedName,
                      @NotNull String intruderName,
                      @Nullable String comment,
                      @NotNull String serverName,
                      long createdTimeMillis) throws RemoteException {

        this.reason = reason;
        this.whoReportedName = whoReportedName;
        this.intruderName = intruderName;
        this.comment = comment;
        this.serverName = serverName;
        this.createdTimeMillis = createdTimeMillis;
    }
}
