package com.example.grocery.utils;

import com.example.grocery.dto.MenuFilterRequest;
import com.example.grocery.dto.SortType;
import com.example.grocery.entity.PlanedMenus;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class MenuSpecifications {


    public static Specification<PlanedMenus> filterMenus(MenuFilterRequest filterRequest) {

        return (root, cq, cb) -> {
            Predicate p = cb.conjunction();

            if (filterRequest.getStartDate() != null && filterRequest.getEndDate() != null) {
                p = cb.and(p, cb.between(root.get("startDate"), filterRequest.getStartDate(), filterRequest.getEndDate()));
            } else if (filterRequest.getStartDate() != null) {
                p = cb.and(p, cb.greaterThanOrEqualTo(root.get("startDate"), filterRequest.getStartDate()));
            }
            if (filterRequest.getUserId() != null && !filterRequest.getUserId().isEmpty()) {
                p = cb.and(p, cb.equal(root.get("userId"), filterRequest.getUserId()));
            }
           if(filterRequest.getSort() != null){

               if (filterRequest.getSort().equals(SortType.DESC)) {
                   cq.orderBy(cb.desc(root.get("startDate")));
               }
               if (filterRequest.getSort().equals(SortType.ASC)) {
                   cq.orderBy(cb.asc(root.get("startDate")));
               }
           }

            return p;
        };
    }
}


