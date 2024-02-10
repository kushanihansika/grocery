package com.example.grocery.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MenuCreateRequest {
    private String userId;
    private String startDate;
    private String endDate;
    private Long generatedGroceryListId;
    private Boolean isVeg;

}
