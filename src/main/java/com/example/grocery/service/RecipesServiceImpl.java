package com.example.grocery.service;
import com.example.grocery.dto.RecipeDetailsDto;
import com.example.grocery.dto.RecipesFilterRequest;
import com.example.grocery.entity.Recipe;
import com.example.grocery.repository.RecipesRepository;
import com.example.grocery.utils.RecipesSpecifications;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class RecipesServiceImpl implements RecipesService {

    @Autowired
    private RecipesRepository recipeRepository;

    public Recipe getRecipeById(Long recipeId) {
        Optional<Recipe> recipeOptional = recipeRepository.findById(recipeId);
        return recipeOptional.orElse(null);
    }
    public RecipeDetailsDto getRecipeDetailsById(Long recipeId) {
        Optional<Recipe> recipeOptional = recipeRepository.findById(recipeId);
        return recipeOptional.map(this::getRecipeDetailsDto).orElse(null);
    }

    public List<RecipeDetailsDto> getAllRecipes( String menueType, Integer days, String type, Integer servings,Long recipeId) {
      try {
          List<RecipeDetailsDto> recipeDetailsDtoList = new ArrayList<>();
          if(recipeId != null){
              RecipeDetailsDto recipeDetailsDto= getRecipeDetailsById(recipeId);
              recipeDetailsDtoList.add(recipeDetailsDto);
              return recipeDetailsDtoList;
          }
          RecipesFilterRequest request = new RecipesFilterRequest();
          request.setType(type);
          request.setMenueType(menueType);
          request.setServings(servings);
          return recipeRepository.findAll(RecipesSpecifications.generateFilterRecipeQuery(request)).stream().map(this::getRecipeDetailsDto).collect(Collectors.toList());
      }catch (Exception e){
          e.printStackTrace();
          throw e;
      }

    }
    public RecipeDetailsDto getRecipeDetailsDto(Recipe recipe){

        Map<String, String> nutritionMap = new HashMap<>();
        String[] pairs = recipe.getHealthyDetails().split(",");
        for (String pair : pairs) {
            // Split each pair by space to separate key and value
            String[] keyValue = pair.split(" ", 2);

            // Check if there are two elements in the keyValue array
            if (keyValue.length == 2) {
                String key = keyValue[0];
                String value = keyValue[1];

                nutritionMap.put(key, value);
            }

        }

        return RecipeDetailsDto.builder().id(recipe.getId())
                .url(recipe.getUrl())
                .title(recipe.getTitle())
                .description(recipe.getDescription())
                .menuType(recipe.getMenuType())
                .type(recipe.getType())
                .servings(recipe.getServings())
                .cook(recipe.getCook()).images(recipe.getImages())
                .ingredients(Arrays.asList(recipe.getIngredients().split(", ")))
                .calories(nutritionMap.get("Calories"))
                .carbs(nutritionMap.get("Carbs"))
                .fat(nutritionMap.get("Fat"))
                .protein(nutritionMap.get("Protein"))
                .prepTime(recipe.getPrepTime())
                .images(recipe.getImages())
                .totalTime(recipe.getTotalTime()).directions(parseSteps(recipe.getDirections())).build();
    }


    private static Map<String, String> parseSteps(String input) {
        Map<String, String> stepMap = new HashMap<>();
        Pattern pattern = Pattern.compile("Step\\d+:\\s(.*?)(?=(Step\\d+|$))", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(input);

        while (matcher.find()) {
            String step = matcher.group(1).trim();
            String stepNumber = matcher.group().split(":")[0].trim();
            stepMap.put(stepNumber, step);
        }

        return stepMap;
    }
}
