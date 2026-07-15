package dev.daltonwood.netscan.service;

import dev.daltonwood.netscan.entity.TargetSubnet;
import dev.daltonwood.netscan.network.IpService;
import dev.daltonwood.netscan.repository.TargetSubnetRepo;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Component
@Service
public class TargetSubnetService {
    private final TargetSubnetRepo targetSubnetRepo;

    public TargetSubnetService(TargetSubnetRepo targetSubnetRepo) {
        this.targetSubnetRepo = targetSubnetRepo;
    }

    public TargetSubnet findOrCreateTarget(String userInput) {

        Optional<TargetSubnet> result = targetSubnetRepo.findByCidrValue(userInput);

        TargetSubnet targetSubnet;
        if (result.isPresent()) {

            targetSubnet = result.get();
            return targetSubnet;

        } else {

            return targetSubnetRepo.save(new TargetSubnet(userInput));
        }
    }
}
