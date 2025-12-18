package com.example.demo.entity;
import jakarta.persistence.*;
import jakatra.validation.constraints.NotBlank;
import java.time.LocalDateTime;
@Entity
@Table(
    name="products",
    uniqueConstraints={
        @UniqueConstraint(colmunNames="sku")
    }
)
public class Product{
    @Id
    GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message="product name must not be empty")
    @Column(nullable=false)
    private String productName;
    @Column(nullable=false,unique=true)
}    