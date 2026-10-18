package com.configquote.configquote_api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.configquote.configquote_api.entity.ProductOptionValue;

public interface ProductOptionValueRepository
        extends JpaRepository<ProductOptionValue, Long> {

    List<ProductOptionValue> findByOption_IdOrderByIdAsc(Long optionId);

    List<ProductOptionValue> findByOption_Product_Id(Long productId);
}
