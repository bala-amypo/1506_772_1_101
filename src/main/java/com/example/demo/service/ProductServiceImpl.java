package com.example.demo.service.impl;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Product;
import com.example.demo.repository.ProductRepository;
import com.example.demo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    
    private final ProductRepository productRepository;
    
    @Autowired
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
    
    @Override
    @Transactional
    public Product createProduct(Product product) {
        if (product.getProductName() == null || product.getProductName().trim().isEmpty()) {
            throw new IllegalArgumentException("Product name must not be empty");
        }
        
        if (productRepository.findBySku(product.getSku()).isPresent()) {
            throw new IllegalArgumentException("SKU must be unique");
        }
        
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







package com.example.demo.service.impl;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.ConsumptionLog;
import com.example.demo.model.PredictionRule;
import com.example.demo.model.StockRecord;
import com.example.demo.repository.ConsumptionLogRepository;
import com.example.demo.repository.PredictionRuleRepository;
import com.example.demo.repository.StockRecordRepository;
import com.example.demo.service.PredictionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class PredictionServiceImpl implements PredictionService {
    
    private final PredictionRuleRepository predictionRuleRepository;
    private final StockRecordRepository stockRecordRepository;
    private final ConsumptionLogRepository consumptionLogRepository;
    
    @Autowired
    public PredictionServiceImpl(
            PredictionRuleRepository predictionRuleRepository,
            StockRecordRepository stockRecordRepository,
            ConsumptionLogRepository consumptionLogRepository) {
        this.predictionRuleRepository = predictionRuleRepository;
        this.stockRecordRepository = stockRecordRepository;
        this.consumptionLogRepository = consumptionLogRepository;
    }
    
    @Override
    @Transactional
    public PredictionRule createRule(PredictionRule rule) {
        if (rule.getAverageDaysWindow() <= 0) {
            throw new IllegalArgumentException("Average days window must be greater than zero");
        }
        
        if (rule.getMinDailyUsage() > rule.getMaxDailyUsage()) {
            throw new IllegalArgumentException("Minimum daily usage must be less than or equal to maximum daily usage");
        }
        
        if (predictionRuleRepository.findByRuleName(rule.getRuleName()).isPresent()) {
            throw new IllegalArgumentException("Rule name must be unique");
        }
        
        return predictionRuleRepository.save(rule);
    }
    
    @Override
    public List<PredictionRule> getAllRules() {
        return predictionRuleRepository.findAll();
    }
    
    @Override
    public LocalDate predictRestockDate(Long stockRecordId) {
        StockRecord stockRecord = stockRecordRepository.findById(stockRecordId)
                .orElseThrow(() -> new ResourceNotFoundException("StockRecord not found"));
        
        List<ConsumptionLog> logs = consumptionLogRepository.findByStockRecordId(stockRecordId);
        
        if (logs.isEmpty()) {
            throw new IllegalArgumentException("No consumption logs available for prediction");
        }
        
        // Get default rule or first rule
        PredictionRule rule = predictionRuleRepository.findAll().stream()
                .findFirst()
                .orElse(PredictionRule.builder()
                        .averageDaysWindow(30)
                        .minDailyUsage(1)
                        .maxDailyUsage(100)
                        .build());
        
        // Calculate average daily consumption
        double totalConsumption = logs.stream()
                .mapToInt(ConsumptionLog::getConsumedQuantity)
                .sum();
        
        long daysBetween = ChronoUnit.DAYS.between(
                logs.get(0).getConsumedDate(),
                logs.get(logs.size() - 1).getConsumedDate()
        );
        
        if (daysBetween == 0) daysBetween = 1;
        
        double averageDailyConsumption = totalConsumption / daysBetween;
        
        // Calculate days until reorder threshold
        int remainingQuantity = stockRecord.getCurrentQuantity() - stockRecord.getReorderThreshold();
        if (remainingQuantity <= 0) {
            return LocalDate.now(); // Already at or below threshold
        }
        
        double daysUntilReorder = remainingQuantity / averageDailyConsumption;
        
        return LocalDate.now().plusDays((long) Math.ceil(daysUntilReorder));
    }
}
package com.example.demo.service.impl;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.ConsumptionLog;
import com.example.demo.model.PredictionRule;
import com.example.demo.model.StockRecord;
import com.example.demo.repository.ConsumptionLogRepository;
import com.example.demo.repository.PredictionRuleRepository;
import com.example.demo.repository.StockRecordRepository;
import com.example.demo.service.PredictionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class PredictionServiceImpl implements PredictionService {
    
    private final PredictionRuleRepository predictionRuleRepository;
    private final StockRecordRepository stockRecordRepository;
    private final ConsumptionLogRepository consumptionLogRepository;
    
    @Autowired
    public PredictionServiceImpl(
            PredictionRuleRepository predictionRuleRepository,
            StockRecordRepository stockRecordRepository,
            ConsumptionLogRepository consumptionLogRepository) {
        this.predictionRuleRepository = predictionRuleRepository;
        this.stockRecordRepository = stockRecordRepository;
        this.consumptionLogRepository = consumptionLogRepository;
    }
    
    @Override
    @Transactional
    public PredictionRule createRule(PredictionRule rule) {
        if (rule.getAverageDaysWindow() <= 0) {
            throw new IllegalArgumentException("Average days window must be greater than zero");
        }
        
        if (rule.getMinDailyUsage() > rule.getMaxDailyUsage()) {
            throw new IllegalArgumentException("Minimum daily usage must be less than or equal to maximum daily usage");
        }
        
        if (predictionRuleRepository.findByRuleName(rule.getRuleName()).isPresent()) {
            throw new IllegalArgumentException("Rule name must be unique");
        }
        
        return predictionRuleRepository.save(rule);
    }
    
    @Override
    public List<PredictionRule> getAllRules() {
        return predictionRuleRepository.findAll();
    }
    
    @Override
    public LocalDate predictRestockDate(Long stockRecordId) {
        StockRecord stockRecord = stockRecordRepository.findById(stockRecordId)
                .orElseThrow(() -> new ResourceNotFoundException("StockRecord not found"));
        
        List<ConsumptionLog> logs = consumptionLogRepository.findByStockRecordId(stockRecordId);
        
        if (logs.isEmpty()) {
            throw new IllegalArgumentException("No consumption logs available for prediction");
        }
        
        // Get default rule or first rule
        PredictionRule rule = predictionRuleRepository.findAll().stream()
                .findFirst()
                .orElse(PredictionRule.builder()
                        .averageDaysWindow(30)
                        .minDailyUsage(1)
                        .maxDailyUsage(100)
                        .build());
        
        // Calculate average daily consumption
        double totalConsumption = logs.stream()
                .mapToInt(ConsumptionLog::getConsumedQuantity)
                .sum();
        
        long daysBetween = ChronoUnit.DAYS.between(
                logs.get(0).getConsumedDate(),
                logs.get(logs.size() - 1).getConsumedDate()
        );
        
        if (daysBetween == 0) daysBetween = 1;
        
        double averageDailyConsumption = totalConsumption / daysBetween;
        
        // Calculate days until reorder threshold
        int remainingQuantity = stockRecord.getCurrentQuantity() - stockRecord.getReorderThreshold();
        if (remainingQuantity <= 0) {
            return LocalDate.now(); // Already at or below threshold
        }
        
        double daysUntilReorder = remainingQuantity / averageDailyConsumption;
        
        return LocalDate.now().plusDays((long) Math.ceil(daysUntilReorder));
    }
}

package com.example.demo.service.impl;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.JwtProvider;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    
    @Autowired
    public UserServiceImpl(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            JwtProvider jwtProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtProvider = jwtProvider;
    }
    
    @Override
    @Transactional
    public User register(User user) {
        if (user.getName() == null || user.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Name must not be empty");
        }
        
        if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            throw new IllegalArgumentException("Password must not be empty");
        }
        
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }
        
        // Set default role if none provided
        if (user.getRoles() == null || user.getRoles().isEmpty()) {
            user.getRoles().add("ROLE_USER");
        }
        
        // Encode password
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        
        return userRepository.save(user);
    }
    
    @Override
    public Map<String, Object> login(String email, String password) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
            );
            
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found"));
            
            String token = jwtProvider.generateToken(user.getId(), user.getEmail(), user.getRoles());
            
            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("userId", user.getId());
            response.put("email", user.getEmail());
            response.put("roles", user.getRoles());
            
            return response;
        } catch (Exception e) {
            throw new ResourceNotFoundException("Invalid email or password");
        }
    }
    
    @Override
    public User getByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }
}