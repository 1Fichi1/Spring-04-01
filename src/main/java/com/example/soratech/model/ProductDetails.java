package com.example.soratech.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "product_details")
public class ProductDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    @JsonIgnoreProperties({"details", "category", "manufacturer", "suppliers", "orderItems", "reviews"})
    private Product product;

    @NotBlank(message = "Название характеристики не может быть пустым")
    @Size(min = 2, max = 100, message = "Название характеристики должно быть от 2 до 100 символов")
    @Column(nullable = false)
    private String characteristicName;

    @NotBlank(message = "Значение характеристики не может быть пустым")
    @Size(min = 1, max = 500, message = "Значение характеристики должно быть от 1 до 500 символов")
    @Column(nullable = false, length = 500)
    private String characteristicValue;

    @Column(nullable = false)
    private boolean deleted = false;

    public ProductDetails() {
    }

    // Геттеры и сеттеры
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getCharacteristicName() {
        return characteristicName;
    }

    public void setCharacteristicName(String characteristicName) {
        this.characteristicName = characteristicName;
    }

    public String getCharacteristicValue() {
        return characteristicValue;
    }

    public void setCharacteristicValue(String characteristicValue) {
        this.characteristicValue = characteristicValue;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}



