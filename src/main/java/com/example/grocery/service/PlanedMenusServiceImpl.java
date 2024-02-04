package com.example.grocery.service;

import com.example.grocery.dto.*;
import com.example.grocery.entity.*;
import com.example.grocery.repository.GroceryDetailsRepository;
import com.example.grocery.repository.PlanedMenusRepository;
import com.example.grocery.repository.PlannedGroceryListDetailsRepository;
import com.example.grocery.utils.DateCalculator;
import com.example.grocery.utils.MenuSpecifications;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@Transactional
public class PlanedMenusServiceImpl implements PlanedMenusService{

    @Autowired
    private PlanedMenusRepository planedMenusRepository;
    @Autowired
    private RecipesService recipeService;

    @Autowired
    private PlannedGroceryListDetailsRepository plannedGroceryListDetailsRepository;

    @Autowired
    private GroceryDetailsRepository groceryDetailsRepository;
    private final String fastApiBaseUrl = "http://127.0.0.1:8000";

    public MenuDetailsDto saveMenu(MenuCreateRequest createRequest) throws Exception {

            PlanedMenus planedMenus = new PlanedMenus();
            planedMenus.setUserId(createRequest.getUserId());
            planedMenus.setDays(createRequest.getDays());
            planedMenus.setStartDate(createRequest.getStartDate().toEpochDay());
            planedMenus.setEndData(createRequest.getEndDate().toEpochDay());
            planedMenus.setGeneratedGroceryListId(createRequest.getGeneratedGroceryListId());
            Long daysCount = DateCalculator.calculateDaysBetween(createRequest.getStartDate(),createRequest.getEndDate());
            // Fetch recipes by their IDs
           String vegOrNonVeg ="NON VEG";
           if(Boolean.TRUE.equals(createRequest.getIsVeg())){
               vegOrNonVeg="VEG";
           }
        List<String> recipesNamesDinner = getRecommendations(MenuType.DINNER.name(), vegOrNonVeg);
        List<String> recipesNamesLunch = getRecommendations(MenuType.LUNCH.name(),vegOrNonVeg);
        List<String> recipesNamesBreakfast = getRecommendations(MenuType.BREAKFAST.name(), vegOrNonVeg);
        List<Recipe> recipeListDinner = new ArrayList<>();
        List<Recipe> recipeListLunch = new ArrayList<>();
        List<Recipe> recipeListBreakfast = new ArrayList<>();
            for (String recipesName :recipesNamesDinner) {
                Recipe recipe = recipeService.getRecipeByTitle(recipesName);
                if(recipe != null){
                    recipeListDinner.add(recipe);
                }

            }

        for (String recipesName :recipesNamesLunch) {
            Recipe recipe = recipeService.getRecipeByTitle(recipesName);
            if(recipe != null){
                recipeListLunch.add(recipe);
            }
        }
        for (String recipesName :recipesNamesBreakfast) {
            Recipe recipe = recipeService.getRecipeByTitle(recipesName);
            if(recipe != null){
                recipeListBreakfast.add(recipe);
            }
        }
         List<Recipe> getAllDinnerRecipes =getNumberOfRecipes(recipeListDinner,daysCount.intValue()+1);
         List<Recipe> getAllLunchRecipes =getNumberOfRecipes(recipeListLunch,daysCount.intValue()+1);
         List<Recipe> getAllBreakfastRecipes =getNumberOfRecipes(recipeListBreakfast,daysCount.intValue()+1);
        List<Days> daysList = new ArrayList<>();
       for (Long day = 0L; day <= daysCount; day++) {
           Days days = new Days();
           Long dayName = day+1;
           days.setDayName("Day_".concat(dayName.toString()));
           List<Recipe> dayRecipe = new ArrayList<>();
           dayRecipe.add(getAllDinnerRecipes.get(day.intValue()));
           dayRecipe.add(getAllLunchRecipes.get(day.intValue()));
           dayRecipe.add(getAllBreakfastRecipes.get(day.intValue()));
           days.setRecipes(dayRecipe);
           List<RecipeDetailsDto> recipeDetailsDtos=dayRecipe.stream().map(this::getRecipeDetailsDto).toList();
           int sumOfCalories = recipeDetailsDtos.stream()
                   .mapToInt(value -> Integer.parseInt(value.getCalories())) // Assuming getCalories is the getter for calories
                   .sum();
           days.setCaloriesPerDay(Integer.toString(sumOfCalories));
           daysList.add(days);
        }



       List<String> plannedAllRecipesIdList= new ArrayList<>();
       // generate groceryList
        for(Days day : daysList){
            day.getRecipes().stream().forEach(recipe -> plannedAllRecipesIdList.add(recipe.getId().toString()));
        }

        planedMenus.setDaysList(daysList);
        planedMenus.setGeneratedGroceryListId(generateGroceryList(plannedAllRecipesIdList));
            // Save the menu
            planedMenusRepository.save(planedMenus);
          return getMenuDetailsDto(planedMenus);
    }

