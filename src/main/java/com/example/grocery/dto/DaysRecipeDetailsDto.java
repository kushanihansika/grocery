package com.example.grocery.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DaysRecipeDetailsDto {
    private MenuType menuType;
    private RecipeDetailsDto recipeDetail;
}
