package com.example.grocery.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "grocery_details")
@Data
public class GroceryDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String qty;

    private String unit;

    private String price;
    @ManyToOne
    @JoinColumn(name="planned_grocery_list_details_id",referencedColumnName = "id")
    private PlannedGroceryListDetails plannedGroceryListDetails;

    @OneToMany(mappedBy = "groceryDetails",cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<GroceryPromotion> promotions = new ArrayList<>();
}
