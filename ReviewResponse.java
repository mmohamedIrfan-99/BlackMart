package com.blackmart.blackmart.dto;

import java.sql.Timestamp;

public class ReviewResponse {

    private Long id;
    private Long buyerId;
    private Long productId;
    private String buyerName;
    private int rating;
    private String comment;
    private Timestamp createdAt;

    public ReviewResponse() {
    }

    public ReviewResponse(Long id, Long buyerId, Long productId,
                          String buyerName, int rating,
                          String comment, Timestamp createdAt) {
        this.id = id;
        this.buyerId = buyerId;
        this.productId = productId;
        this.buyerName = buyerName;
        this.rating = rating;
        this.comment = comment;
        this.createdAt = createdAt;
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

    public String getBuyerName() {
        return buyerName;
    }

    public void setBuyerName(String buyerName) {
        this.buyerName = buyerName;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}
