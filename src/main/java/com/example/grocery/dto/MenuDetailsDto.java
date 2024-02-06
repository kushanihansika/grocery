package com.example.grocery.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MenuDetailsDto {

    private Long id;
    private Long userId;
    private LocalDate startDate;
    private LocalDate endDate;
    private List<RecipeDetailsDto> recipeDetails;
    private List<DaysDetailsDto> recommended;
    private GroceryDetailsDto groceryDetail;
}
