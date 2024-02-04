package com.example.grocery.repository;

import com.example.grocery.entity.GroceryDetails;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroceryDetailsRepository  extends JpaRepository<GroceryDetails,Long> {
}
