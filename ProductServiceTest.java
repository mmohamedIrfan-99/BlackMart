package com.blackmart.blackmart.service;

import com.blackmart.blackmart.model.Product;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProductServiceTest {

    @Test
    void productObjectShouldStoreValuesCorrectly() {
        Product product = new Product();

        product.setName("Shoes");
        product.setDescription("Running shoes");
        product.setPrice(1500.00);
        product.setCategory("Fashion");
        product.setStock(10);

        assertEquals("Shoes", product.getName());
        assertEquals("Running shoes", product.getDescription());
        assertEquals(1500.00, product.getPrice());
        assertEquals("Fashion", product.getCategory());
        assertEquals(10, product.getStock());
    }
}
