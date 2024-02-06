package com.example.grocery.entity;

import com.example.grocery.dto.Status;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "planed_menus")
@Data
public class PlanedMenus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String userId;
    private int days;
    private LocalDate startDate;
    private LocalDate endData;
    private Long generatedGroceryListId;
    private Status status;


    @OneToMany(mappedBy = "planedMenus",cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Days> daysList = new ArrayList<>();

    public void setDaysList(List<Days> daysList) {
        daysList.forEach(days1 -> days1.setPlanedMenus(this));
        this.daysList = daysList;
    }
}
