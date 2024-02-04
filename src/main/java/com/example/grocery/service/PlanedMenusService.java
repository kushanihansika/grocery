package com.example.grocery.service;

import com.example.grocery.dto.GroceryDetailsDto;
import com.example.grocery.dto.GroceryDetailsUpdatedDto;
import com.example.grocery.dto.MenuCreateRequest;
import com.example.grocery.dto.MenuDetailsDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PlanedMenusService {
    MenuDetailsDto saveMenu(MenuCreateRequest createRequest) throws Exception;

    List<MenuDetailsDto> getMenusByDateRange(String userId, Long startDate, Long endDate, String sortType, Long menuId);

    MenuDetailsDto getMenuById(Long menuId) throws Exception;

    void  updatedMenuPromotions(GroceryDetailsUpdatedDto groceryDetailsUpdatedDto);
}
