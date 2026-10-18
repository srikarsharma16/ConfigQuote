package com.configquote.configquote_api.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.configquote.configquote_api.dto.*;
import com.configquote.configquote_api.entity.*;
import com.configquote.configquote_api.repository.*;

import jakarta.persistence.EntityManager;

@Service
public class QuotationService {

	private final QuotationRepository quotationRepository;
	private final QuotationItemRepository itemRepository;
	private final QuotationItemSelectionRepository selectionRepository;
	private final CustomerRepository customerRepository;
	private final ProductConfigurationService pricingService;
	private final ProductOptionValueRepository valueRepository;
	private final EntityManager entityManager;

	public QuotationService(QuotationRepository quotationRepository, QuotationItemRepository itemRepository,
			QuotationItemSelectionRepository selectionRepository, CustomerRepository customerRepository,
			ProductConfigurationService pricingService, ProductOptionValueRepository valueRepository,
			EntityManager entityManager) {

		this.quotationRepository = quotationRepository;
		this.itemRepository = itemRepository;
		this.selectionRepository = selectionRepository;
		this.customerRepository = customerRepository;
		this.pricingService = pricingService;
		this.valueRepository = valueRepository;
		this.entityManager = entityManager;
	}

	@Transactional
	public QuotationResponse createQuotation(QuotationRequest request) {

		if (!customerRepository.existsById(request.customerId())) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found");
		}

		Quotation quotation = new Quotation();
		quotation.setQuoteNumber("Q-" + UUID.randomUUID());
		quotation.setCustomerId(request.customerId());
		quotation.setStatus("DRAFT");
		quotation.setTotalAmount(BigDecimal.ZERO);

		quotation = quotationRepository.saveAndFlush(quotation);

		BigDecimal quotationTotal = BigDecimal.ZERO;

		for (QuotationRequest.ItemRequest requestItem : request.items()) {

			// Reuse the existing validated pricing engine.
			PriceCalculationResponse price = pricingService.calculatePrice(requestItem.productId(),
					new PriceCalculationRequest(requestItem.selectedValueIds()));

			BigDecimal lineTotal = price.totalPrice().multiply(BigDecimal.valueOf(requestItem.quantity()));

			QuotationItem item = new QuotationItem();
			item.setQuotationId(quotation.getId());
			item.setProductId(requestItem.productId());
			item.setProductName(price.productName());
			item.setQuantity(requestItem.quantity());
			item.setUnitPrice(price.totalPrice());
			item.setLineTotal(lineTotal);

			item = itemRepository.saveAndFlush(item);

			// Save selected option names and prices as snapshots.
			List<ProductOptionValue> availableValues = valueRepository.findByOption_Product_Id(requestItem.productId());

			Map<Long, ProductOptionValue> valuesById = availableValues.stream()
					.collect(Collectors.toMap(ProductOptionValue::getId, Function.identity()));

			for (Long valueId : requestItem.selectedValueIds()) {

				ProductOptionValue selected = valuesById.get(valueId);

				// The pricing service has already validated the IDs.
				QuotationItemSelection snapshot = new QuotationItemSelection();

				snapshot.setQuotationItemId(item.getId());
				snapshot.setOptionName(selected.getOption().getName());
				snapshot.setSelectedValue(selected.getValue());
				snapshot.setAdditionalPrice(selected.getAdditionalPrice());

				selectionRepository.save(snapshot);
			}

			quotationTotal = quotationTotal.add(lineTotal);
		}

		quotation.setTotalAmount(quotationTotal);
		quotationRepository.saveAndFlush(quotation);

		entityManager.refresh(quotation);

		return toResponse(quotation);
	}

	@Transactional(readOnly = true)
	public List<QuotationResponse> getAllQuotations() {

		return quotationRepository.findAll().stream().map(this::toResponse).toList();
	}

	@Transactional(readOnly = true)
	public QuotationResponse getQuotationById(Long id) {

		return toResponse(findQuotation(id));
	}

	@Transactional
	public QuotationResponse updateStatus(Long id, QuotationStatusRequest request) {

		Quotation quotation = findQuotation(id);

		String current = quotation.getStatus();
		String target = request.status().name();

		boolean allowed = (current.equals("DRAFT") && target.equals("SENT"))
				|| (current.equals("SENT") && Set.of("ACCEPTED", "REJECTED").contains(target));

		if (!allowed) {
			throw new ResponseStatusException(HttpStatus.CONFLICT,
					"Invalid quotation status transition: " + current + " -> " + target);
		}

		quotation.setStatus(target);
		quotationRepository.saveAndFlush(quotation);

		return toResponse(quotation);
	}

	private Quotation findQuotation(Long id) {

		return quotationRepository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Quotation not found: " + id));
	}

	private QuotationResponse toResponse(Quotation quotation) {

		List<QuotationResponse.ItemResponse> items = new ArrayList<>();

		for (QuotationItem item : itemRepository.findByQuotationIdOrderByIdAsc(quotation.getId())) {

			List<QuotationResponse.SelectionResponse> selections = selectionRepository
					.findByQuotationItemIdOrderByIdAsc(item.getId()).stream()
					.map(s -> new QuotationResponse.SelectionResponse(s.getOptionName(), s.getSelectedValue(),
							s.getAdditionalPrice()))
					.toList();

			items.add(new QuotationResponse.ItemResponse(item.getId(), item.getProductId(), item.getProductName(),
					item.getQuantity(), item.getUnitPrice(), item.getLineTotal(), selections));
		}

		return new QuotationResponse(quotation.getId(), quotation.getQuoteNumber(), quotation.getCustomerId(),
				quotation.getStatus(), quotation.getTotalAmount(), quotation.getCreatedAt(), items);
	}
}
