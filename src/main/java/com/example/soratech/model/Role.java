package com.example.soratech.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "role")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Название роли не может быть пустым")
    @Size(min = 2, max = 50, message = "Название роли должно быть от 2 до 50 символов")
    @Column(nullable = false, unique = true)
    private String name;

    @Size(max = 255, message = "Описание не должно превышать 255 символов")
    @Column
    private String description;

    @OneToMany(mappedBy = "role")
    private Set<User> users = new HashSet<>();

    @Column(nullable = false)
    private boolean deleted = false;

    public Role() {
    }

    public Role(String name) {
        this.name = name;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}



