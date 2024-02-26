package me.moonways.bridgenet.test.services;

import me.moonways.bridgenet.rsi.module.access.AccessConfig;
import me.moonways.bridgenet.rsi.module.access.AccessRemoteModule;
import me.moonways.bridgenet.rsi.service.ServiceInfo;
import me.moonways.bridgenet.test.engine.BridgenetJUnitTestRunner;
import me.moonways.bridgenet.model.parties.PartiesServiceModel;
import me.moonways.bridgenet.model.reports.ReportsServiceModel;
import me.moonways.bridgenet.model.reports.Report;
import me.moonways.bridgenet.model.reports.ReportReason;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.rmi.RemoteException;

import static org.junit.Assert.assertEquals;

@RunWith(BridgenetJUnitTestRunner.class)
public class ReportsServiceConnectTest {

    private AccessRemoteModule subj;

    @Before
    public void setUp() {
        ServiceInfo serviceInfo = new ServiceInfo("reports", 7009, PartiesServiceModel.class);

        subj = new AccessRemoteModule();
        subj.init(serviceInfo, new AccessConfig("127.0.0.1"));
    }

    @Test
    public void test_success() throws RemoteException {
        ReportsServiceModel stub = subj.lookupStub();

        Report report = stub.createReport(
                ReportReason.CHEATING, "GitCoder", "xxcoldinme",
                "читерил на хайпе", "Hypixel");

        assertEquals("GitCoder", report.getWhoReportedName());
        assertEquals("читерил на хайпе", report.getComment());

        assertEquals(1, stub.getTotalReportedPlayersCount());
    }
}

