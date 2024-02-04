package com.example.grocery.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "super_market")
@Data
public class SuperMarket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String shopName;

    private String lat;
    private String lon;
}
