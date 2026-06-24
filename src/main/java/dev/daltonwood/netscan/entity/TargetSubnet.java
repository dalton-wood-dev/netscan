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
@Table(name = "subnetTargets")
public class TargetSubnet {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    private String cidrVal;

    private String description;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public TargetSubnet() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCidrVal() {
        return cidrVal;
    }

    public void setCidrVal(String cidrVal) {
        this.cidrVal = cidrVal;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
