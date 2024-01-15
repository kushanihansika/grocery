package com.example.grocery.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "planed_menus")
@Data
public class GroceryDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
