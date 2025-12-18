package com.example.demo.repository;

import org.springframework.data.jpa.repository.jpa.repository.JpaRepository;
public interface ProductRepository extends JpaRepository<Product,Long>{
    
}