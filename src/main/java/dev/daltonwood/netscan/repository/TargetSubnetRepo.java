package dev.daltonwood.netscan.repository;

import dev.daltonwood.netscan.entity.TargetSubnet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface TargetSubnetRepo extends JpaRepository<TargetSubnet, Long> {

    Optional<TargetSubnet> findByCidrValue(String cidrValue);
}
