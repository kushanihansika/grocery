package com.example.grocery.repository;

import com.example.grocery.entity.PlanedMenus;
import com.example.grocery.entity.PlannedGroceryListDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlannedGroceryListDetailsRepository  extends JpaRepository<PlannedGroceryListDetails,Long> {
}
