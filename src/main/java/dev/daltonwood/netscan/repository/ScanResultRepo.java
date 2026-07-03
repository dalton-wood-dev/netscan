package dev.daltonwood.netscan.repository;

import dev.daltonwood.netscan.entity.ScanResult;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScanResultRepo extends JpaRepository<ScanResult, Long> {
}
