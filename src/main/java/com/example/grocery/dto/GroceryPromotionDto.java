package com.example.grocery.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroceryPromotionDto {
    private Long promotionId;
    private String superMarketName;
    private double price;
    private String status;
    private boolean isSelected;
}
