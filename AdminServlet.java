package com.blackmart.blackmart.controller;

import com.blackmart.blackmart.model.Order;
import com.blackmart.blackmart.model.Product;
import com.blackmart.blackmart.model.User;
import com.blackmart.blackmart.dao.ProductDao;
import com.blackmart.blackmart.dao.UserDao;
import com.blackmart.blackmart.service.OrderService;
import com.blackmart.blackmart.service.ProductService;
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

@WebServlet("/api/admin")
public class AdminServlet extends HttpServlet {

    private final UserDao userDao = new UserDao();
    private final ProductDao productDao = new ProductDao();
    private final ProductService productService = new ProductService();
    private final OrderService orderService = new OrderService();
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        setJson(response);

        try {
            User admin = getAdmin(request);

            String action = request.getParameter("action");

            if ("users".equalsIgnoreCase(action)) {
                getUsers(response);
            } else if ("products".equalsIgnoreCase(action)) {
                getProducts(response);
            } else if ("orders".equalsIgnoreCase(action)) {
                getOrders(response);
            } else {
                sendError(response, "Invalid admin action");
            }

        } catch (IllegalArgumentException e) {
            sendError(response, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            sendError(response, "Admin operation failed");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request,
                           HttpServletResponse response)
            throws ServletException, IOException {

        setJson(response);

        try {
            User admin = getAdmin(request);

            String action = request.getParameter("action");

            if ("product-status".equalsIgnoreCase(action)) {
                updateProductStatus(request, response);
            } else if ("order-status".equalsIgnoreCase(action)) {
                updateOrderStatus(request, response);
            } else {
                sendError(response, "Invalid admin action");
            }

        } catch (IllegalArgumentException e) {
            sendError(response, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            sendError(response, "Admin operation failed");
        }
    }

    private void getUsers(HttpServletResponse response)
            throws Exception {

        List<User> users = userDao.findAll();

        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("users", users);

        response.getWriter().write(
                gson.toJson(result)
        );
    }

    private void getProducts(HttpServletResponse response)
            throws Exception {

        List<Product> products =
                productDao.findAll();

        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("products", products);

        response.getWriter().write(
                gson.toJson(result)
        );
    }

    private void getOrders(HttpServletResponse response)
            throws Exception {

        List<Order> orders =
                orderService.getAllOrders();

        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("orders", orders);

        response.getWriter().write(
                gson.toJson(result)
        );
    }

    private void updateProductStatus(HttpServletRequest request,
                                     HttpServletResponse response)
            throws Exception {

        long productId =
                parseId(request.getParameter("id"));

        String status =
                request.getParameter("status");

        if (status == null || status.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Product status is required"
            );
        }

        productService.updateStatus(
                productId,
                status
        );

        sendSuccess(
                response,
                "Product status updated"
        );
    }

    private void updateOrderStatus(HttpServletRequest request,
                                   HttpServletResponse response)
            throws Exception {

        long orderId =
                parseId(request.getParameter("id"));

        String status =
                request.getParameter("status");

        orderService.updateOrderStatus(
                orderId,
                status
        );

        sendSuccess(
                response,
                "Order status updated"
        );
    }

    private User getAdmin(HttpServletRequest request) {

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

        if (!"ADMIN".equalsIgnoreCase(user.getRole())) {
            throw new IllegalArgumentException(
                    "Admin access required"
            );
        }

        return user;
    }

    private long parseId(String value) {

        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "ID is required"
            );
        }

        try {
            long id = Long.parseLong(value);

            if (id <= 0) {
                throw new IllegalArgumentException(
                        "Invalid ID"
                );
            }

            return id;

        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "Invalid ID"
            );
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

        response.getWriter().write(
                gson.toJson(result)
        );
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
}
