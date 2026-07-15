package dev.daltonwood.netscan.service;

/*
*
*
*
*
*
*/

import dev.daltonwood.netscan.entity.Scan;
import dev.daltonwood.netscan.entity.ScanResult;
import dev.daltonwood.netscan.entity.ScanStatus;
import dev.daltonwood.netscan.entity.TargetSubnet;
import dev.daltonwood.netscan.repository.ScanRepo;
import dev.daltonwood.netscan.repository.ScanResultRepo;
import dev.daltonwood.netscan.network.IpService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.*;

@Service
public class ScanService {

    private final IpService ipService;
    private final TargetSubnetService targetSubnetService;
    private final ScanRepo scanRepo;
    private final ScanResultRepo scanResultRepo;

    public ScanService(IpService ipService, TargetSubnetService targetSubnetService, ScanRepo scanRepo, ScanResultRepo scanResultRepo) {
        this.ipService = ipService;
        this.targetSubnetService = targetSubnetService;
        this.scanRepo = scanRepo;
        this.scanResultRepo = scanResultRepo;
    }

    @Transactional
    public Scan createScan(String userInput) {

        Scan scan = new Scan();
        boolean isValidCidr = ipService.isValidCidr(userInput);

        if (isValidCidr) {

            TargetSubnet targetSubnet = targetSubnetService.findOrCreateTarget(userInput);
            scan.setTargetSubnet(targetSubnet);
            scan.setStatus(ScanStatus.STAGED);

            return scanRepo.save(scan);

        } else {

            throw new IllegalArgumentException("Invalid CIDR: please enter a valid CIDR value.");
        }
    }

    @Transactional
    public Scan startScan(Scan scan) {

        scan.setStatus(ScanStatus.IN_PROGRESS);
        System.out.println(scan.getStatus());
        scan.setStartedAt(LocalDateTime.now());
        String cidrValue = scan.getTargetSubnet().getCidrValue();

        List<ScanResult> scanResults = ipService.pingEachAddress(cidrValue, scan);

        scanResults = scanResultRepo.saveAll(scanResults);
        scan.setScanResults(scanResults);
        scan.setCompletedAt(LocalDateTime.now());
        scan.setStatus(ScanStatus.COMPLETED);
        return scanRepo.save(scan);
    }
}
