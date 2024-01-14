package com.example.grocery.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MenuCreateRequest {
    private String userId;
    private int days;
    private Long startDate;
    private Long endDate;
    private Long generatedGroceryListId;
    private List<String> recipesList;
}
