package com.example.grocery.service;
import com.example.grocery.dto.*;
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

    public GroceryDetailsDto getGroceryDetailsDto(CreateGroceryListDto groceryListDto){

        GroceryDetailsDto groceryDetailsDto = new GroceryDetailsDto();
       groceryDetailsDto.setGroceryListId(1L);
       groceryDetailsDto.setGroceryItems(getGroceryItems());
       return groceryDetailsDto;
    }

    private List<GroceryItem> getGroceryItems(){
        List<GroceryItem> items = new ArrayList<>();
        GroceryItem groceryItem1 = new GroceryItem();
        groceryItem1.setIngredientName("Sugar");
        groceryItem1.setQuantity("1Kg 500g");
        groceryItem1.setGroceryPromotions(getGroceryPromotion());
        GroceryItem groceryItem2 = new GroceryItem();
        groceryItem2.setIngredientName("Salt");
        groceryItem2.setQuantity("1Kg 100g");
        groceryItem2.setGroceryPromotions(getGroceryPromotion());
        GroceryItem groceryItem3 = new GroceryItem();
        groceryItem3.setIngredientName("Bread");
        groceryItem3.setQuantity("2 ");
        groceryItem3.setGroceryPromotions(getGroceryPromotion());
        GroceryItem groceryItem4 = new GroceryItem();
        groceryItem4.setIngredientName("Flour");
        groceryItem4.setQuantity("1Kg 500g");
        groceryItem4.setGroceryPromotions(getGroceryPromotion());
        GroceryItem groceryItem5 = new GroceryItem();
        groceryItem5.setIngredientName("Rice");
        groceryItem5.setQuantity("1Kg 500g");
        groceryItem5.setGroceryPromotions(getGroceryPromotion());
        GroceryItem groceryItem6 = new GroceryItem();
        groceryItem6.setIngredientName("Cream Cheese");
        groceryItem6.setQuantity("500g");
        groceryItem6.setGroceryPromotions(getGroceryPromotion());
        GroceryItem groceryItem7 = new GroceryItem();
        groceryItem7.setIngredientName("Chicken");
        groceryItem7.setQuantity("500g");
        groceryItem7.setGroceryPromotions(getGroceryPromotion());
        GroceryItem groceryItem8 = new GroceryItem();
        groceryItem8.setIngredientName("Eggs");
        groceryItem8.setQuantity("500g");
        groceryItem8.setGroceryPromotions(getGroceryPromotion());
        items.add(groceryItem1);
        items.add(groceryItem2);
        items.add(groceryItem3);
        items.add(groceryItem4);
        items.add(groceryItem5);
        items.add(groceryItem6);
        items.add(groceryItem7);
        items.add(groceryItem8);

        return items;

    }

    private  List<GroceryPromotion> getGroceryPromotion(){
        List<GroceryPromotion> groceryPromotions = new ArrayList<>();
        GroceryPromotion groceryPromotion1 = new GroceryPromotion();
        groceryPromotion1.setSuperMarketName(Supermarket.ALDI.name());
        groceryPromotion1.setPrice(2);

        GroceryPromotion groceryPromotion2 = new GroceryPromotion();
        groceryPromotion2.setSuperMarketName(Supermarket.KELLS.name());
        groceryPromotion2.setPrice(5);

        GroceryPromotion groceryPromotion3 = new GroceryPromotion();
        groceryPromotion3.setSuperMarketName(Supermarket.SPAR.name());
        groceryPromotion3.setPrice(5);

        groceryPromotions.add(groceryPromotion1);
        groceryPromotions.add(groceryPromotion2);
        groceryPromotions.add(groceryPromotion3);
        return groceryPromotions;
    }
}
