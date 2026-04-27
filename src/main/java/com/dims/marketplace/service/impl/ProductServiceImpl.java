package com.dims.marketplace.service.impl;

import com.dims.marketplace.dto.enums.Category;
import com.dims.marketplace.dto.enums.Role;
import com.dims.marketplace.dto.mapper.ProductMapper;
import com.dims.marketplace.dto.product.create.ProductRequest;
import com.dims.marketplace.dto.product.response.ProductDetailResponse;
import com.dims.marketplace.dto.product.response.ProductListResponse;
import com.dims.marketplace.dto.product.update.UpdateProductRequest;
import com.dims.marketplace.dto.product.variant.VariantResponse;
import com.dims.marketplace.entity.Product;
import com.dims.marketplace.entity.Variant;
import com.dims.marketplace.entity.User;
import com.dims.marketplace.exceptions.DataNotFoundException;
import com.dims.marketplace.exceptions.UnauthorizedException;
import com.dims.marketplace.repository.ProductRepository;
import com.dims.marketplace.repository.UserRepository;
import com.dims.marketplace.service.inter.ProductService;
import com.dims.marketplace.service.inter.UserService;
import com.dims.marketplace.specification.ProductSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Override
    @CacheEvict(value = "products", allEntries = true)
    public Product createProduct(ProductRequest request, String authenticatedEmail) {

        User seller = userRepository.findByEmail(authenticatedEmail)
                .orElseThrow(() -> new DataNotFoundException("Seller not found"));

        if (seller.getRole() != Role.SELLER) {
            throw new UnauthorizedException("User must be SELLER to create product");
        }

        Product product = new Product();
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setSeller(seller);
        product.setCategory(request.getCategory());

        List<Variant> variants = request.getVariants()
                .stream()
                .map(v -> {
                    Variant variant = new Variant();
                    variant.setSku(v.getSku());
                    variant.setPrice(v.getPrice());
                    variant.setSize(v.getSize());
                    variant.setStock(v.getStock());
                    variant.setProduct(product);
                    return variant;
                })
                .collect(Collectors.toList());

        product.setVariants(variants);

        return productRepository.save(product);
    }

    @Override // adding variant is included here
    @CacheEvict(value = {"products", "product"}, allEntries = true)
    public Product updateProduct(UUID id, UpdateProductRequest request) {

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Product not found"));

        if (request.getName() != null && !request.getName().isBlank()) {
            product.setName(request.getName());
        }

        if (request.getDescription() != null && !request.getDescription().isBlank()) {
            product.setDescription(request.getDescription());
        }

        if (request.getCategory() != null) {
            product.setCategory(request.getCategory());
        }

        if (request.getVariants() != null) {
            List<Variant> variants = request.getVariants()
                    .stream()
                    .map(v -> {
                        Variant variant = new Variant();
                        variant.setSku(v.getSku());
                        variant.setPrice(v.getPrice());
                        variant.setSize(v.getSize());
                        variant.setStock(v.getStock());
                        variant.setProduct(product);
                        return variant;
                    })
                    .collect(Collectors.toList());

            product.setVariants(variants);
        }

        return productRepository.save(product);
    }

    @Override
    @Cacheable(
            value = "products",
            key = "T(String).valueOf(#minPrice) + '-' + T(String).valueOf(#maxPrice) + '-' + #category + '-' + #name + '-' + #sellerId + '-' + #pageable.pageNumber + '-' + #pageable.pageSize"
    )
    public Page<ProductListResponse> getAllProducts(
            BigDecimal minPrice,
            BigDecimal maxPrice,
            Category category,
            String name,
            UUID sellerId,
            Pageable pageable) {

        Specification<Product> spec =
                ProductSpecification.hasMinPrice(minPrice)
                .and(ProductSpecification.hasMaxPrice(maxPrice))
                .and(ProductSpecification.hasCategory(category))
                .and(ProductSpecification.hasName(name))
                .and(ProductSpecification.hasSeller(sellerId));

        Page<Product> products = productRepository.findAll((root, query, cb) -> {
            if (query != null) {
                query.distinct(true);
            }
            return spec.toPredicate(root, query, cb);
        }, pageable);

        return products.map(ProductMapper::toListResponse);
    }

    @Override
    @Cacheable(value = "product", key = "#id")
    public ProductDetailResponse getProductById(UUID id) {

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Product not found"));

        return new ProductDetailResponse(
                product.getId(),
                product.getSeller().getId(),
                product.getName(),
                product.getDescription(),
                product.getCategory(),
                product.getCreatedAt(),
                product.getVariants().stream()
                        .map(v -> new VariantResponse(
                                v.getId(),
                                v.getSku(),
                                v.getSize(),
                                v.getPrice(),
                                v.getStock(),
                                v.getCreatedAt()
                        ))
                        .collect(Collectors.toList())
        );
    }

    @Override
    @CacheEvict(value = {"products", "product"}, allEntries = true)
    public void deleteProduct(UUID productId) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new DataNotFoundException("Product not found"));

        productRepository.delete(product);
    }
}