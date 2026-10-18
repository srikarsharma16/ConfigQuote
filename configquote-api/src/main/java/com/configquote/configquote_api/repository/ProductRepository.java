package com.configquote.configquote_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.configquote.configquote_api.entity.Product;

public interface ProductRepository
        extends JpaRepository<Product, Long> {

    boolean existsBySku(String sku);

    boolean existsBySkuAndIdNot(String sku, Long id);
}
