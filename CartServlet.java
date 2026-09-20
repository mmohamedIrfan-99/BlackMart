package com.blackmart.blackmart.controller;

import com.blackmart.blackmart.model.CartItem;
import com.blackmart.blackmart.model.User;
import com.blackmart.blackmart.service.CartService;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/api/cart")
public class CartServlet extends HttpServlet {

    private final CartService cartService = new CartService();
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        setJson(response);

        try {
            User user = getLoggedUser(request);

            List<CartItem> items =
                    cartService.getCart(user.getId());

            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("items", items);

            response.getWriter().write(gson.toJson(result));

        } catch (IllegalArgumentException e) {
            sendError(response, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            sendError(response, "Unable to load cart");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request,
                           HttpServletResponse response)
            throws ServletException, IOException {

        setJson(response);

        try {
            User user = getLoggedUser(request);

            String action = request.getParameter("action");

            if ("add".equalsIgnoreCase(action)) {
                addItem(request, response, user);
            } else if ("update".equalsIgnoreCase(action)) {
                updateItem(request, response, user);
            } else if ("remove".equalsIgnoreCase(action)) {
                removeItem(request, response, user);
            } else if ("clear".equalsIgnoreCase(action)) {
                clearCart(response, user);
            } else {
                sendError(response, "Invalid cart action");
            }

        } catch (IllegalArgumentException e) {
            sendError(response, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            sendError(response, "Cart operation failed");
        }
    }

    private void addItem(HttpServletRequest request,
                         HttpServletResponse response,
                         User user)
            throws Exception {

        long productId =
                parseLong(request.getParameter("productId"));

        int quantity =
                parseInt(request.getParameter("quantity"));

        cartService.addToCart(
                user.getId(),
                productId,
                quantity
        );

        sendSuccess(response, "Product added to cart");
    }

    private void updateItem(HttpServletRequest request,
                            HttpServletResponse response,
                            User user)
            throws Exception {

        long productId =
                parseLong(request.getParameter("productId"));

        int quantity =
                parseInt(request.getParameter("quantity"));

        cartService.updateQuantity(
                user.getId(),
                productId,
                quantity
        );

        sendSuccess(response, "Cart updated");
    }

    private void removeItem(HttpServletRequest request,
                            HttpServletResponse response,
                            User user)
            throws Exception {

        long productId =
                parseLong(request.getParameter("productId"));

        cartService.removeFromCart(
                user.getId(),
                productId
        );

        sendSuccess(response, "Product removed from cart");
    }

    private void clearCart(HttpServletResponse response,
                           User user)
            throws Exception {

        cartService.clearCart(user.getId());

        sendSuccess(response, "Cart cleared");
    }

    private User getLoggedUser(HttpServletRequest request) {

        HttpSession session = request.getSession(false);

        if (session == null) {
            throw new IllegalArgumentException("Login required");
        }

        User user = (User) session.getAttribute("user");

        if (user == null) {
            throw new IllegalArgumentException("Login required");
        }

        if (!"BUYER".equalsIgnoreCase(user.getRole())) {
            throw new IllegalArgumentException(
                    "Only buyers can use the cart"
            );
        }

        return user;
    }

    private long parseLong(String value) {

        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Product ID is required");
        }

        try {
            long number = Long.parseLong(value);

            if (number <= 0) {
                throw new IllegalArgumentException("Invalid product ID");
            }

            return number;

        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid product ID");
        }
    }

    private int parseInt(String value) {

        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Quantity is required");
        }

        try {
            int number = Integer.parseInt(value);

            if (number <= 0) {
                throw new IllegalArgumentException(
                        "Quantity must be greater than zero"
                );
            }

            return number;

        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid quantity");
        }
    }

    private void setJson(HttpServletResponse response) {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
    }

    private void sendSuccess(HttpServletResponse response,
                             String message)
            throws IOException {

        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", message);

        response.getWriter().write(gson.toJson(result));
    }

    private void sendError(HttpServletResponse response,
                           String message)
            throws IOException {

        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

        Map<String, Object> result = new HashMap<>();
        result.put("success", false);
        result.put(
                "message",
                message == null ? "Request failed" : message
        );

        response.getWriter().write(gson.toJson(result));
    }
          }
