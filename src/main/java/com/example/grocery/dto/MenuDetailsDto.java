package com.example.grocery.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MenuDetailsDto {

    private Long id;
    private Long userId;
    private Long startDate;
    private Long endDate;
    private List<RecipeDetailsDto> recipeDetails;
}
