package com.shop.shoppingCart.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shop.shoppingCart.model.Product;
import com.shop.shoppingCart.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

    @Component
    public class ProductLoader implements CommandLineRunner {

        private final ProductRepository productRepository;
        private final ObjectMapper objectMapper;

        public ProductLoader(ProductRepository productRepository) {
            this.productRepository = productRepository;
            this.objectMapper = new ObjectMapper();
        }

        @Override
        public void run(String... args) throws Exception {
            if (productRepository.count() == 0) {
                InputStream inputStream = getClass().getResourceAsStream("/products.json");
                List<Product> products = Arrays.asList(objectMapper.readValue(inputStream, Product[].class));
                productRepository.saveAll(products);
                System.out.println("✅ Products loaded from JSON");
            }
        }
    }

