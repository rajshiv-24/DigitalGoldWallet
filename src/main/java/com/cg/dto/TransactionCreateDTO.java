package com.cg.dto;

import com.cg.enums.TransactionType2;
import java.math.BigDecimal;

public class TransactionCreateDTO {
    private Integer userId;
    private Integer branchId;
    private TransactionType2 transactionType;
    private BigDecimal quantity;

    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }
    public Integer getBranchId() { return branchId; }
    public void setBranchId(Integer branchId) { this.branchId = branchId; }
    public TransactionType2 getTransactionType() { return transactionType; }
    public void setTransactionType(TransactionType2 transactionType) { this.transactionType = transactionType; }
    public BigDecimal getQuantity() { return quantity; }
    public void setQuantity(BigDecimal quantity) { this.quantity = quantity; }
}