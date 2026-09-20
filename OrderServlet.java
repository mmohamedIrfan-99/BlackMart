package com.blackmart.blackmart.controller;

import com.blackmart.blackmart.model.Order;
import com.blackmart.blackmart.model.OrderItem;
import com.blackmart.blackmart.model.User;
import com.blackmart.blackmart.service.OrderService;
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

@WebServlet("/api/orders")
public class OrderServlet extends HttpServlet {

    private final OrderService orderService = new OrderService();
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        setJson(response);

        try {
            User user = getLoggedUser(request);
            String action = request.getParameter("action");

            if ("my-orders".equalsIgnoreCase(action)) {
                getMyOrders(response, user);
            } else if ("items".equalsIgnoreCase(action)) {
                getOrderItems(request, response, user);
            } else if ("seller-orders".equalsIgnoreCase(action)) {
                getSellerOrders(response, user);
            } else if ("all".equalsIgnoreCase(action)) {
                getAllOrders(response, user);
            } else {
                getOrder(request, response, user);
            }

        } catch (IllegalArgumentException e) {
            sendError(response, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            sendError(response, "Unable to load orders");
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

            if ("checkout".equalsIgnoreCase(action)) {
                checkout(request, response, user);
            } else if ("status".equalsIgnoreCase(action)) {
                updateStatus(request, response, user);
            } else {
                sendError(response, "Invalid order action");
            }

        } catch (IllegalArgumentException e) {
            sendError(response, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            sendError(response, "Order operation failed");
        }
    }

    private void checkout(HttpServletRequest request,
                          HttpServletResponse response,
                          User user)
            throws Exception {

        if (!"BUYER".equalsIgnoreCase(user.getRole())) {
            sendForbidden(response);
            return;
        }

        String address = request.getParameter("shippingAddress");

        long orderId =
                orderService.checkout(
                        user.getId(),
                        address
                );

        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", "Order placed successfully");
        result.put("orderId", orderId);

        response.getWriter().write(gson.toJson(result));
    }

    private void getMyOrders(HttpServletResponse response,
                             User user)
            throws Exception {

        if (!"BUYER".equalsIgnoreCase(user.getRole())) {
            sendForbidden(response);
            return;
        }

        List<Order> orders =
                orderService.getBuyerOrders(user.getId());

        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("orders", orders);

        response.getWriter().write(gson.toJson(result));
    }

    private void getOrder(HttpServletRequest request,
                          HttpServletResponse response,
                          User user)
            throws Exception {

        long orderId =
                parseId(request.getParameter("id"));

        Order order = orderService.getOrder(orderId);

        if (order == null) {
            sendNotFound(response, "Order not found");
            return;
        }

        if ("BUYER".equalsIgnoreCase(user.getRole())
                && order.getBuyerId() != user.getId()) {

            sendForbidden(response);
            return;
        }

        List<OrderItem> items =
                orderService.getOrderItems(orderId);

        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("order", order);
        result.put("items", items);

        response.getWriter().write(gson.toJson(result));
    }

    private void getOrderItems(HttpServletRequest request,
                               HttpServletResponse response,
                               User user)
            throws Exception {

        long orderId =
                parseId(request.getParameter("id"));

        Order order = orderService.getOrder(orderId);

        if (order == null) {
            sendNotFound(response, "Order not found");
            return;
        }

        if ("BUYER".equalsIgnoreCase(user.getRole())
                && order.getBuyerId() != user.getId()) {

            sendForbidden(response);
            return;
        }

        List<OrderItem> items =
                orderService.getOrderItems(orderId);

        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("items", items);

        response.getWriter().write(gson.toJson(result));
    }

    private void getSellerOrders(HttpServletResponse response,
                                 User user)
            throws Exception {

        if (!"SELLER".equalsIgnoreCase(user.getRole())) {
            sendForbidden(response);
            return;
        }

        List<OrderItem> items =
                orderService.getSellerOrders(user.getId());

        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("items", items);

        response.getWriter().write(gson.toJson(result));
    }

    private void getAllOrders(HttpServletResponse response,
                              User user)
            throws Exception {

        if (!"ADMIN".equalsIgnoreCase(user.getRole())) {
            sendForbidden(response);
            return;
        }

        List<Order> orders =
                orderService.getAllOrders();

        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("orders", orders);

        response.getWriter().write(gson.toJson(result));
    }

    private void updateStatus(HttpServletRequest request,
                              HttpServletResponse response,
                              User user)
            throws Exception {

        if (!"ADMIN".equalsIgnoreCase(user.getRole())) {
            sendForbidden(response);
            return;
        }

        long orderId =
                parseId(request.getParameter("id"));

        String status =
                request.getParameter("status");

        orderService.updateOrderStatus(
                orderId,
                status
        );

        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", "Order status updated");

        response.getWriter().write(gson.toJson(result));
    }

    private User getLoggedUser(HttpServletRequest request) {

        HttpSession session =
                request.getSession(false);

        if (session == null) {
            throw new IllegalArgumentException(
                    "Login required"
            );
        }

        User user =
                (User) session.getAttribute("user");

        if (user == null) {
            throw new IllegalArgumentException(
                    "Login required"
            );
        }

        return user;
    }

    private long parseId(String value) {

        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Order ID is required"
            );
        }

        try {
            long id = Long.parseLong(value);

            if (id <= 0) {
                throw new IllegalArgumentException(
                        "Invalid order ID"
                );
            }

            return id;

        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "Invalid order ID"
            );
        }
    }

    private void setJson(HttpServletResponse response) {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
    }

    private void sendError(HttpServletResponse response,
                           String message)
            throws IOException {

        response.setStatus(
                HttpServletResponse.SC_BAD_REQUEST
        );

        Map<String, Object> result = new HashMap<>();
        result.put("success", false);
        result.put(
                "message",
                message == null
                        ? "Request failed"
                        : message
        );

        response.getWriter().write(
                gson.toJson(result)
        );
    }

    private void sendNotFound(HttpServletResponse response,
                              String message)
            throws IOException {

        response.setStatus(
                HttpServletResponse.SC_NOT_FOUND
        );

        Map<String, Object> result = new HashMap<>();
        result.put("success", false);
        result.put("message", message);

        response.getWriter().write(
                gson.toJson(result)
        );
    }

    private void sendForbidden(HttpServletResponse response)
            throws IOException {

        response.setStatus(
                HttpServletResponse.SC_FORBIDDEN
        );

        Map<String, Object> result = new HashMap<>();
        result.put("success", false);
        result.put("message", "Access denied");

        response.getWriter().write(
                gson.toJson(result)
        );
    }
                                             }
