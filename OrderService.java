package com.blackmart.blackmart.service;

import com.blackmart.blackmart.dao.CartDao;
import com.blackmart.blackmart.dao.OrderDao;
import com.blackmart.blackmart.dao.ProductDao;
import com.blackmart.blackmart.dao.OrderItemDao;
import com.blackmart.blackmart.model.CartItem;
import com.blackmart.blackmart.model.Order;
import com.blackmart.blackmart.model.OrderItem;
import com.blackmart.blackmart.model.Product;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class OrderService {

    private final CartDao cartDao;
    private final ProductDao productDao;
    private final OrderDao orderDao;
    private final OrderItemDao orderItemDao;

    public OrderService() {
        this.cartDao = new CartDao();
        this.productDao = new ProductDao();
        this.orderDao = new OrderDao();
        this.orderItemDao = new OrderItemDao();
    }

    public long checkout(long buyerId, String shippingAddress) throws Exception {

        if (buyerId <= 0) {
            throw new IllegalArgumentException("Invalid buyer");
        }

        if (shippingAddress == null || shippingAddress.trim().isEmpty()) {
            throw new IllegalArgumentException("Shipping address is required");
        }

        List<CartItem> cartItems = cartDao.findByBuyer(buyerId);

        if (cartItems.isEmpty()) {
            throw new IllegalArgumentException("Cart is empty");
        }

        List<OrderItem> orderItems = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        for (CartItem cartItem : cartItems) {

            Product product = productDao.findById(cartItem.getProductId());

            if (product == null) {
                throw new IllegalArgumentException("Product not found");
            }

            if (!"ACTIVE".equalsIgnoreCase(product.getStatus())) {
                throw new IllegalArgumentException(
                        "Product is not available: " + product.getName()
                );
            }

            if (cartItem.getQuantity() <= 0) {
                throw new IllegalArgumentException("Invalid quantity");
            }

            if (cartItem.getQuantity() > product.getStock()) {
                throw new IllegalArgumentException(
                        "Insufficient stock for: " + product.getName()
                );
            }

            BigDecimal itemTotal = product.getPrice()
                    .multiply(BigDecimal.valueOf(cartItem.getQuantity()));

            total = total.add(itemTotal);

            OrderItem orderItem = new OrderItem();

            orderItem.setProductId(product.getId());
            orderItem.setSellerId(product.getSellerId());
            orderItem.setQuantity(cartItem.getQuantity());

            // Save the current product price in the order.
            orderItem.setPrice(product.getPrice());

            orderItems.add(orderItem);
        }

        Order order = new Order();

        order.setBuyerId(buyerId);
        order.setTotalAmount(total);
        order.setStatus("PLACED");
        order.setPaymentStatus("PAID");
        order.setShippingAddress(shippingAddress.trim());

        long orderId = orderDao.createOrder(order, orderItems);

        cartDao.clearCart(buyerId);

        return orderId;
    }

    public Order getOrder(long orderId) throws Exception {

        if (orderId <= 0) {
            throw new IllegalArgumentException("Invalid order ID");
        }

        return orderDao.findById(orderId);
    }

    public List<Order> getBuyerOrders(long buyerId) throws Exception {

        if (buyerId <= 0) {
            throw new IllegalArgumentException("Invalid buyer");
        }

        return orderDao.findByBuyer(buyerId);
    }

    public List<Order> getAllOrders() throws Exception {
        return orderDao.findAll();
    }

    public List<OrderItem> getOrderItems(long orderId) throws Exception {

        if (orderId <= 0) {
            throw new IllegalArgumentException("Invalid order ID");
        }

        return orderItemDao.findByOrderId(orderId);
    }

    public List<OrderItem> getSellerOrders(long sellerId) throws Exception {

        if (sellerId <= 0) {
            throw new IllegalArgumentException("Invalid seller");
        }

        return orderItemDao.findBySeller(sellerId);
    }

    public void updateOrderStatus(long orderId, String status) throws Exception {

        if (orderId <= 0) {
            throw new IllegalArgumentException("Invalid order ID");
        }

        if (status == null || status.trim().isEmpty()) {
            throw new IllegalArgumentException("Order status is required");
        }

        String normalizedStatus = status.trim().toUpperCase();

        if (!isValidStatus(normalizedStatus)) {
            throw new IllegalArgumentException("Invalid order status");
        }

        orderDao.updateStatus(orderId, normalizedStatus);
    }

    private boolean isValidStatus(String status) {

        return status.equals("PLACED")
                || status.equals("CONFIRMED")
                || status.equals("SHIPPED")
                || status.equals("DELIVERED")
                || status.equals("CANCELLED");
    }
}
