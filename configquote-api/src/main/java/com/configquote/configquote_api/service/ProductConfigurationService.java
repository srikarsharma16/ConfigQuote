package com.configquote.configquote_api.service;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.configquote.configquote_api.dto.OptionResponse;
import com.configquote.configquote_api.dto.OptionResponse.ValueResponse;
import com.configquote.configquote_api.dto.PriceCalculationRequest;
import com.configquote.configquote_api.dto.PriceCalculationResponse;
import com.configquote.configquote_api.entity.Product;
import com.configquote.configquote_api.entity.ProductOption;
import com.configquote.configquote_api.entity.ProductOptionValue;
import com.configquote.configquote_api.exception.ProductNotFoundException;
import com.configquote.configquote_api.repository.ProductOptionRepository;
import com.configquote.configquote_api.repository.ProductOptionValueRepository;
import com.configquote.configquote_api.repository.ProductRepository;

@Service
public class ProductConfigurationService {

	private final ProductRepository productRepository;
	private final ProductOptionRepository optionRepository;
	private final ProductOptionValueRepository valueRepository;

	public ProductConfigurationService(ProductRepository productRepository, ProductOptionRepository optionRepository,
			ProductOptionValueRepository valueRepository) {

		this.productRepository = productRepository;
		this.optionRepository = optionRepository;
		this.valueRepository = valueRepository;
	}

	@Transactional(readOnly = true)
	public List<OptionResponse> getProductOptions(Long productId) {

		getProductOrThrow(productId);

		return optionRepository.findByProduct_IdOrderByIdAsc(productId).stream().map(option -> {

			List<ValueResponse> values = valueRepository.findByOption_IdOrderByIdAsc(option.getId()).stream()
					.map(value -> new ValueResponse(value.getId(), value.getValue(), value.getAdditionalPrice()))
					.toList();

			return new OptionResponse(option.getId(), option.getName(), option.isRequired(), values);
		}).toList();
	}

	@Transactional(readOnly = true)
	public PriceCalculationResponse calculatePrice(Long productId, PriceCalculationRequest request) {

		Product product = getProductOrThrow(productId);

		List<Long> selectedIds = request.selectedValueIds();

		Set<Long> uniqueIds = new HashSet<>(selectedIds);

		if (uniqueIds.size() != selectedIds.size()) {
			throw badRequest("Duplicate option values are not allowed");
		}

		List<ProductOption> options = optionRepository.findByProduct_IdOrderByIdAsc(productId);

		List<ProductOptionValue> availableValues = valueRepository.findByOption_Product_Id(productId);

		Map<Long, ProductOptionValue> valuesById = availableValues.stream()
				.collect(Collectors.toMap(ProductOptionValue::getId, Function.identity()));

		Set<Long> selectedOptionIds = new HashSet<>();

		BigDecimal optionsPrice = BigDecimal.ZERO;

		for (Long selectedId : selectedIds) {

			ProductOptionValue value = valuesById.get(selectedId);

			if (value == null) {
				throw badRequest("Invalid option value ID for this product: " + selectedId);
			}

			Long optionId = value.getOption().getId();

			if (!selectedOptionIds.add(optionId)) {
				throw badRequest("Select only one value per option");
			}

			optionsPrice = optionsPrice.add(value.getAdditionalPrice());
		}

		for (ProductOption option : options) {

			if (option.isRequired() && !selectedOptionIds.contains(option.getId())) {

				throw badRequest("Required option missing: " + option.getName());
			}
		}

		BigDecimal totalPrice = product.getBasePrice().add(optionsPrice);

		return new PriceCalculationResponse(product.getId(), product.getName(), product.getBasePrice(), optionsPrice,
				totalPrice);
	}

	private Product getProductOrThrow(Long productId) {
		return productRepository.findById(productId).orElseThrow(() -> new ProductNotFoundException(productId));
	}

	private ResponseStatusException badRequest(String message) {
		return new ResponseStatusException(HttpStatus.BAD_REQUEST, message);
	}
}
