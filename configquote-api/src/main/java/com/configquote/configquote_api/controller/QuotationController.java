package com.configquote.configquote_api.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.configquote.configquote_api.dto.*;
import com.configquote.configquote_api.service.QuotationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/quotations")
@Tag(name = "Quotation Management", description = "Create and manage customer quotations")
public class QuotationController {

	private final QuotationService quotationService;

	public QuotationController(QuotationService quotationService) {
		this.quotationService = quotationService;
	}

	@Operation(summary = "Create a quotation")
	@PostMapping
	public ResponseEntity<QuotationResponse> createQuotation(@Valid @RequestBody QuotationRequest request) {

		QuotationResponse created = quotationService.createQuotation(request);

		return ResponseEntity.created(URI.create("/api/quotations/" + created.id())).body(created);
	}

	@Operation(summary = "Get all quotations")
	@GetMapping
	public List<QuotationResponse> getAllQuotations() {
		return quotationService.getAllQuotations();
	}

	@Operation(summary = "Get quotation by ID")
	@GetMapping("/{id}")
	public QuotationResponse getQuotationById(@PathVariable Long id) {

		return quotationService.getQuotationById(id);
	}

	@Operation(summary = "Update quotation status")
	@PatchMapping("/{id}/status")
	public QuotationResponse updateStatus(@PathVariable Long id, @Valid @RequestBody QuotationStatusRequest request) {

		return quotationService.updateStatus(id, request);
	}
}
