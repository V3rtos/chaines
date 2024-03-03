package me.moonways.bridgenet.test.services;

import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.test.engine.BridgenetJUnitTestRunner;
import me.moonways.bridgenet.model.reports.ReportsServiceModel;
import me.moonways.bridgenet.model.reports.Report;
import me.moonways.bridgenet.model.reports.ReportReason;
import me.moonways.bridgenet.test.engine.persistance.Order;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.rmi.RemoteException;

import static org.junit.Assert.assertEquals;

@RunWith(BridgenetJUnitTestRunner.class)
public class ReportsServiceEndpointTest {

    @Inject
    private ReportsServiceModel serviceModel;

    @Test
    @Order(0)
    public void test_reportCreate() throws RemoteException {
        Report report = serviceModel.createReport(
                ReportReason.CHEATING, "GitCoder", "xxcoldinme",
                "читерил на хайпе", "Hypixel");

        assertEquals("GitCoder", report.getWhoReportedName());
        assertEquals("читерил на хайпе", report.getComment());

        assertEquals(1, serviceModel.getTotalReportedPlayersCount());
    }
}

