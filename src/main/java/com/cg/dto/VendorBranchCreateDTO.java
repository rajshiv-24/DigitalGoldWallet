package com.cg.dto;

import java.math.BigDecimal;

public class VendorBranchCreateDTO {
    private Long vendorId;
    private Integer addressId;
    private BigDecimal quantity;

    public Long getVendorId() { return vendorId; }
    public void setVendorId(Long vendorId) { this.vendorId = vendorId; }
    public Integer getAddressId() { return addressId; }
    public void setAddressId(Integer addressId) { this.addressId = addressId; }
    public BigDecimal getQuantity() { return quantity; }
    public void setQuantity(BigDecimal quantity) { this.quantity = quantity; }
}
