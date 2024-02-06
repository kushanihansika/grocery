package com.example.grocery.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DaysDetailsDto {
    private String dayName;
    private Long dayId;

    private String caloriesPerDay;
    private List<DaysRecipeDetailsDto> daysRecipeDetails;

    private List<DaysRecipeDetailsDto> daysRecipeDetailsDto;

}
