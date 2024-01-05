package com.example.grocery.utils;

import com.example.grocery.dto.MenuFilterRequest;
import com.example.grocery.entity.PlanedMenus;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class MenuSpecifications {



    public static Predicate emptyCheckMenus(Predicate predicate, Root<PlanedMenus> root, CriteriaBuilder cb, MenuFilterRequest filterRequest) {

        if (!filterRequest.getUserId().isEmpty()) {
            predicate = cb.and(predicate, cb.equal(root.get("userId"), filterRequest.getUserId()));
        }

        return predicate;
    }

    public static Specification<PlanedMenus> filterMenus(MenuFilterRequest filterRequest) {

        return (root, cq, cb) -> {
            Predicate p = cb.conjunction();

            if (filterRequest.getStartDate() != null && filterRequest.getEndDate() != null) {
                p = cb.and(p, cb.between(root.get("startDate"), filterRequest.getStartDate(), filterRequest.getEndDate()));
            } else if (filterRequest.getStartDate() != null) {
                p = cb.and(p, cb.greaterThanOrEqualTo(root.get("startDate"), filterRequest.getStartDate()));
            }

            p = emptyCheckMenus(p, root, cb, filterRequest);

            if (filterRequest.getSort().equals("ASC")) {
                cq.orderBy(cb.asc(root.get("startDate")));
            } else {
                cq.orderBy(cb.desc(root.get("startDate")));
            }

            return p;
        };
    }
}


