package me.moonways.service.api.test;

import me.moonways.bridgenet.rsi.module.access.AccessConfig;
import me.moonways.bridgenet.rsi.module.access.AccessModule;
import me.moonways.bridgenet.rsi.service.ServiceInfo;
import me.moonways.service.api.parties.BridgenetPartiesService;
import me.moonways.service.api.reports.BridgenetReportsService;
import me.moonways.service.api.reports.Report;
import me.moonways.service.api.reports.ReportReason;

import java.rmi.RemoteException;

public class RsiReportTest {


    public static void main(String[] args) {
        ServiceInfo serviceInfo = new ServiceInfo("reports", 7009, BridgenetPartiesService.class);

        AccessModule accessModule = new AccessModule();
        accessModule.init(serviceInfo, new AccessConfig("127.0.0.1"));

        BridgenetReportsService stub = accessModule.lookupStub();

        try {
            Report report = stub.createReport(
                    ReportReason.CHEATING,
                    "GitCoder", "xxcoldinme","читерил на хайпе", "Hypixel");

            System.out.println(report.getComment());
        } catch (RemoteException exception) {
            exception.printStackTrace();
        }
    }
}