    private   List<Recipe> getNumberOfRecipes(List<Recipe> recipeList, int numberOfRecipes) {
        List<Recipe> selectedRecipes = new ArrayList<>();

        // Check if there are enough recipes in the list
        if (numberOfRecipes > recipeList.size()) {
            System.out.println("Not enough recipes available.");
            return selectedRecipes;
        }

        // Get the first 'numberOfRecipes' recipes from the list
        for (int i = 0; i < numberOfRecipes; i++) {
            selectedRecipes.add(recipeList.get(i));
        }

        return selectedRecipes;
    }
    @Transactional
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
        menuFilterRequest.setSort(sortType !=null?SortType.valueOf(sortType):null);
        menuFilterRequest.setUserId(userId);
        return planedMenusRepository.findAll(MenuSpecifications.filterMenus(menuFilterRequest)).stream().map(this::getMenuDetailsDto).collect(Collectors.toList());
    }
    @Transactional
    public MenuDetailsDto getMenuById(Long menuId) {
        Optional<PlanedMenus> planedMenusOptional = planedMenusRepository.findById(menuId);
        return planedMenusOptional.map(planedMenus -> {
            List<Recipe> recipes = new ArrayList<>();

            return MenuDetailsDto.builder()
                    .id(planedMenus.getId())
                    .userId(Long.parseLong(planedMenus.getUserId()))
                    .startDate(planedMenus.getStartDate())
                    .recipeDetails(getRecipeDetailsDto(recipes))
                    .recommended(getDaysDetailsDto(planedMenus.getDaysList()))
                    .endDate(planedMenus.getEndData())
                    .build();
        }).orElse(null);


    }

    public void  updatedMenuPromotions(GroceryDetailsUpdatedDto groceryDetailsUpdatedDto){
        for (GroceryItem groceryItem :groceryDetailsUpdatedDto.getGroceryItems()){
            Optional<GroceryDetails> groceryDetails = groceryDetailsRepository.findById(groceryItem.getGroceryItemId());
            GroceryDetails details = groceryDetails.get();
            details.setPromotionId(groceryItem.getGroceryPromotionDtos().get(0).getPromotionId());
            details.setChosenSuperMarket(groceryItem.getGroceryPromotionDtos().get(0).getSuperMarketName());
            details.setDiscountRate(groceryItem.getGroceryPromotionDtos().get(0).getPrice());
            groceryDetailsRepository.save(details);
        }

    }
    private List<DaysDetailsDto> getDaysDetailsDto(List<Days> days){

    List<DaysDetailsDto> dtos = new ArrayList<>();
    for (Days day : days){
        DaysDetailsDto detailsDto = new DaysDetailsDto();
        detailsDto.setDayId(day.getDayId());
        detailsDto.setDayName(day.getDayName());
        detailsDto.setCaloriesPerDay(day.getCaloriesPerDay());
        detailsDto.setDaysRecipeDetailsDto(getDaysRecipeDetailsDto(day));
        dtos.add(detailsDto);
    }
    return dtos;
    }
    private List<DaysRecipeDetailsDto> getDaysRecipeDetailsDto(Days day){
        List<DaysRecipeDetailsDto> detailsDtoList = new ArrayList<>();
        System.out.println("recipes"+day.getRecipes());
        for (Recipe recipe: day.getRecipes()){
            DaysRecipeDetailsDto daysRecipeDetailsDto = new DaysRecipeDetailsDto();
            daysRecipeDetailsDto.setMenueType(MenuType.valueOf(recipe.getMenuType()));
            daysRecipeDetailsDto.setRecipeDetailsDto(getRecipeDetailsDto(recipe));
            detailsDtoList.add(daysRecipeDetailsDto);
        }
        return detailsDtoList;
    }
    private MenuDetailsDto getMenuDetailsDto(PlanedMenus planedMenus){
        //getRecipeDetailsDto(planedMenus.getRecipes())
        return MenuDetailsDto.builder().id(planedMenus.getId())
                .userId(Long.parseLong(planedMenus.getUserId()))
                .startDate(planedMenus.getStartDate())
                .recipeDetails(null)
                .groceryDetailsDto(getGroceryDetailsDto(planedMenus.getGeneratedGroceryListId()))
                .recommended(getDaysDetailsDto(planedMenus.getDaysList()))
                .endDate(planedMenus.getEndData()).build();
    }

    private GroceryDetailsDto getGroceryDetailsDto(Long groceryListId){

        GroceryDetailsDto groceryDetailsDto = new GroceryDetailsDto();
        Optional<PlannedGroceryListDetails> groceryDetails=plannedGroceryListDetailsRepository.findById(groceryListId);
        if(groceryDetails.isPresent()){
            PlannedGroceryListDetails details = groceryDetails.get();
            groceryDetailsDto.setGroceryListId(details.getId());
            groceryDetailsDto.setPredictedTime(details.getPredictedTime());
            groceryDetailsDto.setPredictedPrice(details.getPredictedAmount());
            List<GroceryItem> detailsList = new ArrayList<>();

            for (GroceryDetails groceryDetail : details.getGroceryDetails()){
                GroceryItem item = new GroceryItem();
                item.setGroceryItemId(groceryDetail.getId());
                item.setIngredientName(groceryDetail.getName());
                item.setUnit(groceryDetail.getUnit());
                item.setQuantity(groceryDetail.getQty());
                List<GroceryPromotionDto> promotionDtos = new ArrayList<>();
                if(groceryDetail.getPromotionId()  != null){
                    GroceryPromotionDto groceryPromotionDto = new GroceryPromotionDto();
                    groceryPromotionDto.setPromotionId(groceryDetail.getPromotionId());
                    groceryPromotionDto.setPrice(groceryDetail.getDiscountRate());
                    groceryPromotionDto.setSuperMarketName(groceryDetail.getChosenSuperMarket());
                    promotionDtos.add(groceryPromotionDto);
                }else {
                    promotionDtos.addAll(getGroceryPromotionDto());
                }
                item.setGroceryPromotionDtos(promotionDtos);
                detailsList.add(item);
            }

            groceryDetailsDto.setGroceryItems(detailsList);
        }
        return groceryDetailsDto;
    }
    private List<RecipeDetailsDto> getRecipeDetailsDto(List<Recipe> recipes){
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




    public List<String> getRecommendations(String recipeType, String vegType) throws Exception {
        String url = fastApiBaseUrl + "/recommend/?recipe_type=" + recipeType + "&veg_type=" + vegType;

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Convert the response body to a List<String> using Jackson for JSON parsing
        List<String> recommendations = convertResponseToList(response.body());
        System.out.println("list of recommendations"+recommendations);
        return recommendations;
    }

    private List<String> convertResponseToList(String responseBody) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(responseBody, List.class);
    }

    private Long generateGroceryList(List<String> recipeIds){
        PlannedGroceryListDetails groceryListDetails = new PlannedGroceryListDetails();
        groceryListDetails.setPredictedAmount(200.0);
        groceryListDetails.setGroceryDetails(getGroceryDetails());
        groceryListDetails.setPredictedTime("1h");
        PlannedGroceryListDetails plannedGroceryListDetails= plannedGroceryListDetailsRepository.save(groceryListDetails);
        return plannedGroceryListDetails.getId();
    }
    public GroceryDetailsDto getGroceryDetailsDto(CreateGroceryListDto groceryListDto){
        GroceryDetailsDto groceryDetailsDto = new GroceryDetailsDto();
        groceryDetailsDto.setGroceryListId(1L);
        groceryDetailsDto.setGroceryItems(getGroceryItems());
        ObjectMapper mapper = new ObjectMapper();

        return groceryDetailsDto;
    }

    private List<GroceryItem> getGroceryItems(){
        List<GroceryItem> items = new ArrayList<>();
        GroceryItem groceryItem1 = new GroceryItem();
        groceryItem1.setIngredientName("Sugar");
        groceryItem1.setQuantity("1Kg 500g");
        groceryItem1.setGroceryPromotionDtos(getGroceryPromotionDto());
        GroceryItem groceryItem2 = new GroceryItem();
        groceryItem2.setIngredientName("Salt");
        groceryItem2.setQuantity("1Kg 100g");
        groceryItem2.setGroceryPromotionDtos(getGroceryPromotionDto());
        GroceryItem groceryItem3 = new GroceryItem();
        groceryItem3.setIngredientName("Bread");
        groceryItem3.setQuantity("2 ");
        groceryItem3.setGroceryPromotionDtos(getGroceryPromotionDto());
        GroceryItem groceryItem4 = new GroceryItem();
        groceryItem4.setIngredientName("Flour");
        groceryItem4.setQuantity("1Kg 500g");
        groceryItem4.setGroceryPromotionDtos(getGroceryPromotionDto());
        GroceryItem groceryItem5 = new GroceryItem();
        groceryItem5.setIngredientName("Rice");
        groceryItem5.setQuantity("1Kg 500g");
        groceryItem5.setGroceryPromotionDtos(getGroceryPromotionDto());
        GroceryItem groceryItem6 = new GroceryItem();
        groceryItem6.setIngredientName("Cream Cheese");
        groceryItem6.setQuantity("500g");
        groceryItem6.setGroceryPromotionDtos(getGroceryPromotionDto());
        GroceryItem groceryItem7 = new GroceryItem();
        groceryItem7.setIngredientName("Chicken");
        groceryItem7.setQuantity("500g");
        groceryItem7.setGroceryPromotionDtos(getGroceryPromotionDto());
        GroceryItem groceryItem8 = new GroceryItem();
        groceryItem8.setIngredientName("Eggs");
        groceryItem8.setQuantity("500g");
        groceryItem8.setGroceryPromotionDtos(getGroceryPromotionDto());
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
    private List<GroceryDetails> getGroceryDetails(){
        List<GroceryDetails> items = new ArrayList<>();
        GroceryDetails groceryItem1 = new GroceryDetails();
        groceryItem1.setName("Sugar");
        groceryItem1.setQty("1Kg 500g");
        groceryItem1.setPrice(1.0);


        GroceryDetails groceryItem2 = new GroceryDetails();
        groceryItem2.setName("Salt");
        groceryItem2.setQty("1Kg 100g");
        groceryItem2.setPrice(1.0);

        GroceryDetails groceryItem3 = new GroceryDetails();
        groceryItem3.setName("Bread");
        groceryItem3.setQty("2 ");
        groceryItem3.setPrice(3.0);

        GroceryDetails groceryItem4 = new GroceryDetails();
        groceryItem4.setName("Flour");
        groceryItem4.setQty("1Kg 500g");
        groceryItem4.setPrice(7.0);

        GroceryDetails groceryItem5 = new GroceryDetails();
        groceryItem5.setName("Rice");
        groceryItem5.setQty("1Kg 500g");
        groceryItem5.setPrice(3.0);

        GroceryDetails groceryItem6 = new GroceryDetails();
        groceryItem6.setName("Cream Cheese");
        groceryItem6.setQty("500g");
        groceryItem6.setPrice(5.0);

        GroceryDetails groceryItem7 = new GroceryDetails();
        groceryItem7.setName("Chicken");
        groceryItem7.setQty("500g");
        groceryItem7.setPrice(4.0);

        GroceryDetails groceryItem8 = new GroceryDetails();
        groceryItem8.setName("Eggs");
        groceryItem8.setQty("500g");
        groceryItem8.setPrice(8.0);

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
    private  List<GroceryPromotionDto> getGroceryPromotionDto(){
        List<GroceryPromotionDto> groceryPromotionDtos = new ArrayList<>();
        GroceryPromotionDto groceryPromotionDto1 = new GroceryPromotionDto();
        groceryPromotionDto1.setSuperMarketName(Supermarket.ALDI.name());
        groceryPromotionDto1.setPrice(2);
        groceryPromotionDto1.setStatus("ACTIVE");
        groceryPromotionDto1.setPromotionId(1L);

        GroceryPromotionDto groceryPromotionDto2 = new GroceryPromotionDto();
        groceryPromotionDto2.setSuperMarketName(Supermarket.KELLS.name());
        groceryPromotionDto2.setPrice(5);
        groceryPromotionDto2.setPromotionId(2L);
        groceryPromotionDto1.setStatus("ACTIVE");
        GroceryPromotionDto groceryPromotionDto3 = new GroceryPromotionDto();
        groceryPromotionDto3.setSuperMarketName(Supermarket.SPAR.name());
        groceryPromotionDto3.setPrice(5);
        groceryPromotionDto3.setPromotionId(3L);
        groceryPromotionDto1.setStatus("ACTIVE");
        groceryPromotionDtos.add(groceryPromotionDto1);
        groceryPromotionDtos.add(groceryPromotionDto2);
        groceryPromotionDtos.add(groceryPromotionDto3);
        return groceryPromotionDtos;
    }

    private  List<GroceryPromotion> getGroceryPromotion(){
        List<GroceryPromotion> groceryPromotions = new ArrayList<>();
        GroceryPromotion groceryPromotion1 = new GroceryPromotion();
        groceryPromotion1.setId(1L);
        groceryPromotion1.setShopName(Supermarket.ALDI.name());
        groceryPromotion1.setPrice(2.0);
        groceryPromotion1.setStatus("ACTIVE");
        groceryPromotion1.setDiscountedRate(5.0);

        GroceryPromotion groceryPromotion2 = new GroceryPromotion();
        groceryPromotion1.setId(2L);
        groceryPromotion2.setShopName(Supermarket.KELLS.name());
        groceryPromotion2.setPrice(5.0);
        groceryPromotion2.setStatus("ACTIVE");
        groceryPromotion1.setDiscountedRate(5.0);

        GroceryPromotion groceryPromotion3 = new GroceryPromotion();
        groceryPromotion1.setId(3L);
        groceryPromotion3.setShopName(Supermarket.SPAR.name());
        groceryPromotion3.setPrice(5.0);
        groceryPromotion3.setStatus("ACTIVE");
        groceryPromotion1.setDiscountedRate(5.0);

        groceryPromotions.add(groceryPromotion1);
        groceryPromotions.add(groceryPromotion2);
        groceryPromotions.add(groceryPromotion3);
        return groceryPromotions;
    }
}
