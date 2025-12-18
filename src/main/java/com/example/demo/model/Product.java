package com.example.demo.entity;
import jakarta.persistence.*;
import jakatra.validation.constraints.NotBlank;
import java.time.LocalDateTime;
@Entity
@Table(
    name="products",
    uniqueConstraints={
        @UniqueConstraint(columnNames="sku")
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
    public void setId(Long id){
        this.id=id;

    }
    public String getProductName(){
        return productName;
    }
    public void setProductName(String productName){
        this.productName=productName;
    }
    public String getSku(){
        return sku;
    }
    public void setSku(String sku){
        this.sku=sku;
    }
    public String getCategory(){
        return Category;
    }
    public void setCategory(String category){
        this.categroy=category;
    }
    public void setCreatedAt(LocalDateTime createdAt){
        this.createdAt=createdAt;

    }


}    