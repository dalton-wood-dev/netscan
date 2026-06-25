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
@Table(name = "scan_results")
public class ScanResult {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "scan_id")
    private Scan scan;

    @ManyToOne
    @JoinColumn(name = "asset_id")
    private Asset asset;

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

    public Scan getScan() {
        return scan;
    }

    public void setScan(Scan scan) {
        this.scan = scan;
    }

    public Asset getAsset() {
        return asset;
    }

    public void setAsset(Asset asset) {
        this.asset = asset;
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
