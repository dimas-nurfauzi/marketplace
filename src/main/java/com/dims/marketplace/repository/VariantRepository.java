package com.dims.marketplace.repository;

import com.dims.marketplace.entity.Variant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface VariantRepository extends JpaRepository<Variant, UUID> {
    boolean existsBySku(String sku);
    List<Variant> findByProductId(UUID productId);}
