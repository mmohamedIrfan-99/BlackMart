package com.blackmart.blackmart.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Order {

    private Long id;
    private Long buyerId;
    private BigDecimal totalAmount;
    private String status;
    private String paymentStatus;
    private String shippingAddress;
    private LocalDateTime createdAt;

    public Order() {
    }

    public Order(Long id, Long buyerId, BigDecimal totalAmount,
                 String status, String paymentStatus,
                 String shippingAddress, LocalDateTime createdAt) {
        this.id = id;
        this.buyerId = buyerId;
        this.totalAmount = totalAmount;
        this.status = status;
        this.paymentStatus = paymentStatus;
        this.shippingAddress = shippingAddress;
        this.createdAt = createdAt;
    }

    public Order(Long buyerId, BigDecimal totalAmount,
                 String shippingAddress) {
        this.buyerId = buyerId;
        this.totalAmount = totalAmount;
        this.status = "PLACED";
        this.paymentStatus = "PAID";
        this.shippingAddress = shippingAddress;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(Long buyerId) {
        this.buyerId = buyerId;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
