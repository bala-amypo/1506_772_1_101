package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.demo.model.Product;
import com.example.demo.service.ProductService;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/products")
@Tag(name = "Product Controller", description = "Product Management APIs")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping("/")
    public Product create(@RequestBody Product product) {
        return productService.createProduct(product);
    }

    @GetMapping("/")
    public List<Product> getAll() {
        return productService.getAllProducts();
    }

    @GetMapping("/{id}")
    public Product getById(@PathVariable Long id) {
        return productService.getProduct(id);
    }
}