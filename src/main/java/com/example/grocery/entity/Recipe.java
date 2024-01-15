package com.example.grocery.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "recipe")
@Data
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "url" )
    private String url;

    @Column(name = "title" )
    private String title;

    @Column(name = "cook")
    private String cook;

    @Column(name = "description")
    private String description;

    @Column(name = "directions")
    private String directions;

    @Column(name = "images")
    private String images;

    @Column(name = "ingredients")
    private String ingredients;

    @Column(name = "prep_time")
    private String prepTime;

    @Column(name = "servings")
    private String servings;

    @Column(name = "total_time")
    private String totalTime;

    @Column(name = "menu_type")
    private String menuType;

    @Column(name = "type")
    private String type;

    @Column(name = "healthy_details")
    private String healthyDetails;

    @Column(name = "ingredients_quantity")
    private String ingredientsQuantity;

    @ManyToMany(mappedBy = "recipes",fetch = FetchType.EAGER)
    private List<PlanedMenus> planedMenus = new ArrayList<>();
}
