package dev.daltonwood.netscan.network;

/*
*
*
*
*
*
*/

import dev.daltonwood.netscan.entity.Scan;
import dev.daltonwood.netscan.entity.ScanResult;
import org.apache.commons.net.util.SubnetUtils;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.InetAddress;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

@Component
@Service
public class IpService {

    public boolean isValidCidr(String userInput) {

        if (userInput == null || userInput.isBlank()) {
            return false;
        }

        try {
            new SubnetUtils(userInput);
            return true;

        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public String getSubnetInfo(String cidrValue) {

        SubnetUtils block = new SubnetUtils(cidrValue);
        return block.getInfo().toString();
    }

    private Iterable<String> getIterableFromSubnet(String cidrValue) {

        SubnetUtils subnetUtils = new SubnetUtils(cidrValue);

        return subnetUtils.getInfo().iterableAddressStrings();

    }

    public List<ScanResult> pingEachAddress(String cidrValue, Scan scan) {

        Iterable<String> potentialEndpoints = getIterableFromSubnet(cidrValue);

        ExecutorService pool = Executors.newFixedThreadPool(16);
        List<Future<ScanResult>> scanResultFutures = new ArrayList<>();
        List<ScanResult> scanResults = new ArrayList<>();

        for (String currAddress : potentialEndpoints) {
            Future<ScanResult> scanResultFuture = pool.submit(() -> {
                try {
                    InetAddress currIp = InetAddress.getByName(currAddress);
                    boolean isReachable = currIp.isReachable(200);
                    if (isReachable) {
//                      TODO: Decouple ping scanning from entity creation. this method should return results to the entity service and the entity service should instantiate and persist entities.
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
            if (!pool.awaitTermination(1000, TimeUnit.MILLISECONDS)) {
                pool.shutdownNow();
            }
        } catch (InterruptedException e) {
            pool.shutdownNow();
        }

        return scanResults;
    }

}
