package com.example.grocery.controller;

import com.example.grocery.dto.MenuCreateRequest;
import com.example.grocery.dto.MenuDetailsDto;
import com.example.grocery.dto.MenuFilterRequest;
import com.example.grocery.service.PlanedMenusService;
import com.example.grocery.service.RecipesService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/menu")
public class MenuController {


    private PlanedMenusService planedMenusService;

    private RecipesService recipeService;

    final ObjectMapper objectMapper;

    public MenuController(PlanedMenusService planedMenusService, RecipesService recipeService, ObjectMapper objectMapper) {
        this.planedMenusService = planedMenusService;
        this.recipeService = recipeService;
        this.objectMapper = objectMapper;
    }

    @PostMapping("/create")
    public ResponseEntity<MenuDetailsDto> createMenu(@RequestBody MenuCreateRequest createMenuRequest) {
        MenuDetailsDto planedMenus=  planedMenusService.saveMenu(createMenuRequest);
            return new ResponseEntity<>(planedMenus, HttpStatus.CREATED);
    }

    @PostMapping("/getAll")
    public ResponseEntity<Page<MenuDetailsDto>> getMenusByDateRange(
            @RequestBody MenuFilterRequest filterRequest) {
        try {

            Pageable pageable = PageRequest.of(filterRequest.getPageNumber(), filterRequest.getPageSize());
            Page<MenuDetailsDto> menus = planedMenusService.getMenusByDateRange(filterRequest, pageable);

            return new ResponseEntity<>(menus, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{menuId}")
    public ResponseEntity<MenuDetailsDto> getById(@PathVariable String menuId) {

            MenuDetailsDto menuDetailsDto= planedMenusService.getMenuById(Long.valueOf(menuId));
            return new ResponseEntity<>(menuDetailsDto, HttpStatus.OK);

    }
}
