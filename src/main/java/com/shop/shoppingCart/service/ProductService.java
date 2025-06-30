package com.shop.shoppingCart.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shop.shoppingCart.model.Product;
import com.shop.shoppingCart.repository.ProductRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ObjectMapper objectMapper;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
        this.objectMapper = new ObjectMapper();
    }

    @PostConstruct
    public void loadProductsFromJson() {
        try {
            InputStream inputStream = new ClassPathResource("products.json").getInputStream();
            List<Product> products = objectMapper.readValue(inputStream, new TypeReference<List<Product>>() {});
            productRepository.saveAll(products);
            System.out.println("Products loaded successfully");
        } catch (Exception e) {
            System.err.println("Failed to load products: " + e.getMessage());
        }
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(Integer id) {
        return productRepository.findById(id).orElse(null);
    }
}
