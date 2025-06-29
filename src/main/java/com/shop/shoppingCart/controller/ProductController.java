package com.shop.shoppingCart.controller;


import com.shop.shoppingCart.model.Product;
import com.shop.shoppingCart.service.ProductService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/get")
    public List<Product> getProducts() throws Exception{
        return productService.getProducts();
    }
}
