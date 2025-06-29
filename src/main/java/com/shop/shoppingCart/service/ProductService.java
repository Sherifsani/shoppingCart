package com.shop.shoppingCart.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shop.shoppingCart.model.Product;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

@Service
public class ProductService {
    public List<Product> getProducts() {
        try {
            System.out.println("Loading products.json from classpath...");

            ObjectMapper mapper = new ObjectMapper();
            InputStream inputStream = new ClassPathResource("products.json").getInputStream();
            List<Product> products = Arrays.asList(mapper.readValue(inputStream, Product[].class));

            System.out.println(" Loaded " + products.size() + " products.");
            return products;

        } catch (Exception e) {
            System.err.println("❌ Failed to load products.json: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Could not load product data", e);
        }
    }

}
