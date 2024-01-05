package com.example.grocery.service;

import com.example.grocery.dto.MenuCreateRequest;
import com.example.grocery.dto.MenuDetailsDto;
import com.example.grocery.dto.MenuFilterRequest;
import com.example.grocery.entity.PlanedMenus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface PlanedMenusService {
    PlanedMenus saveMenu(MenuCreateRequest createRequest);

    Page<MenuDetailsDto> getMenusByDateRange(MenuFilterRequest menuFilterRequest, Pageable pageable);

    MenuDetailsDto getMenuById(Long menuId);
}