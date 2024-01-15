package com.example.grocery.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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
    private Long generatedGroceryListId;

    @ManyToMany(fetch = FetchType.LAZY,cascade =CascadeType.ALL)
    @JoinTable(
            name = "planed_menus_recipes",
            joinColumns = @JoinColumn(name = "planed_menu_id"),
            inverseJoinColumns = @JoinColumn(name = "recipe_id")
    )
    private List<Recipe> recipes = new ArrayList<>();
}
