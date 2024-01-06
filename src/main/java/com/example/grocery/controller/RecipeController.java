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

@RestController
@RequestMapping("/api/recipes")
public class RecipeController {

    private RecipesService recipesService;

    final ObjectMapper objectMapper;

    public RecipeController(RecipesService recipesService, ObjectMapper objectMapper) {
        this.recipesService = recipesService;
        this.objectMapper = objectMapper;
    }

    @PostMapping("/getAll")
    public ResponseEntity<Page<RecipeDetailsDto>> getAllRecipes(
            @RequestBody RecipesFilterRequest filterRequest) {
        try {
            //final RecipesFilterRequest filterRequest = objectMapper.readValue(filterRequest, RecipesFilterRequest.class);
            Pageable pageable = PageRequest.of(filterRequest.getPageNumber(), filterRequest.getPageSize());
            System.out.println("calling");
            Page<RecipeDetailsDto> menus = recipesService.getAllRecipes(filterRequest, pageable);
            return new ResponseEntity<>(menus, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{recipeId}")
    public ResponseEntity<RecipeDetailsDto> getById(@PathVariable String recipeId) {
        RecipeDetailsDto recipeDetailsDto= recipesService.getRecipeDetailsById(Long.valueOf(recipeId));
        return new ResponseEntity<>(recipeDetailsDto, HttpStatus.OK);
    }
}
