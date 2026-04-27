package com.cg.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class VirtualGoldHoldingDTO {
    private Integer holdingId;
    private Integer userId;
    private String userName;
    private Integer branchId;
    private String vendorName;
    private BigDecimal quantity;
    private LocalDateTime createdAt;

    public VirtualGoldHoldingDTO() {}

    public Integer getHoldingId() { return holdingId; }
    public void setHoldingId(Integer holdingId) { this.holdingId = holdingId; }
    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }
    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }
    public Integer getBranchId() { return branchId; }
    public void setBranchId(Integer branchId) { this.branchId = branchId; }
    public String getVendorName() { return vendorName; }
    public void setVendorName(String vendorName) { this.vendorName = vendorName; }
    public BigDecimal getQuantity() { return quantity; }
    public void setQuantity(BigDecimal quantity) { this.quantity = quantity; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}