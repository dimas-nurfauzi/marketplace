package com.dims.marketplace.service.impl;

import com.dims.marketplace.dto.product.create.VariantRequest;
import com.dims.marketplace.dto.product.variant.UpdateVariantRequest;
import com.dims.marketplace.entity.Product;
import com.dims.marketplace.entity.Variant;
import com.dims.marketplace.exceptions.DataNotFoundException;
import com.dims.marketplace.repository.ProductRepository;
import com.dims.marketplace.repository.VariantRepository;
import com.dims.marketplace.service.inter.VariantService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class VariantServiceImpl implements VariantService {

    private final VariantRepository variantRepository;
    private final ProductRepository productRepository;

    @Override //error: stock -1, empty sku are permissible,
    public Variant updateVariant(UUID id, UpdateVariantRequest request) {

        Variant variant = variantRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Variant not found"));

        if (request.getSku() != null) {
            variant.setSku(request.getSku());
        }

        if (request.getSize() != null) {
            variant.setSize(request.getSize());
        }

        if (request.getPrice() != null) {
            variant.setPrice(request.getPrice());
        }

        if (request.getStock() != null) {
            variant.setStock(request.getStock());
        }

        return variantRepository.save(variant);
    }

    @Override
    public void deleteVariant(UUID id) {

        Variant variant = variantRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Variant not found"));

        variantRepository.delete(variant);
    }

    @Override // error: showing empty list when product id is wrong
    public List<Variant> getVariantsByProduct(UUID productId) {

        return variantRepository.findByProductId(productId);
    }

    @Override
    public Variant addVariantToProduct(UUID productId, VariantRequest request) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new DataNotFoundException("Product not found"));

        Variant variant = new Variant();
        variant.setSku(request.getSku());
        variant.setSize(request.getSize());
        variant.setPrice(request.getPrice());
        variant.setStock(request.getStock());
        variant.setProduct(product);

        return variantRepository.save(variant);
    }
}