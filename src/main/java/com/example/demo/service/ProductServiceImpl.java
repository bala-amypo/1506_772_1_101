package com.example.demo.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Product;
import com.example.demo.repository.ProductRepository;
import com.example.demo.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public Product createProduct(Product product) {

        if (product.getProductName() == null || product.getProductName().isEmpty()) {
            throw new IllegalArgumentException("productName must not be blank");
        }

        productRepository.findBySku(product.getSku())
                .ifPresent(p -> {
                    throw new IllegalArgumentException("SKU already exists");
                });

        product.setCreatedAt(LocalDateTime.now());
        return productRepository.save(product);
    }

    @Override
    public Product getProduct(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }
}
