package com.cg.dto;

import com.cg.enums.PaymentMethod;
import com.cg.enums.TransactionType;
import java.math.BigDecimal;

public class PaymentCreateDTO {
    private Integer userId;
    private BigDecimal amount;
    private PaymentMethod paymentMethod;
    private TransactionType transactionType;

    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public PaymentMethod getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(PaymentMethod paymentMethod) { this.paymentMethod = paymentMethod; }
    public TransactionType getTransactionType() { return transactionType; }
    public void setTransactionType(TransactionType transactionType) { this.transactionType = transactionType; }
}
