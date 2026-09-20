package com.blackmart.blackmart.dao;

import com.blackmart.blackmart.model.OrderItem;
import com.blackmart.blackmart.util.DBUtil;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OrderItemDao {

    public List<OrderItem> findByOrderId(Long orderId)
            throws SQLException {

        String sql = """
                SELECT id, order_id, product_id, seller_id,
                       quantity, price, created_at
                FROM order_items
                WHERE order_id = ?
                ORDER BY id
                """;

        try (Connection connection =
                     DBUtil.getDataSource().getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setLong(1, orderId);

            List<OrderItem> items = new ArrayList<>();

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                while (resultSet.next()) {
                    items.add(mapOrderItem(resultSet));
                }
            }

            return items;
        }
    }

    public List<OrderItem> findBySeller(Long sellerId)
            throws SQLException {

        String sql = """
                SELECT id, order_id, product_id, seller_id,
                       quantity, price, created_at
                FROM order_items
                WHERE seller_id = ?
                ORDER BY created_at DESC
                """;

        try (Connection connection =
                     DBUtil.getDataSource().getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setLong(1, sellerId);

            List<OrderItem> items = new ArrayList<>();

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                while (resultSet.next()) {
                    items.add(mapOrderItem(resultSet));
                }
            }

            return items;
        }
    }

    private OrderItem mapOrderItem(ResultSet resultSet)
            throws SQLException {

        Timestamp timestamp =
                resultSet.getTimestamp("created_at");

        LocalDateTime createdAt =
                timestamp != null
                        ? timestamp.toLocalDateTime()
                        : null;

        return new OrderItem(
                resultSet.getLong("id"),
                resultSet.getLong("order_id"),
                resultSet.getLong("product_id"),
                resultSet.getLong("seller_id"),
                resultSet.getInt("quantity"),
                resultSet.getBigDecimal("price"),
                createdAt
        );
    }
}
