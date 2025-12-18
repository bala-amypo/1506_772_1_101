package com.example.demo.entity;
import jakarta.persistence.*;
import jakatra.validation.constraints.NotBlank;
import java.time.LocalDateTime;
@Entity
@Table(
    name="products",
    uniqueConstraints={
        @UniqueConstraint(colmunNmaes="sku")
    }
)
public class Product{
    @Id
    GeneratedValue(strategy=GenerationType.IDENTITY)
}