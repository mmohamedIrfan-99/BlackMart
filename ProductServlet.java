package com.blackmart.blackmart.controller;

import com.blackmart.blackmart.model.Product;
import com.blackmart.blackmart.model.User;
import com.blackmart.blackmart.service.ProductService;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/api/products")
public class ProductServlet extends HttpServlet {

    private final ProductService productService = new ProductService();
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            String action = request.getParameter("action");

            if ("browse".equalsIgnoreCase(action)) {
                browse(request, response);
            } else if ("my-products".equalsIgnoreCase(action)) {
                myProducts(request, response);
            } else if ("get".equalsIgnoreCase(action)) {
                getProduct(request, response);
            } else {
                browse(request, response);
            }

        } catch (Exception e) {
            e.printStackTrace();
            sendError(response, e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            String action = request.getParameter("action");

            if ("create".equalsIgnoreCase(action)) {
                createProduct(request, response);
            } else if ("update".equalsIgnoreCase(action)) {
                updateProduct(request, response);
            } else if ("delete".equalsIgnoreCase(action)) {
                deleteProduct(request, response);
            } else if ("status".equalsIgnoreCase(action)) {
                updateStatus(request, response);
            } else {
                sendError(response, "Invalid product action");
            }

        } catch (IllegalArgumentException e) {
            sendError(response, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            sendError(response, "Something went wrong");
        }
    }

    private void browse(HttpServletRequest request,
                        HttpServletResponse response)
            throws Exception {

        String search = request.getParameter("search");
        String category = request.getParameter("category");

        List<Product> products =
                productService.browseProducts(search, category);

        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("products", products);

        response.getWriter().write(gson.toJson(result));
    }

    private void getProduct(HttpServletRequest request,
                            HttpServletResponse response)
            throws Exception {

        long id = parseId(request.getParameter("id"));

        Product product = productService.findById(id);

        if (product == null) {
            sendNotFound(response, "Product not found");
            return;
        }

        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("product", product);

        response.getWriter().write(gson.toJson(result));
    }

    private void myProducts(HttpServletRequest request,
                            HttpServletResponse response)
            throws Exception {

        User user = getLoggedUser(request);

        if (!"SELLER".equalsIgnoreCase(user.getRole())) {
            sendForbidden(response);
            return;
        }

        List<Product> products =
                productService.getSellerProducts(user.getId());

        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("products", products);

        response.getWriter().write(gson.toJson(result));
    }

    private void createProduct(HttpServletRequest request,
                               HttpServletResponse response)
            throws Exception {

        User user = getLoggedUser(request);

        if (!"SELLER".equalsIgnoreCase(user.getRole())) {
            sendForbidden(response);
            return;
        }

        Product product = buildProduct(request);

        product.setSellerId(user.getId());

        Product created =
                productService.createProduct(product);

        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", "Product created successfully");
        result.put("product", created);

        response.getWriter().write(gson.toJson(result));
    }

    private void updateProduct(HttpServletRequest request,
                               HttpServletResponse response)
            throws Exception {

        User user = getLoggedUser(request);

        if (!"SELLER".equalsIgnoreCase(user.getRole())) {
            sendForbidden(response);
            return;
        }

        long id = parseId(request.getParameter("id"));

        Product existing = productService.findById(id);

        if (existing == null) {
            sendNotFound(response, "Product not found");
            return;
        }

        if (existing.getSellerId() != user.getId()) {
            sendForbidden(response);
            return;
        }

        Product product = buildProduct(request);

        product.setId(id);
        product.setSellerId(user.getId());
        product.setStatus(existing.getStatus());

        productService.updateProduct(product);

        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", "Product updated successfully");

        response.getWriter().write(gson.toJson(result));
    }

    private void deleteProduct(HttpServletRequest request,
                               HttpServletResponse response)
            throws Exception {

        User user = getLoggedUser(request);

        if (!"SELLER".equalsIgnoreCase(user.getRole())) {
            sendForbidden(response);
            return;
        }

        long id = parseId(request.getParameter("id"));

        Product existing = productService.findById(id);

        if (existing == null) {
            sendNotFound(response, "Product not found");
            return;
        }

        if (existing.getSellerId() != user.getId()) {
            sendForbidden(response);
            return;
        }

        productService.deleteProduct(id, user.getId());

        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", "Product deleted successfully");

        response.getWriter().write(gson.toJson(result));
    }

    private void updateStatus(HttpServletRequest request,
                              HttpServletResponse response)
            throws Exception {

        User user = getLoggedUser(request);

        if (!"ADMIN".equalsIgnoreCase(user.getRole())) {
            sendForbidden(response);
            return;
        }

        long id = parseId(request.getParameter("id"));
        String status = request.getParameter("status");

        productService.updateStatus(id, status);

        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", "Product status updated");

        response.getWriter().write(gson.toJson(result));
    }

    private Product buildProduct(HttpServletRequest request) {

        String name = request.getParameter("name");
        String description = request.getParameter("description");
        String category = request.getParameter("category");
        String priceText = request.getParameter("price");
        String stockText = request.getParameter("stock");
        String imageUrl = request.getParameter("imageUrl");

        if (priceText == null || stockText == null) {
            throw new IllegalArgumentException(
                    "Price and stock are required"
            );
        }

        BigDecimal price;

        try {
            price = new BigDecimal(priceText);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid price");
        }

        int stock;

        try {
            stock = Integer.parseInt(stockText);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid stock");
        }

        Product product = new Product();

        product.setName(name);
        product.setDescription(description);
        product.setCategory(category);
        product.setPrice(price);
        product.setStock(stock);
        product.setImageUrl(imageUrl);

        return product;
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

        return user;
    }

    private long parseId(String value) {

        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("ID is required");
        }

        try {
            long id = Long.parseLong(value);

            if (id <= 0) {
                throw new IllegalArgumentException("Invalid ID");
            }

            return id;

        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid ID");
        }
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

    private void sendNotFound(HttpServletResponse response,
                              String message)
            throws IOException {

        response.setStatus(HttpServletResponse.SC_NOT_FOUND);

        Map<String, Object> result = new HashMap<>();
        result.put("success", false);
        result.put("message", message);

        response.getWriter().write(gson.toJson(result));
    }

    private void sendForbidden(HttpServletResponse response)
            throws IOException {

        response.setStatus(HttpServletResponse.SC_FORBIDDEN);

        Map<String, Object> result = new HashMap<>();
        result.put("success", false);
        result.put("message", "Access denied");

        response.getWriter().write(gson.toJson(result));
    }
                  }
