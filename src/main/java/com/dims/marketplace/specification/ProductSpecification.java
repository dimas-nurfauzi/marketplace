package com.dims.marketplace.specification;

import com.dims.marketplace.dto.enums.Category;
import com.dims.marketplace.entity.Product;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.UUID;

public class ProductSpecification {

    public static Specification<Product> hasMinPrice(BigDecimal minPrice) {
        return (root, query, cb) ->
                minPrice == null ? null :
                        cb.greaterThanOrEqualTo(
                                root.join("variants").get("price"), minPrice
                        );
    }

    public static Specification<Product> hasMaxPrice(BigDecimal maxPrice) {
        return (root, query, cb) ->
                maxPrice == null ? null :
                        cb.lessThanOrEqualTo(
                                root.join("variants").get("price"), maxPrice
                        );
    }

    public static Specification<Product> hasCategory(Category category) {
        return (root, query, cb) ->
                category == null ? null :
                        cb.equal(root.get("category"), category);
    }

    public static Specification<Product> hasName(String name) {
        return (root, query, cb) ->
                name == null ? null :
                        cb.like(
                                cb.lower(root.get("name")),
                                "%" + name.toLowerCase() + "%"
                        );
    }

    public static Specification<Product> hasSeller(UUID sellerId) {
        return (root, query, cb) ->
                sellerId == null ? null :
                        cb.equal(root.join("seller").get("id"), sellerId);
    }
}