package com.example.grocery.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroceryDetailsDto {
   private Long groceryListId;
   private List<GroceryItem> groceryItems;

}
