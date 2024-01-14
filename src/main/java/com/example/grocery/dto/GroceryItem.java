package com.example.grocery.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroceryItem {

   private String ingredientName;

   private String quantity;
   private String unit;

   private List<GroceryPromotion> groceryPromotions;
}
