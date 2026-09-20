package com.blackmart.blackmart.dao;

import com.blackmart.blackmart.model.Product;
import com.blackmart.blackmart.util.DBUtil;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ProductDao {

    public Product findById(Long id) throws SQLException {

        String sql = """
                SELECT id, seller_id, name, description, category,
                       price, stock, image_url, status, created_at
                FROM products
                WHERE id = ?
                """;

        try (Connection connection = DBUtil.getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {
                    return mapProduct(resultSet);
                }
            }
        }

        return null;
    }

    public List<Product> findActiveProducts(String search,
                                            String category)
            throws SQLException {

        StringBuilder sql = new StringBuilder("""
                SELECT id, seller_id, name, description, category,
                       price, stock, image_url, status, created_at
                FROM products
                WHERE status = 'ACTIVE'
                """);

        List<Object> parameters = new ArrayList<>();

        if (search != null && !search.isBlank()) {
            sql.append("""
                     AND (
                         LOWER(name) LIKE ?
                         OR LOWER(description) LIKE ?
                         )
                    """);

            String value = "%" + search.toLowerCase() + "%";
            parameters.add(value);
            parameters.add(value);
        }

        if (category != null && !category.isBlank()) {
            sql.append(" AND category = ?");
            parameters.add(category);
        }

        sql.append(" ORDER BY created_at DESC");

        try (Connection connection = DBUtil.getDataSource().getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql.toString())) {

            for (int i = 0; i < parameters.size(); i++) {
                statement.setObject(i + 1, parameters.get(i));
            }

            List<Product> products = new ArrayList<>();

            try (ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {
                    products.add(mapProduct(resultSet));
                }
            }

            return products;
        }
    }

    public List<Product> findBySeller(Long sellerId)
            throws SQLException {

        String sql = """
                SELECT id, seller_id, name, description, category,
                       price, stock, image_url, status, created_at
                FROM products
                WHERE seller_id = ?
                ORDER BY created_at DESC
                """;

        try (Connection connection = DBUtil.getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, sellerId);

            List<Product> products = new ArrayList<>();

            try (ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {
                    products.add(mapProduct(resultSet));
                }
            }

            return products;
        }
    }

    public long create(Product product) throws SQLException {

        String sql = """
                INSERT INTO products
                (seller_id, name, description, category,
                 price, stock, image_url, status)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection connection = DBUtil.getDataSource().getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(
                             sql,
                             Statement.RETURN_GENERATED_KEYS)) {

            statement.setLong(1, product.getSellerId());
            statement.setString(2, product.getName());
            statement.setString(3, product.getDescription());
            statement.setString(4, product.getCategory());
            statement.setBigDecimal(5, product.getPrice());
            statement.setInt(6, product.getStock());
            statement.setString(7, product.getImageUrl());
            statement.setString(8, product.getStatus());

            statement.executeUpdate();

            try (ResultSet resultSet = statement.getGeneratedKeys()) {

                if (resultSet.next()) {
                    return resultSet.getLong(1);
                }
            }
        }

        throw new SQLException("Product creation failed.");
    }

    public boolean update(Product product) throws SQLException {

        String sql = """
                UPDATE products
                SET name = ?,
                    description = ?,
                    category = ?,
                    price = ?,
                    stock = ?,
                    image_url = ?
                WHERE id = ?
                  AND seller_id = ?
                """;

        try (Connection connection = DBUtil.getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, product.getName());
            statement.setString(2, product.getDescription());
            statement.setString(3, product.getCategory());
            statement.setBigDecimal(4, product.getPrice());
            statement.setInt(5, product.getStock());
            statement.setString(6, product.getImageUrl());
            statement.setLong(7, product.getId());
            statement.setLong(8, product.getSellerId());

            return statement.executeUpdate() > 0;
        }
    }

    public boolean delete(Long productId, Long sellerId)
            throws SQLException {

        String sql = """
                DELETE FROM products
                WHERE id = ?
                  AND seller_id = ?
                """;

        try (Connection connection = DBUtil.getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, productId);
            statement.setLong(2, sellerId);

            return statement.executeUpdate() > 0;
        }
    }

    public boolean updateStatus(Long productId,
                                String status)
            throws SQLException {

        String sql = """
                UPDATE products
                SET status = ?
                WHERE id = ?
                """;

        try (Connection connection = DBUtil.getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, status);
            statement.setLong(2, productId);

            return statement.executeUpdate() > 0;
        }
    }

    private Product mapProduct(ResultSet resultSet)
            throws SQLException {

        Timestamp timestamp =
                resultSet.getTimestamp("created_at");

        LocalDateTime createdAt =
                timestamp != null
                        ? timestamp.toLocalDateTime()
                        : null;

        return new Product(
                resultSet.getLong("id"),
                resultSet.getLong("seller_id"),
                resultSet.getString("name"),
                resultSet.getString("description"),
                resultSet.getString("category"),
                resultSet.getBigDecimal("price"),
                resultSet.getInt("stock"),
                resultSet.getString("image_url"),
                resultSet.getString("status"),
                createdAt
        );
    }
              }
