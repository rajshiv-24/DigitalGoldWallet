package com.cg.dto;

import com.cg.enums.PaymentMethod;
import com.cg.enums.PaymentStatus;
import com.cg.enums.TransactionType;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PaymentDTO {
    private Integer paymentId;
    private Integer userId;
    private String userName;
    private BigDecimal amount;
    private PaymentMethod paymentMethod;
    private TransactionType transactionType;
    private PaymentStatus paymentStatus;
    private LocalDateTime createdAt;

    public PaymentDTO() {}

    public Integer getPaymentId() { return paymentId; }
    public void setPaymentId(Integer paymentId) { this.paymentId = paymentId; }
    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }
    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public PaymentMethod getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(PaymentMethod paymentMethod) { this.paymentMethod = paymentMethod; }
    public TransactionType getTransactionType() { return transactionType; }
    public void setTransactionType(TransactionType transactionType) { this.transactionType = transactionType; }
    public PaymentStatus getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(PaymentStatus paymentStatus) { this.paymentStatus = paymentStatus; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
