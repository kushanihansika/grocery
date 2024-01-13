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

    private String menueType;
    private Integer days;
    private String type;
    private Integer servings;
    private int pageSize;
    private int pageNumber;

}
