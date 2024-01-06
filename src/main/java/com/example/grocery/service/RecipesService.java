package com.example.grocery.service;

import com.example.grocery.dto.RecipeDetailsDto;
import com.example.grocery.dto.RecipesFilterRequest;
import com.example.grocery.entity.Recipe;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface RecipesService {
     Recipe getRecipeById(Long recipeId);
     RecipeDetailsDto getRecipeDetailsById(Long recipeId);
     RecipeDetailsDto getRecipeDetailsDto(Recipe recipe);
     Page<RecipeDetailsDto> getAllRecipes(RecipesFilterRequest recipesFilterRequest, Pageable pageable);
}
