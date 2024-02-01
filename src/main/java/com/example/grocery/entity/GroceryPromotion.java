package com.example.grocery.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "grocery_promotion")
@Data
public class GroceryPromotion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String shopName;
    private double price;
    private double discountedRate;
    private  String status;

    @ManyToOne
    @JoinColumn(name="grocery_details_id",referencedColumnName = "id")
    private GroceryDetails groceryDetails;
}
