package com.example.grocery.service;

import com.example.grocery.dto.*;
import com.example.grocery.entity.PlanedMenus;
import com.example.grocery.entity.Recipe;
import com.example.grocery.repository.PlanedMenusRepository;
import com.example.grocery.utils.MenuSpecifications;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class PlanedMenusServiceImpl implements PlanedMenusService{

    @Autowired
    private PlanedMenusRepository planedMenusRepository;
    @Autowired
    private RecipesService recipeService;


    public MenuDetailsDto saveMenu(MenuCreateRequest createRequest) {
            PlanedMenus planedMenus = new PlanedMenus();
            planedMenus.setUserId(createRequest.getUserId());
            planedMenus.setDays(createRequest.getDays());
            planedMenus.setStartDate(createRequest.getStartDate());
            planedMenus.setEndData(createRequest.getEndDate());
            // Fetch recipes by their IDs
            Set<Recipe> recipes = new HashSet<>();
            for (String recipeId : createRequest.getRecipesList()) {
                Recipe recipe = recipeService.getRecipeById(Long.parseLong(recipeId));
                recipes.add(recipe);
            }
            planedMenus.setRecipes(recipes);
            // Save the menu
            planedMenusRepository.save(planedMenus);
          return getMenuDetailsDto(planedMenus);
    }

    public List<MenuDetailsDto> getMenusByDateRange(String userId, Long startDate, Long endDate, String sortType, Long menuId) {
        List<MenuDetailsDto> response = new ArrayList<>();
        if(menuId != null){
            Optional<PlanedMenus > menus = planedMenusRepository.findById(menuId);
            if(menus.isPresent()){
               MenuDetailsDto menuDetailsDto= getMenuDetailsDto(menus.get());
                response.add(menuDetailsDto);
                return response;
            }
        }
        MenuFilterRequest menuFilterRequest = new MenuFilterRequest();
        menuFilterRequest.setEndDate(endDate);
        menuFilterRequest.setStartDate(startDate);
        menuFilterRequest.setSort(SortType.valueOf(sortType));
        menuFilterRequest.setUserId(userId);
        return planedMenusRepository.findAll(MenuSpecifications.filterMenus(menuFilterRequest)).stream().map(this::getMenuDetailsDto).collect(Collectors.toList());
    }

    public MenuDetailsDto getMenuById(Long menuId){
        Optional<PlanedMenus> planedMenusOptional = planedMenusRepository.findById(menuId);
        return planedMenusOptional.map(this::getMenuDetailsDto).orElse(null);
    }

    private MenuDetailsDto getMenuDetailsDto(PlanedMenus planedMenus){
        return MenuDetailsDto.builder().id(planedMenus.getId())
                .userId(Long.parseLong(planedMenus.getUserId()))
                .startDate(planedMenus.getStartDate()).recipeDetails(getRecipeDetailsDto(planedMenus.getRecipes()))
                .endDate(planedMenus.getEndData()).build();
    }

    private List<RecipeDetailsDto> getRecipeDetailsDto(Set<Recipe> recipes){
       return recipes.stream().map(this::getRecipeDetailsDto
       ).collect(Collectors.toList());

    }

    private RecipeDetailsDto getRecipeDetailsDto(Recipe recipe){

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
