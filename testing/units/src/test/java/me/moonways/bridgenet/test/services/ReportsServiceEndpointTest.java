package me.moonways.bridgenet.test.services;

import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.model.service.reports.Report;
import me.moonways.bridgenet.model.service.reports.ReportReason;
import me.moonways.bridgenet.model.service.reports.ReportsServiceModel;
import me.moonways.bridgenet.test.data.TestConst;
import me.moonways.bridgenet.test.engine.ModernTestEngineRunner;
import me.moonways.bridgenet.test.engine.component.module.impl.RmiServicesModule;
import me.moonways.bridgenet.test.engine.persistance.TestModules;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.rmi.RemoteException;

import static org.junit.Assert.assertEquals;

@RunWith(ModernTestEngineRunner.class)
@TestModules(RmiServicesModule.class)
public class ReportsServiceEndpointTest {

    @Inject
    private ReportsServiceModel serviceModel;

    @Test
    public void test_reportCreate() throws RemoteException {
        Report report = serviceModel.createReport(
                ReportReason.CHEATING,
                TestConst.Report.REPORTER,
                TestConst.Report.INTRUDER,
                TestConst.Report.COMMENT,
                TestConst.Report.SOURCE);

        assertEquals(TestConst.Report.REPORTER, report.getWhoReportedName());
        assertEquals(TestConst.Report.COMMENT, report.getComment());

        assertEquals(1, serviceModel.getTotalReportedPlayersCount());
    }
}

