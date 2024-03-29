package com.example.grocery.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "days")
@Setter
@Getter
public class Days {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long dayId;

    private String dayName;

    private String caloriesPerDay;
    @ManyToOne
    @JoinColumn(name="menu_id",referencedColumnName = "id")
    private PlanedMenus planedMenus;


    @ManyToMany(fetch = FetchType.LAZY,cascade =CascadeType.ALL)
    @JoinTable(
            name = "days_recipes",
            joinColumns = @JoinColumn(name = "day_id"),
            inverseJoinColumns = @JoinColumn(name = "recipe_id")
    )
    private List<Recipe> recipes = new ArrayList<>();
}
