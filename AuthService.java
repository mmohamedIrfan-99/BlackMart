package com.blackmart.blackmart.service;

import com.blackmart.blackmart.dao.UserDao;
import com.blackmart.blackmart.model.User;
import org.mindrot.jbcrypt.BCrypt;

public class AuthService {

    private final UserDao userDao;

    public AuthService() {
        this.userDao = new UserDao();
    }

    public User register(String name,
                         String email,
                         String password,
                         String role) throws Exception {

        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name is required.");
        }

        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email is required.");
        }

        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("Password is required.");
        }

        if (password.length() < 6) {
            throw new IllegalArgumentException(
                    "Password must contain at least 6 characters."
            );
        }

        email = email.trim().toLowerCase();

        if (userDao.emailExists(email)) {
            throw new IllegalArgumentException(
                    "Email already registered."
            );
        }

        if (role == null ||
                (!role.equals("BUYER") && !role.equals("SELLER"))) {
            role = "BUYER";
        }

        String hashedPassword = BCrypt.hashpw(
                password,
                BCrypt.gensalt(12)
        );

        User user = new User(
                name.trim(),
                email,
                hashedPassword,
                role
        );

        long id = userDao.create(user);

        user.setId(id);

        return user;
    }

    public User login(String email,
                      String password) throws Exception {

        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email is required.");
        }

        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("Password is required.");
        }

        email = email.trim().toLowerCase();

        User user = userDao.findByEmail(email);

        if (user == null) {
            throw new IllegalArgumentException(
                    "Invalid email or password."
            );
        }

        boolean passwordMatches = BCrypt.checkpw(
                password,
                user.getPassword()
        );

        if (!passwordMatches) {
            throw new IllegalArgumentException(
                    "Invalid email or password."
            );
        }

        return user;
    }

    public User findById(Long id) throws Exception {
        return userDao.findById(id);
    }
}
