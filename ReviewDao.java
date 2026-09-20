package com.blackmart.blackmart.dao;

import com.blackmart.blackmart.model.Review;
import com.blackmart.blackmart.util.DBUtil;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ReviewDao {

    public long create(Review review) throws SQLException {

        String sql = """
                INSERT INTO reviews
                (buyer_id, product_id, rating, comment)
                VALUES (?, ?, ?, ?)
                """;

        try (Connection connection =
                     DBUtil.getDataSource().getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(
                             sql,
                             Statement.RETURN_GENERATED_KEYS)) {

            statement.setLong(1, review.getBuyerId());
            statement.setLong(2, review.getProductId());
            statement.setInt(3, review.getRating());
            statement.setString(4, review.getComment());

            statement.executeUpdate();

            try (ResultSet resultSet =
                         statement.getGeneratedKeys()) {

                if (resultSet.next()) {
                    return resultSet.getLong(1);
                }
            }
        }

        throw new SQLException("Review creation failed.");
    }

    public List<Review> findByProduct(Long productId)
            throws SQLException {

        String sql = """
                SELECT id, buyer_id, product_id, rating,
                       comment, created_at
                FROM reviews
                WHERE product_id = ?
                ORDER BY created_at DESC
                """;

        try (Connection connection =
                     DBUtil.getDataSource().getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setLong(1, productId);

            List<Review> reviews = new ArrayList<>();

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                while (resultSet.next()) {
                    reviews.add(mapReview(resultSet));
                }
            }

            return reviews;
        }
    }

    public boolean exists(Long buyerId, Long productId)
            throws SQLException {

        String sql = """
                SELECT COUNT(*)
                FROM reviews
                WHERE buyer_id = ?
                  AND product_id = ?
                """;

        try (Connection connection =
                     DBUtil.getDataSource().getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setLong(1, buyerId);
            statement.setLong(2, productId);

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                if (resultSet.next()) {
                    return resultSet.getInt(1) > 0;
                }
            }
        }

        return false;
    }

    public double getAverageRating(Long productId)
            throws SQLException {

        String sql = """
                SELECT COALESCE(AVG(rating), 0)
                FROM reviews
                WHERE product_id = ?
                """;

        try (Connection connection =
                     DBUtil.getDataSource().getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setLong(1, productId);

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                if (resultSet.next()) {
                    return resultSet.getDouble(1);
                }
            }
        }

        return 0.0;
    }

    private Review mapReview(ResultSet resultSet)
            throws SQLException {

        Timestamp timestamp =
                resultSet.getTimestamp("created_at");

        LocalDateTime createdAt =
                timestamp != null
                        ? timestamp.toLocalDateTime()
                        : null;

        return new Review(
                resultSet.getLong("id"),
                resultSet.getLong("buyer_id"),
                resultSet.getLong("product_id"),
                resultSet.getInt("rating"),
                resultSet.getString("comment"),
                createdAt
        );
    }
}
