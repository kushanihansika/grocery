package com.example.grocery.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "planned_grocery_list_details")
@Data
public class PlannedGroceryListDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double predictedAmount;

    private String predictedTime;

    @OneToMany(mappedBy = "plannedGroceryListDetails",cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<GroceryDetails> groceryDetails = new ArrayList<>();

    public void setGroceryDetails(List<GroceryDetails> groceryDetails) {
        groceryDetails.forEach(groceryDetails1 -> groceryDetails1.setPlannedGroceryListDetails(this));
        this.groceryDetails = groceryDetails;
    }
}
