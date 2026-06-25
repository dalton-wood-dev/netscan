package dev.daltonwood.netscan.entity;

/*
*
*
*
*
*
*/

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name="scans")
public class Scan {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "target_subnet_id")
    private TargetSubnet targetSubnet;

    private LocalDateTime startedAt;

    private LocalDateTime completedAt;

    @Enumerated(EnumType.STRING)
    private ScanStatus status;

    @OneToMany(mappedBy = "scan", cascade = CascadeType.REMOVE)
    private List<ScanResult> scanResults;

    private int assetsFoundCount;

    private String errorMessage;

    public Scan() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TargetSubnet getTargetSubnet() {
        return targetSubnet;
    }

    public void setTargetSubnet(TargetSubnet targetSubnet) {
        this.targetSubnet = targetSubnet;
    }

    public LocalDateTime getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(LocalDateTime startedAt) {
        this.startedAt = startedAt;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(LocalDateTime completedAt) {
        this.completedAt = completedAt;
    }

    public ScanStatus getStatus() {
        return status;
    }

    public void setStatus(ScanStatus status) {
        this.status = status;
    }

    public List<ScanResult> getScanResults() {
        return scanResults;
    }

    public void setScanResults(List<ScanResult> scanResults) {
        this.scanResults = scanResults;
    }

    public int getAssetsFoundCount() {
        return assetsFoundCount;
    }

    public void setAssetsFoundCount(int assetsFoundCount) {
        this.assetsFoundCount = assetsFoundCount;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
