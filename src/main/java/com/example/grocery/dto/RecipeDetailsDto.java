package com.example.grocery.dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecipeDetailsDto {
    private Long id;

    private String url;

    private String title;

    private String cook;

    private String description;

    private Map<String, String> directions;

    private String images;

    private List<String> ingredients;

    private String prepTime;

    private String servings;

    private String totalTime;

    private String menuType;

    private String type;

    private String calories;

    private  String fat;

    private String carbs;

    private String protein;
}
