package com.blackmart.blackmart.service;

import com.blackmart.blackmart.dao.ProductDao;
import com.blackmart.blackmart.model.Product;

import java.math.BigDecimal;
import java.util.List;

public class ProductService {

    private final ProductDao productDao;

    public ProductService() {
        this.productDao = new ProductDao();
    }

    public long createProduct(Long sellerId,
                              String name,
                              String description,
                              String category,
                              BigDecimal price,
                              int stock,
                              String imageUrl) throws Exception {

        validateProduct(name, category, price, stock);

        Product product = new Product(
                sellerId,
                name.trim(),
                description == null ? "" : description.trim(),
                category.trim(),
                price,
                stock,
                imageUrl == null ? "" : imageUrl.trim()
        );

        return productDao.create(product);
    }

    public boolean updateProduct(Long sellerId,
                                 Long productId,
                                 String name,
                                 String description,
                                 String category,
                                 BigDecimal price,
                                 int stock,
                                 String imageUrl) throws Exception {

        validateProduct(name, category, price, stock);

        Product product = new Product(
                productId,
                sellerId,
                name.trim(),
                description == null ? "" : description.trim(),
                category.trim(),
                price,
                stock,
                imageUrl == null ? "" : imageUrl.trim(),
                "ACTIVE",
                null
        );

        return productDao.update(product);
    }

    public boolean deleteProduct(Long sellerId,
                                 Long productId) throws Exception {

        return productDao.delete(productId, sellerId);
    }

    public Product findById(Long productId) throws Exception {

        if (productId == null || productId <= 0) {
            throw new IllegalArgumentException(
                    "Invalid product ID."
            );
        }

        return productDao.findById(productId);
    }

    public List<Product> browseProducts(String search,
                                        String category)
            throws Exception {

        return productDao.findActiveProducts(
                search == null ? "" : search.trim(),
                category == null ? "" : category.trim()
        );
    }

    public List<Product> getSellerProducts(Long sellerId)
            throws Exception {

        return productDao.findBySeller(sellerId);
    }

    public boolean updateStatus(Long productId,
                                String status)
            throws Exception {

        if (!"ACTIVE".equals(status)
                && !"BLOCKED".equals(status)
                && !"PENDING".equals(status)) {

            throw new IllegalArgumentException(
                    "Invalid product status."
            );
        }

        return productDao.updateStatus(productId, status);
    }

    private void validateProduct(String name,
                                 String category,
                                 BigDecimal price,
                                 int stock) {

        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException(
                    "Product name is required."
            );
        }

        if (category == null || category.isBlank()) {
            throw new IllegalArgumentException(
                    "Product category is required."
            );
        }

        if (price == null || price.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(
                    "Price must be greater than zero."
            );
        }

        if (stock < 0) {
            throw new IllegalArgumentException(
                    "Stock cannot be negative."
            );
        }
    }
          }
