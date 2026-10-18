package com.configquote.configquote_api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.configquote.configquote_api.entity.ProductOption;

public interface ProductOptionRepository
        extends JpaRepository<ProductOption, Long> {

    List<ProductOption> findByProduct_IdOrderByIdAsc(Long productId);
}
