package com.blackmart.blackmart.model;

import java.time.LocalDateTime;

public class CartItem {

    private Long id;
    private Long buyerId;
    private Long productId;
    private int quantity;
    private LocalDateTime createdAt;

    public CartItem() {
    }

    public CartItem(Long id, Long buyerId, Long productId,
                    int quantity, LocalDateTime createdAt) {
        this.id = id;
        this.buyerId = buyerId;
        this.productId = productId;
        this.quantity = quantity;
        this.createdAt = createdAt;
    }

    public CartItem(Long buyerId, Long productId, int quantity) {
        this.buyerId = buyerId;
        this.productId = productId;
        this.quantity = quantity;
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

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
