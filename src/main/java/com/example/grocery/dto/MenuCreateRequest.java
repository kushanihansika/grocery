package com.example.grocery.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MenuCreateRequest {
    private String userId;
    private LocalDate startDate;
    private LocalDate endDate;
    private Long generatedGroceryListId;
    private Boolean isVeg;

}
