package com.example.grocery.controller;

import com.example.grocery.dto.*;
import com.example.grocery.service.PlanedMenusService;
import com.example.grocery.service.RecipesService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<MenuDetailsDto> createMenu(@RequestBody MenuCreateRequest createMenuRequest) throws Exception {
        MenuDetailsDto planedMenus=  planedMenusService.saveMenu(createMenuRequest);
            return new ResponseEntity<>(planedMenus, HttpStatus.CREATED);
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<MenuDetailsDto>> getMenusByDateRange(
            @RequestParam(value = "userId" ,required = false) String userId,
            @RequestParam(value = "startDate",required = false)Long startDate,
            @RequestParam(value = "endDate",required = false)Long endDate,
            @RequestParam(value = "sortType",required = false)String sortType,
            @RequestParam(value = "menuId",required = false)Long menuId
           ) {
        try {
            List<MenuDetailsDto> menus = planedMenusService.getMenusByDateRange( userId,startDate, endDate, sortType, menuId);
            return new ResponseEntity<>(menus, HttpStatus.OK);
        } catch (Exception e) {
            throw e;
            //return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{menuId}")
    public ResponseEntity<MenuDetailsDto> getById(@PathVariable String menuId) throws Exception {
            MenuDetailsDto menuDetailsDto= planedMenusService.getMenuById(Long.valueOf(menuId));
            return new ResponseEntity<>(menuDetailsDto, HttpStatus.OK);
    }

    @PutMapping()
    public ResponseEntity<Void> updateGroceryList(@RequestBody GroceryDetailsUpdatedDto groceryDetailsDto)  {
        planedMenusService.updatedMenuPromotions(groceryDetailsDto);
        return new ResponseEntity<>( HttpStatus.OK);
    }
    @GetMapping("/getAllGroceryDetails")
    public ResponseEntity<List<GroceryDetailsDto>> getALlGroceryDetailsDto(@RequestParam(required = false) Long menuId) throws Exception {
        List<GroceryDetailsDto> allGroceryDetailsDto= planedMenusService.getALlGroceryDetailsDto(menuId);
        return new ResponseEntity<>(allGroceryDetailsDto, HttpStatus.OK);

    }

}
