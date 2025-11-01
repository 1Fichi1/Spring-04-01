package com.example.soratech.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "supplier")
public class Supplier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Название поставщика не может быть пустым")
    @Size(min = 2, max = 150, message = "Название должно быть от 2 до 150 символов")
    @Column(nullable = false)
    private String name;

    @NotBlank(message = "Контактное лицо не может быть пустым")
    @Size(min = 2, max = 100, message = "Контактное лицо должно быть от 2 до 100 символов")
    @Column(nullable = false)
    private String contactPerson;

    @NotBlank(message = "Email не может быть пустым")
    @Email(message = "Некорректный формат email")
    @Column(nullable = false)
    private String email;

    @NotBlank(message = "Телефон не может быть пустым")
    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Некорректный формат телефона")
    @Column(nullable = false)
    private String phone;

    @NotBlank(message = "Адрес не может быть пустым")
    @Size(max = 255, message = "Адрес не должен превышать 255 символов")
    @Column(nullable = false)
    private String address;

    @ManyToMany(mappedBy = "suppliers")
    private Set<Product> products = new HashSet<>();

    @Column(nullable = false)
    private boolean deleted = false;

    public Supplier() {
    }

    // Геттеры и сеттеры
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Set<Product> getProducts() {
        return products;
    }

    public void setProducts(Set<Product> products) {
        this.products = products;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}



