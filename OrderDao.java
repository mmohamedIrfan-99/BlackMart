package com.blackmart.blackmart.dao;

import com.blackmart.blackmart.model.Order;
import com.blackmart.blackmart.model.OrderItem;
import com.blackmart.blackmart.util.DBUtil;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OrderDao {

    public long createOrder(Order order, List<OrderItem> items)
            throws SQLException {

        String orderSql = """
                INSERT INTO orders
                (buyer_id, total_amount, status,
                 payment_status, shipping_address)
                VALUES (?, ?, ?, ?, ?)
                """;

        String itemSql = """
                INSERT INTO order_items
                (order_id, product_id, seller_id, quantity, price)
                VALUES (?, ?, ?, ?, ?)
                """;

        String stockSql = """
                UPDATE products
                SET stock = stock - ?
                WHERE id = ?
                  AND stock >= ?
                """;

        try (Connection connection =
                     DBUtil.getDataSource().getConnection()) {

            try {
                connection.setAutoCommit(false);

                long orderId;

                try (PreparedStatement statement =
                             connection.prepareStatement(
                                     orderSql,
                                     Statement.RETURN_GENERATED_KEYS)) {

                    statement.setLong(1, order.getBuyerId());
                    statement.setBigDecimal(2, order.getTotalAmount());
                    statement.setString(3, order.getStatus());
                    statement.setString(4, order.getPaymentStatus());
                    statement.setString(5, order.getShippingAddress());

                    statement.executeUpdate();

                    try (ResultSet resultSet =
                                 statement.getGeneratedKeys()) {

                        if (!resultSet.next()) {
                            throw new SQLException(
                                    "Order creation failed."
                            );
                        }

                        orderId = resultSet.getLong(1);
                    }
                }

                try (PreparedStatement itemStatement =
                             connection.prepareStatement(itemSql);
                     PreparedStatement stockStatement =
                             connection.prepareStatement(stockSql)) {

                    for (OrderItem item : items) {

                        itemStatement.setLong(1, orderId);
                        itemStatement.setLong(2, item.getProductId());
                        itemStatement.setLong(3, item.getSellerId());
                        itemStatement.setInt(4, item.getQuantity());
                        itemStatement.setBigDecimal(5, item.getPrice());

                        itemStatement.addBatch();

                        stockStatement.setInt(1, item.getQuantity());
                        stockStatement.setLong(2, item.getProductId());
                        stockStatement.setInt(3, item.getQuantity());

                        int updated =
                                stockStatement.executeUpdate();

                        if (updated == 0) {
                            throw new SQLException(
                                    "Insufficient stock for product: "
                                            + item.getProductId()
                            );
                        }
                    }

                    itemStatement.executeBatch();
                }

                connection.commit();

                return orderId;

            } catch (Exception e) {

                connection.rollback();

                if (e instanceof SQLException) {
                    throw (SQLException) e;
                }

                throw new SQLException(
                        "Order creation failed.",
                        e
                );
            }
        }
    }

    public Order findById(Long orderId) throws SQLException {

        String sql = """
                SELECT id, buyer_id, total_amount, status,
                       payment_status, shipping_address, created_at
                FROM orders
                WHERE id = ?
                """;

        try (Connection connection =
                     DBUtil.getDataSource().getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setLong(1, orderId);

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                if (resultSet.next()) {
                    return mapOrder(resultSet);
                }
            }
        }

        return null;
    }

    public List<Order> findByBuyer(Long buyerId)
            throws SQLException {

        String sql = """
                SELECT id, buyer_id, total_amount, status,
                       payment_status, shipping_address, created_at
                FROM orders
                WHERE buyer_id = ?
                ORDER BY created_at DESC
                """;

        try (Connection connection =
                     DBUtil.getDataSource().getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setLong(1, buyerId);

            List<Order> orders = new ArrayList<>();

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                while (resultSet.next()) {
                    orders.add(mapOrder(resultSet));
                }
            }

            return orders;
        }
    }

    public List<Order> findAll() throws SQLException {

        String sql = """
                SELECT id, buyer_id, total_amount, status,
                       payment_status, shipping_address, created_at
                FROM orders
                ORDER BY created_at DESC
                """;

        try (Connection connection =
                     DBUtil.getDataSource().getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql);
             ResultSet resultSet =
                     statement.executeQuery()) {

            List<Order> orders = new ArrayList<>();

            while (resultSet.next()) {
                orders.add(mapOrder(resultSet));
            }

            return orders;
        }
    }

    public boolean updateStatus(Long orderId,
                                String status)
            throws SQLException {

        String sql = """
                UPDATE orders
                SET status = ?
                WHERE id = ?
                """;

        try (Connection connection =
                     DBUtil.getDataSource().getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, status);
            statement.setLong(2, orderId);

            return statement.executeUpdate() > 0;
        }
    }

    private Order mapOrder(ResultSet resultSet)
            throws SQLException {

        Timestamp timestamp =
                resultSet.getTimestamp("created_at");

        LocalDateTime createdAt =
                timestamp != null
                        ? timestamp.toLocalDateTime()
                        : null;

        return new Order(
                resultSet.getLong("id"),
                resultSet.getLong("buyer_id"),
                resultSet.getBigDecimal("total_amount"),
                resultSet.getString("status"),
                resultSet.getString("payment_status"),
                resultSet.getString("shipping_address"),
                createdAt
        );
    }
  }
