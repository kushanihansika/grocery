package com.example.grocery.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecipesFilterRequest {

    private MenueType menueType;
    private int days;
    private Type type;
    private int servings;
    private int pageSize;
    private int pageNumber;

}
