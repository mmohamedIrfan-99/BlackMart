package com.blackmart.blackmart.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class OrderItem {

    private Long id;
    private Long orderId;
    private Long productId;
    private Long sellerId;
    private int quantity;
    private BigDecimal price;
    private LocalDateTime createdAt;

    public OrderItem() {
    }

    public OrderItem(Long id, Long orderId, Long productId,
                     Long sellerId, int quantity, BigDecimal price,
                     LocalDateTime createdAt) {
        this.id = id;
        this.orderId = orderId;
        this.productId = productId;
        this.sellerId = sellerId;
        this.quantity = quantity;
        this.price = price;
        this.createdAt = createdAt;
    }

    public OrderItem(Long orderId, Long productId, Long sellerId,
                     int quantity, BigDecimal price) {
        this.orderId = orderId;
        this.productId = productId;
        this.sellerId = sellerId;
        this.quantity = quantity;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getSellerId() {
        return sellerId;
    }

    public void setSellerId(Long sellerId) {
        this.sellerId = sellerId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
