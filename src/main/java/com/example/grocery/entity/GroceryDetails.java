package com.example.grocery.entity;

import jakarta.persistence.*;
import lombok.Data;


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

    private Double price;

    private Long promotionId;

    private String chosenSuperMarket;

    private double discountRate;

    @ManyToOne
    @JoinColumn(name="planned_grocery_list_details_id",referencedColumnName = "id")
    private PlannedGroceryListDetails plannedGroceryListDetails;

}
