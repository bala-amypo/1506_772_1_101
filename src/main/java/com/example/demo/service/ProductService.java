package com.example.demo.service;

import com.example.demo.model.Product;
import java.util.List;

public interface ProductService {
    Product createProduct(Product product);
    Product getProduct(Long id);
    List<Product> getAllProducts();
}




package com.example.demo.service;

import com.example.demo.model.PredictionRule;
import java.time.LocalDate;
import java.util.List;

public interface PredictionService {
    LocalDate predictRestockDate(Long stockRecordId);
    List<PredictionRule> getAllRules();
    PredictionRule createRule(PredictionRule rule);
}

package com.example.demo.service;

import com.example.demo.model.User;
import java.util.Map;

public interface UserService {
    User register(User user);
    Map<String, Object> login(String email, String password);
    User getByEmail(String email);
}