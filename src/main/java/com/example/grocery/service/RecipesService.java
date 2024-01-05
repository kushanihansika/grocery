package com.example.grocery.service;

import com.example.grocery.dto.RecipeDetailsDto;
import com.example.grocery.entity.Recipe;
import org.springframework.stereotype.Service;

@Service
public interface RecipesService {
     Recipe getRecipeById(Long recipeId);
     RecipeDetailsDto getRecipeDetailsById(Long recipeId);
      RecipeDetailsDto getRecipeDetailsDto(Recipe recipe);
}
