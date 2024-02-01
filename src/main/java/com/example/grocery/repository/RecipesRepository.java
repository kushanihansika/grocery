package com.example.grocery.repository;

import com.example.grocery.entity.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface RecipesRepository extends  JpaRepository<Recipe, Long>, JpaSpecificationExecutor<Recipe> {

    Recipe findByTitle(String recipeName);
}
