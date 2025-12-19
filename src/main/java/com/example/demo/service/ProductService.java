package com.example.demo.service;

import java.util.List;
import com.example.demo.model.Product;

public interface ProductService {
    Product createProduct(Product product);
    Product getProduct(Long id);
    List<Product> getAllProducts();
}
