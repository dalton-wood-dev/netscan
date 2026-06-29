package dev.daltonwood.netscan.bootstrap;

import dev.daltonwood.netscan.entity.Scan;
import dev.daltonwood.netscan.service.StartScanService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class Bootstrap implements CommandLineRunner {

    private final StartScanService startScanService;
    private final Scanner scanner = new Scanner(System.in);

    public Bootstrap(StartScanService startScanService) {
        this.startScanService = startScanService;
    }

    @Override
    public void run(String... args) {

        System.out.println("NetScan started...");

        Scan initialScan = null;

        System.out.print("Input CIDR value:");
        String userInput = scanner.nextLine();

        while (initialScan == null) {
            try {
                initialScan = startScanService.createScan(userInput);
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
                System.out.print("Input CIDR value:");
                userInput = scanner.nextLine();
            }
        }

        System.out.println("CIDR validated...");
        System.out.println(startScanService.getScanInfo(initialScan.getTargetSubnet().getCidrValue()));
        System.out.println("Scan status: " + initialScan.getStatus());

        System.out.println("Start scan? \nY or N");
        String consent = scanner.nextLine();

        while (!consent.equalsIgnoreCase("y") && !consent.equalsIgnoreCase("n")) {
            System.out.println("Start scan? \nY or N");
            consent = scanner.nextLine();
        }

        if (consent.equalsIgnoreCase("y")) {

            System.out.println(startScanService.startScan(initialScan));

        } else if (consent.equalsIgnoreCase("n")) {

            System.out.println("Scan cancelled...");
        }
    }
}
