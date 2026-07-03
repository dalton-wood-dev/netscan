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
import dev.daltonwood.netscan.repository.TargetSubnetRepo;
import dev.daltonwood.netscan.network.SubnetService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.InetAddress;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.*;

@Service
public class ScanService {

    private final SubnetService subnetService;
    private final TargetSubnetRepo targetSubnetRepo;
    private final ScanRepo scanRepo;
    private final ScanResultRepo scanResultRepo;

    public ScanService(SubnetService subnetService, TargetSubnetRepo targetSubnetRepo, ScanRepo scanRepo, ScanResultRepo scanResultRepo) {
        this.subnetService = subnetService;
        this.targetSubnetRepo = targetSubnetRepo;
        this.scanRepo = scanRepo;
        this.scanResultRepo = scanResultRepo;
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
        return subnetService.getSubnetInfoString(userInput);
    }

//    TODO: write method to iterate ipv4 address within subnet - return List<ScanResult>
    @Transactional
    public void startScan(Scan scan) {

        scan.setStartedAt(LocalDateTime.now());
        scan.setStatus(ScanStatus.IN_PROGRESS);
        System.out.println("Scan status: " + scan.getStatus());
        TargetSubnet targetSubnet = scan.getTargetSubnet();

        Iterable<String> potentialSubnetEndpoints = subnetService.getIterableFromSubnet(targetSubnet.getCidrValue());
        ExecutorService pool = Executors.newFixedThreadPool(16);
        List<Future<ScanResult>> scanResultFutures = new ArrayList<>();
        List<ScanResult> scanResults = new ArrayList<>();

        for (String currString : potentialSubnetEndpoints) {
            Future<ScanResult> scanResultFuture = pool.submit(() -> {
                try {
                    InetAddress currIp = InetAddress.getByName(currString);
                    boolean isReachable = currIp.isReachable(200);
                    if (isReachable) {
                        return new ScanResult(currIp, scan, LocalDateTime.now());
                    }

                    return null;
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            scanResultFutures.add(scanResultFuture);
        }

        for (Future<ScanResult> future : scanResultFutures) {
            try {
                ScanResult scanResult = future.get();

                if (scanResult != null) {
                    scanResults.add(scanResult);
                }
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        }

        pool.shutdown();
        try {
            if (!pool.awaitTermination(10000, TimeUnit.MILLISECONDS)) {
                pool.shutdownNow();
            }
        } catch (InterruptedException e) {
            pool.shutdownNow();
        }

        scanResultRepo.saveAll(scanResults);
        scan.setScanResults(scanResults);
        scan.setCompletedAt(LocalDateTime.now());
        scan.setStatus(ScanStatus.COMPLETED);
        Scan savedScan = scanRepo.save(scan);

//        return savedScan.getScanResults();

    }
}
