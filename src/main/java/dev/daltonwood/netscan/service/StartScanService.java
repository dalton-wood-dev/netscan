package dev.daltonwood.netscan.service;

/*
*
*
*
*
*
*/

import dev.daltonwood.netscan.entity.Scan;
import dev.daltonwood.netscan.entity.ScanStatus;
import dev.daltonwood.netscan.entity.TargetSubnet;
import dev.daltonwood.netscan.repository.ScanRepo;
import dev.daltonwood.netscan.repository.TargetSubnetRepo;
import dev.daltonwood.netscan.network.SubnetService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class StartScanService {

    private final SubnetService subnetService;
    private final TargetSubnetRepo targetSubnetRepo;
    private final ScanRepo scanRepo;

    public StartScanService(SubnetService subnetService, TargetSubnetRepo targetSubnetRepo, ScanRepo scanRepo) {
        this.subnetService = subnetService;
        this.targetSubnetRepo = targetSubnetRepo;
        this.scanRepo = scanRepo;
    }

    private TargetSubnet findOrCreateTarget(String userInput) {

        Optional<TargetSubnet> result = targetSubnetRepo.findByCidrValue(userInput);

        TargetSubnet targetSubnet;
        if (result.isPresent()) {

            targetSubnet = result.get();
        } else {

            targetSubnet = new TargetSubnet(userInput);
            targetSubnetRepo.save(targetSubnet);
        }

        return targetSubnet;
    }

    @Transactional
    public Scan createScan(String userInput) {

        Scan scan = new Scan();
        boolean isValidCidr = subnetService.isValidCidr(userInput);

        Scan savedScan;
        if (isValidCidr) {

            TargetSubnet targetSubnet = findOrCreateTarget(userInput);

            scan.setTargetSubnet(targetSubnet);
            scan.setStatus(ScanStatus.STAGED);

            savedScan = scanRepo.save(scan);

        } else {

            throw new IllegalArgumentException("Invalid CIDR: please enter a valid CIDR value.");
        }

        return savedScan;
    }

    public String getScanInfo(String userInput) {
        return subnetService.getSubnetInfo(userInput);
    }

//    TODO: write method to iterate ipv4 address within subnet - return List<ScanResult>
    public String startScan(Scan scan) {
        scan.setStartedAt(LocalDateTime.now());
        scan.setStatus(ScanStatus.IN_PROGRESS);

        return "Scan started...";

    }
}
