package com.blackmart.blackmart.service;

import com.blackmart.blackmart.dao.ReviewDao;
import com.blackmart.blackmart.model.Review;

import java.util.List;

public class ReviewService {

    private final ReviewDao reviewDao;

    public ReviewService() {
        this.reviewDao = new ReviewDao();
    }

    public void addReview(long buyerId,
                          long productId,
                          int rating,
                          String comment) throws Exception {

        if (buyerId <= 0) {
            throw new IllegalArgumentException("Invalid buyer");
        }

        if (productId <= 0) {
            throw new IllegalArgumentException("Invalid product");
        }

        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException(
                    "Rating must be between 1 and 5"
            );
        }

        if (comment != null && comment.length() > 1000) {
            throw new IllegalArgumentException(
                    "Comment is too long"
            );
        }

        if (reviewDao.exists(buyerId, productId)) {
            throw new IllegalArgumentException(
                    "You have already reviewed this product"
            );
        }

        Review review = new Review();

        review.setBuyerId(buyerId);
        review.setProductId(productId);
        review.setRating(rating);
        review.setComment(
                comment == null ? "" : comment.trim()
        );

        reviewDao.create(review);
    }

    public List<Review> getProductReviews(long productId) throws Exception {

        if (productId <= 0) {
            throw new IllegalArgumentException("Invalid product");
        }

        return reviewDao.findByProduct(productId);
    }

    public double getAverageRating(long productId) throws Exception {

        if (productId <= 0) {
            throw new IllegalArgumentException("Invalid product");
        }

        return reviewDao.getAverageRating(productId);
    }

    public boolean hasReviewed(long buyerId, long productId)
            throws Exception {

        if (buyerId <= 0 || productId <= 0) {
            return false;
        }

        return reviewDao.exists(buyerId, productId);
    }
        }
