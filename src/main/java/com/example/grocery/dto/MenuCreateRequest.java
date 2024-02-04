package com.example.grocery.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MenuCreateRequest {
    private String userId;
    private int days;
    private LocalDate startDate;
    private LocalDate endDate;
    private Long generatedGroceryListId;
    private MenuType type;
    private Boolean isVeg;

}
