package com.example.grocery.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "planed_menus")
@Data
public class PlanedMenus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String userId;
    private int days;
    private Long startDate;
    private Long endData;

    @ManyToMany
    @JoinTable(
            name = "planed_menus_recipes",
            joinColumns = @JoinColumn(name = "planed_menu_id"),
            inverseJoinColumns = @JoinColumn(name = "recipe_id")
    )
    private Set<Recipe> recipes = new HashSet<>();
}
