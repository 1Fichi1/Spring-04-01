package com.example.soratech.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import com.example.soratech.validation.ValidPrice;

import java.math.BigDecimal;

@Entity
@Table(name = "order_item")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @NotNull(message = "Количество не может быть null")
    @Positive(message = "Количество должно быть положительным числом")
    @Column(nullable = false)
    private Integer quantity;

    @NotNull(message = "Цена не может быть null")
    @ValidPrice(message = "Цена должна быть положительным числом больше 0")
    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal price;

    @NotNull(message = "Промежуточная сумма не может быть null")
    @ValidPrice(message = "Промежуточная сумма должна быть положительным числом больше 0")
    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal subtotal;

    public OrderItem() {
    }

    // Геттеры и сеттеры
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    @PrePersist
    @PreUpdate
    public void calculateSubtotal() {
        if (quantity != null && price != null) {
            this.subtotal = price.multiply(BigDecimal.valueOf(quantity));
        }
    }
}



