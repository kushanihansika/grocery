package com.example.grocery.repository;

import com.example.grocery.entity.PlanedMenus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;



@Repository
public interface PlanedMenusRepository extends JpaRepository<PlanedMenus,Long> , JpaSpecificationExecutor<PlanedMenus> {

    Page<PlanedMenus> findByUserIdAndStartDateBetween(String userId, Long startDate, Long endDate, Pageable pageable);
}
