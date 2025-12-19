package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.demo.model.Product;
import com.example.demo.service.ProductService;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/products")
@Tag(name = "Product API")
public class ProductController {

    @Autowired
    ProductService service;

    @PostMapping
    public Product create(@RequestBody Product product) {
        return service.createProduct(product);
    }

    @GetMapping
    public List<Product> getAll() {
        return service.getAllProducts();
    }

    @GetMapping("/{id}")
    public Product get(@PathVariable Long id) {
        return service.getProduct(id);
    }
}
