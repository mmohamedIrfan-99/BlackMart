package com.blackmart.blackmart.service;

import com.blackmart.blackmart.dao.CartDao;
import com.blackmart.blackmart.dao.ProductDao;
import com.blackmart.blackmart.model.CartItem;
import com.blackmart.blackmart.model.Product;

import java.util.List;

public class CartService {

    private final CartDao cartDao;
    private final ProductDao productDao;

    public CartService() {
        this.cartDao = new CartDao();
        this.productDao = new ProductDao();
    }

    public void addToCart(Long buyerId,
                           Long productId,
                           int quantity) throws Exception {

        if (quantity <= 0) {
            throw new IllegalArgumentException(
                    "Quantity must be greater than zero."
            );
        }

        Product product = productDao.findById(productId);

        if (product == null || !"ACTIVE".equals(product.getStatus())) {
            throw new IllegalArgumentException(
                    "Product is not available."
            );
        }

        if (product.getStock() < quantity) {
            throw new IllegalArgumentException(
                    "Insufficient stock."
            );
        }

        CartItem existing =
                cartDao.findItem(buyerId, productId);

        if (existing != null) {

            int newQuantity =
                    existing.getQuantity() + quantity;

            if (newQuantity > product.getStock()) {
                throw new IllegalArgumentException(
                        "Requested quantity exceeds available stock."
                );
            }

            cartDao.updateQuantity(
                    buyerId,
                    productId,
                    newQuantity
            );

        } else {

            cartDao.addItem(
                    buyerId,
                    productId,
                    quantity
            );
        }
    }

    public List<CartItem> getCart(Long buyerId)
            throws Exception {

        return cartDao.findByBuyer(buyerId);
    }

    public void updateQuantity(Long buyerId,
                               Long productId,
                               int quantity)
            throws Exception {

        if (quantity <= 0) {
            throw new IllegalArgumentException(
                    "Quantity must be greater than zero."
            );
        }

        Product product = productDao.findById(productId);

        if (product == null) {
            throw new IllegalArgumentException(
                    "Product not found."
            );
        }

        if (quantity > product.getStock()) {
            throw new IllegalArgumentException(
                    "Quantity exceeds available stock."
            );
        }

        boolean updated = cartDao.updateQuantity(
                buyerId,
                productId,
                quantity
        );

        if (!updated) {
            throw new IllegalArgumentException(
                    "Cart item not found."
            );
        }
    }

    public void removeFromCart(Long buyerId,
                               Long productId)
            throws Exception {

        boolean removed =
                cartDao.removeItem(buyerId, productId);

        if (!removed) {
            throw new IllegalArgumentException(
                    "Cart item not found."
            );
        }
    }

    public void clearCart(Long buyerId)
            throws Exception {

        cartDao.clearCart(buyerId);
    }
}
