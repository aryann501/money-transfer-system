package com.example.backend.entities;

import com.example.backend.enums.ERole;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private ERole roleName;

    // No-args constructor
    public Role() {
    }

    // All-args constructor
    public Role(Integer id, ERole roleName) {
        this.id = id;
        this.roleName = roleName;
    }

    // Custom constructor (roleName only)
    public Role(ERole roleName) {
        this.roleName = roleName;
    }

    // toString method
    @Override
    public String toString() {
        return "Role{" +
                "id=" + id +
                ", roleName=" + roleName +
                '}';
    }
}
