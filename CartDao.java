package com.blackmart.blackmart.dao;

import com.blackmart.blackmart.model.CartItem;
import com.blackmart.blackmart.util.DBUtil;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CartDao {

    public List<CartItem> findByBuyer(Long buyerId) throws SQLException {

        String sql = """
                SELECT id, buyer_id, product_id, quantity, created_at
                FROM cart_items
                WHERE buyer_id = ?
                ORDER BY created_at DESC
                """;

        try (Connection connection = DBUtil.getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, buyerId);

            List<CartItem> items = new ArrayList<>();

            try (ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {
                    items.add(mapCartItem(resultSet));
                }
            }

            return items;
        }
    }

    public CartItem findItem(Long buyerId, Long productId)
            throws SQLException {

        String sql = """
                SELECT id, buyer_id, product_id, quantity, created_at
                FROM cart_items
                WHERE buyer_id = ?
                  AND product_id = ?
                """;

        try (Connection connection = DBUtil.getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, buyerId);
            statement.setLong(2, productId);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {
                    return mapCartItem(resultSet);
                }
            }
        }

        return null;
    }

    public void addItem(Long buyerId, Long productId, int quantity)
            throws SQLException {

        String sql = """
                INSERT INTO cart_items
                (buyer_id, product_id, quantity)
                VALUES (?, ?, ?)
                """;

        try (Connection connection = DBUtil.getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, buyerId);
            statement.setLong(2, productId);
            statement.setInt(3, quantity);

            statement.executeUpdate();
        }
    }

    public boolean updateQuantity(Long buyerId,
                                  Long productId,
                                  int quantity)
            throws SQLException {

        String sql = """
                UPDATE cart_items
                SET quantity = ?
                WHERE buyer_id = ?
                  AND product_id = ?
                """;

        try (Connection connection = DBUtil.getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, quantity);
            statement.setLong(2, buyerId);
            statement.setLong(3, productId);

            return statement.executeUpdate() > 0;
        }
    }

    public boolean removeItem(Long buyerId,
                              Long productId)
            throws SQLException {

        String sql = """
                DELETE FROM cart_items
                WHERE buyer_id = ?
                  AND product_id = ?
                """;

        try (Connection connection = DBUtil.getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, buyerId);
            statement.setLong(2, productId);

            return statement.executeUpdate() > 0;
        }
    }

    public boolean clearCart(Long buyerId) throws SQLException {

        String sql = """
                DELETE FROM cart_items
                WHERE buyer_id = ?
                """;

        try (Connection connection = DBUtil.getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, buyerId);

            statement.executeUpdate();
            return true;
        }
    }

    private CartItem mapCartItem(ResultSet resultSet)
            throws SQLException {

        Timestamp timestamp =
                resultSet.getTimestamp("created_at");

        LocalDateTime createdAt =
                timestamp != null
                        ? timestamp.toLocalDateTime()
                        : null;

        return new CartItem(
                resultSet.getLong("id"),
                resultSet.getLong("buyer_id"),
                resultSet.getLong("product_id"),
                resultSet.getInt("quantity"),
                createdAt
        );
    }
                 }
