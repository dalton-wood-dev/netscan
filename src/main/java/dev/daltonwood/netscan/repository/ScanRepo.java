package dev.daltonwood.netscan.repository;

import dev.daltonwood.netscan.entity.Scan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScanRepo extends JpaRepository<Scan, Long> {
}
