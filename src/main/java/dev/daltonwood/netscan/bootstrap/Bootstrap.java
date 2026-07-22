package dev.daltonwood.netscan.bootstrap;

import dev.daltonwood.netscan.entity.Scan;
import dev.daltonwood.netscan.entity.ScanResult;
import dev.daltonwood.netscan.network.IpService;
import dev.daltonwood.netscan.service.ScanService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Scanner;

/*
*
*
*
*
*
*/

@Component
public class Bootstrap implements CommandLineRunner {

    private final ScanService scanService;
    private final Scanner scanner = new Scanner(System.in);
    private final IpService ipService;

    public Bootstrap(ScanService scanService, IpService ipService) {
        this.scanService = scanService;
        this.ipService = ipService;
    }

    @Override
    public void run(String... args) {
//        TODO: Uncomment to prompt actual user input
        Scan initialScan = null;

        System.out.println("NetScan started...");
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
        System.out.println("CIDR validated...");

//        TODO: Remove hardcoded scan
//        Scan initialScan = scanService.createScan("10.12.12.0/24");

        String validCidrValue = initialScan.getTargetSubnet().getCidrValue();
        String validCidrSummary = ipService.getSubnetInfo(validCidrValue);

        System.out.println(validCidrSummary);
        System.out.println("Scan status: " + initialScan.getStatus());

        System.out.println("Execute the scan? \nY or N");
        String consent = scanner.nextLine();

        while (!consent.equalsIgnoreCase("y") && !consent.equalsIgnoreCase("n")) {

            System.out.println("Start scan? \nY or N");
            consent = scanner.nextLine();
        }

        if (consent.equalsIgnoreCase("y")) {

            Scan completedScan = scanService.startScan(initialScan);

            List<ScanResult> results = completedScan.getScanResults();

            for (ScanResult result : results) {
                System.out.println(result.getIpAddr().toString() + " is reachable");
            }

            System.out.println("Scan status: " + initialScan.getStatus());

        } else if (consent.equalsIgnoreCase("n")) {

            System.out.println("Scan status: " + initialScan.getStatus());
        }

    }
}
