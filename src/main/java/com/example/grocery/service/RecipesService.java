package com.example.grocery.service;

import com.example.grocery.dto.CreateGroceryListDto;
import com.example.grocery.dto.GroceryDetailsDto;
import com.example.grocery.dto.RecipeDetailsDto;
import com.example.grocery.dto.RecipesFilterRequest;
import com.example.grocery.entity.Recipe;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface RecipesService {
     Recipe getRecipeById(Long recipeId);
     RecipeDetailsDto getRecipeDetailsById(Long recipeId);
     RecipeDetailsDto getRecipeDetailsDto(Recipe recipe);

     GroceryDetailsDto getGroceryDetailsDto(CreateGroceryListDto groceryListDto);
     List<RecipeDetailsDto> getAllRecipes(String menueType, Integer days, String type, Integer servings,Long recipeId);
}
