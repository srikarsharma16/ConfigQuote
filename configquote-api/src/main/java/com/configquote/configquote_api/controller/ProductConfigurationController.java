package com.configquote.configquote_api.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.configquote.configquote_api.dto.OptionResponse;
import com.configquote.configquote_api.dto.PriceCalculationRequest;
import com.configquote.configquote_api.dto.PriceCalculationResponse;
import com.configquote.configquote_api.service.ProductConfigurationService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(
	    name = "Product Configuration",
	    description = "Product options and configuration pricing"
	)
@RestController
@RequestMapping("/api/products/{productId}")
public class ProductConfigurationController {

    private final ProductConfigurationService configurationService;

    public ProductConfigurationController(
            ProductConfigurationService configurationService) {

        this.configurationService = configurationService;
    }

    @GetMapping("/options")
    public List<OptionResponse> getOptions(
            @PathVariable Long productId) {

        return configurationService.getProductOptions(productId);
    }

    @PostMapping("/calculate-price")
    public PriceCalculationResponse calculatePrice(
            @PathVariable Long productId,
            @Valid @RequestBody PriceCalculationRequest request) {

        return configurationService.calculatePrice(productId, request);
    }
}
