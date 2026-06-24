package dev.daltonwood.netscan.entity;

/*
*
*
*
*
*
*/

import jakarta.persistence.*;

import java.net.InetAddress;
import java.time.LocalDateTime;

@Entity
@Table(name = "scanResults")
public class ScanResult {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long scanId;

    private Long assetId;

    private InetAddress ipAddr;

    private String macAddr;

    private String hostname;

    private LocalDateTime observedAt;

    public ScanResult() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getScanId() {
        return scanId;
    }

    public void setScanId(Long scanId) {
        this.scanId = scanId;
    }

    public Long getAssetId() {
        return assetId;
    }

    public void setAssetId(Long assetId) {
        this.assetId = assetId;
    }

    public InetAddress getIpAddr() {
        return ipAddr;
    }

    public void setIpAddr(InetAddress ipAddr) {
        this.ipAddr = ipAddr;
    }

    public String getMacAddr() {
        return macAddr;
    }

    public void setMacAddr(String macAddr) {
        this.macAddr = macAddr;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public LocalDateTime getObservedAt() {
        return observedAt;
    }

    public void setObservedAt(LocalDateTime observedAt) {
        this.observedAt = observedAt;
    }
}
