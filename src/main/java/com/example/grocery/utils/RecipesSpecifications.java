package com.example.grocery.utils;
import com.example.grocery.dto.RecipesFilterRequest;
import com.example.grocery.entity.Recipe;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

public class RecipesSpecifications {


    public static Specification<Recipe> generateFilterRecipeQuery(RecipesFilterRequest filterRequest) {

        return (root, cq, cb) -> {
            Predicate p = cb.conjunction();


            if (filterRequest.getMenueType() != null) {
                p = cb.and(p, cb.equal(root.get("menuType"), filterRequest.getMenueType()));
            }
            if (filterRequest.getType()!= null) {
                p = cb.and(p,
                        cb.equal(root.get("type"), filterRequest.getType()));
            }
            if (filterRequest.getServings() != null) {
                p = cb.and(p, cb.equal(root.get("servings"), filterRequest.getServings()));
            }
            return p;
        };
    }
}
