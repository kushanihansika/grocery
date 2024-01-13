package com.example.grocery.controller;

import com.example.grocery.dto.RecipeDetailsDto;
import com.example.grocery.dto.RecipesFilterRequest;
import com.example.grocery.service.RecipesService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recipes")
public class RecipeController {

    private RecipesService recipesService;

    final ObjectMapper objectMapper;

    public RecipeController(RecipesService recipesService, ObjectMapper objectMapper) {
        this.recipesService = recipesService;
        this.objectMapper = objectMapper;
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<RecipeDetailsDto>> getAllRecipes(
            @RequestParam(value = "menueType" ,required = false) String menueType,
            @RequestParam(value = "days",required = false)Integer days,
            @RequestParam(value = "type",required = false)String type,
            @RequestParam(value = "servings",required = false)Integer servings,
            @RequestParam(value = "recipeId",required = false)Long recipeId
            ) {
        try {
            System.out.println("calling");
            List<RecipeDetailsDto> menus = recipesService.getAllRecipes( menueType,  days,  type,  servings,recipeId);
            return new ResponseEntity<>(menus, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/recipe-details/{recipeId}")
    public ResponseEntity<RecipeDetailsDto> getById(@PathVariable String recipeId) {
        RecipeDetailsDto recipeDetailsDto= recipesService.getRecipeDetailsById(Long.valueOf(recipeId));
        return new ResponseEntity<>(recipeDetailsDto, HttpStatus.OK);
    }
}
