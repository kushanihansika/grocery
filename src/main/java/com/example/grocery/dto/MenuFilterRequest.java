package com.example.grocery.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MenuFilterRequest {

    private String userId;
    private Long startDate;
    private Long endDate;
    private SortType sort;
    private int pageSize;
    private int pageNumber;
}
