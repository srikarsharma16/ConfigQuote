package com.configquote.configquote_api.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.configquote.configquote_api.dto.CustomerRequest;
import com.configquote.configquote_api.dto.CustomerResponse;
import com.configquote.configquote_api.service.CustomerService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/customers")
@Tag(name = "Customer Management", description = "Customer CRUD operations for ConfigQuote")
public class CustomerController {

	private final CustomerService customerService;

	public CustomerController(CustomerService customerService) {
		this.customerService = customerService;
	}

	@Operation(summary = "Get all customers")
	@GetMapping
	public List<CustomerResponse> getAllCustomers() {

		return customerService.getAllCustomers();
	}

	@Operation(summary = "Get customer by ID")
	@GetMapping("/{id}")
	public CustomerResponse getCustomerById(@PathVariable Long id) {

		return customerService.getCustomerById(id);
	}

	@Operation(summary = "Create a customer")
	@PostMapping
	public ResponseEntity<CustomerResponse> createCustomer(@Valid @RequestBody CustomerRequest request) {

		CustomerResponse created = customerService.createCustomer(request);

		return ResponseEntity.created(URI.create("/api/customers/" + created.id())).body(created);
	}

	@Operation(summary = "Update an existing customer")
	@PutMapping("/{id}")
	public ResponseEntity<CustomerResponse> updateCustomer(@PathVariable Long id,
			@Valid @RequestBody CustomerRequest request) {

		return ResponseEntity.ok(customerService.updateCustomer(id, request));
	}

	@Operation(summary = "Delete a customer")
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {

		customerService.deleteCustomer(id);

		return ResponseEntity.noContent().build();
	}
}
