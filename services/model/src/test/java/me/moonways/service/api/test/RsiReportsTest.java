package me.moonways.service.api.test;

import me.moonways.bridgenet.rsi.module.access.AccessConfig;
import me.moonways.bridgenet.rsi.module.access.AccessModule;
import me.moonways.bridgenet.rsi.service.ServiceInfo;
import me.moonways.model.parties.PartiesServiceModel;
import me.moonways.model.reports.ReportsServiceModel;
import me.moonways.model.reports.Report;
import me.moonways.model.reports.ReportReason;

import java.rmi.RemoteException;

public class RsiReportsTest {


    public static void main(String[] args) {
        ServiceInfo serviceInfo = new ServiceInfo("reports", 7009, PartiesServiceModel.class);

        AccessModule accessModule = new AccessModule();
        accessModule.init(serviceInfo, new AccessConfig("127.0.0.1"));

        ReportsServiceModel stub = accessModule.lookupStub();

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

