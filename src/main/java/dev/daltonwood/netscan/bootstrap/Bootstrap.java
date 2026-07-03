package dev.daltonwood.netscan.bootstrap;

import dev.daltonwood.netscan.entity.Scan;
import dev.daltonwood.netscan.entity.ScanResult;
import dev.daltonwood.netscan.network.SubnetService;
import dev.daltonwood.netscan.repository.ScanResultRepo;
import dev.daltonwood.netscan.service.ScanService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class Bootstrap implements CommandLineRunner {

    private final ScanService scanService;
    private final Scanner scanner = new Scanner(System.in);
    private final SubnetService subnetService;
    private final ScanResultRepo scanResultRepo;

    public Bootstrap(ScanService scanService, SubnetService subnetService, ScanResultRepo scanResultRepo) {
        this.scanService = scanService;
        this.subnetService = subnetService;
        this.scanResultRepo = scanResultRepo;
    }

    @Override
    public void run(String... args) {

        System.out.println("NetScan started...");

//        TODO: Uncomment to prompt actual user input
        Scan initialScan = null;

        System.out.print("Input CIDR value:");
        String userInput = scanner.nextLine();

        while (initialScan == null) {
            try {
                initialScan = scanService.createScan(userInput);
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
                System.out.print("Input CIDR value:");
                userInput = scanner.nextLine();
            }
        }

//        TODO: Remove hardcoded scan
//        Scan initialScan = scanService.createScan("10.12.12.0/24");

        System.out.println(scanService.getScanInfo(initialScan.getTargetSubnet().getCidrValue()));
        System.out.println("CIDR validated...");
        System.out.println("Scan status: " + initialScan.getStatus());

        System.out.println("Continue with scan? \nY or N");
        String consent = scanner.nextLine();

        while (!consent.equalsIgnoreCase("y") && !consent.equalsIgnoreCase("n")) {
            System.out.println("Start scan? \nY or N");
            consent = scanner.nextLine();
        }

        if (consent.equalsIgnoreCase("y")) {

            scanService.startScan(initialScan);

        } else if (consent.equalsIgnoreCase("n")) {

            System.out.println("Scan status: " + initialScan.getStatus());

            System.out.println("Scan cancelled...");
        }

        for (ScanResult result : initialScan.getScanResults()) {
            System.out.println(result.getIpAddr() + " is reachable");
        }
        System.out.println("Scan status: " + initialScan.getStatus());
    }
}
