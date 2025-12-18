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
    @NotBlank(message="Product name must not be empty")
    @Column(nullable=false)
    private String productName;
    @Column(nullable=false,unique=true)
    private String sku;
    private String category;
    private LocalDateTime createdAt;
    pubilc Product(){
        this.createdAt=LocalDateTime.now();
    }
    public Long getId(){
        return id;
    }
    public void setId
    

}    