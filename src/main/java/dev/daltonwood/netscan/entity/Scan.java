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

@Entity
@Table(name="scans")
public class Scan {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String targetSubnet;

    private LocalDateTime startedAt;

    private LocalDateTime completedAt;

    private String status;

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

    public String getTargetSubnet() {
        return targetSubnet;
    }

    public void setTargetSubnet(String targetSubnet) {
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
