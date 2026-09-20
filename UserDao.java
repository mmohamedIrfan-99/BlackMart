package com.blackmart.blackmart.dao;

import com.blackmart.blackmart.model.User;
import com.blackmart.blackmart.util.DBUtil;

import java.sql.*;
import java.time.LocalDateTime;

public class UserDao {

    public User findByEmail(String email) throws SQLException {

        String sql = """
                SELECT id, name, email, password, role, created_at
                FROM users
                WHERE email = ?
                """;

        try (Connection connection = DBUtil.getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, email);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {
                    return mapUser(resultSet);
                }
            }
        }

        return null;
    }

    public User findById(Long id) throws SQLException {

        String sql = """
                SELECT id, name, email, password, role, created_at
                FROM users
                WHERE id = ?
                """;

        try (Connection connection = DBUtil.getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {
                    return mapUser(resultSet);
                }
            }
        }

        return null;
    }

    public long create(User user) throws SQLException {

        String sql = """
                INSERT INTO users
                (name, email, password, role)
                VALUES (?, ?, ?, ?)
                """;

        try (Connection connection = DBUtil.getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     sql,
                     Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, user.getName());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getPassword());
            statement.setString(4, user.getRole());

            statement.executeUpdate();

            try (ResultSet resultSet = statement.getGeneratedKeys()) {

                if (resultSet.next()) {
                    return resultSet.getLong(1);
                }
            }
        }

        throw new SQLException("User creation failed.");
    }

    public boolean emailExists(String email) throws SQLException {

        String sql = """
                SELECT COUNT(*)
                FROM users
                WHERE email = ?
                """;

        try (Connection connection = DBUtil.getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, email);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {
                    return resultSet.getInt(1) > 0;
                }
            }
        }

        return false;
    }

    private User mapUser(ResultSet resultSet) throws SQLException {

        Timestamp timestamp = resultSet.getTimestamp("created_at");

        LocalDateTime createdAt =
                timestamp != null
                        ? timestamp.toLocalDateTime()
                        : null;

        return new User(
                resultSet.getLong("id"),
                resultSet.getString("name"),
                resultSet.getString("email"),
                resultSet.getString("password"),
                resultSet.getString("role"),
                createdAt
        );
    }
               }
