package com.configquote.configquote_api.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.configquote.configquote_api.dto.CustomerRequest;
import com.configquote.configquote_api.dto.CustomerResponse;
import com.configquote.configquote_api.entity.Customer;
import com.configquote.configquote_api.exception.CustomerNotFoundException;
import com.configquote.configquote_api.exception.DuplicateCustomerCodeException;
import com.configquote.configquote_api.repository.CustomerRepository;

@Service
public class CustomerService {

	private final CustomerRepository customerRepository;

	public CustomerService(CustomerRepository customerRepository) {
		this.customerRepository = customerRepository;
	}

	@Transactional(readOnly = true)
	public List<CustomerResponse> getAllCustomers() {

		return customerRepository.findAll().stream().map(this::toResponse).toList();
	}

	@Transactional(readOnly = true)
	public CustomerResponse getCustomerById(Long id) {

		Customer customer = findCustomer(id);

		return toResponse(customer);
	}

	@Transactional
	public CustomerResponse createCustomer(CustomerRequest request) {

		if (customerRepository.existsByCustomerCode(request.customerCode())) {

			throw new DuplicateCustomerCodeException(request.customerCode());
		}

		Customer customer = new Customer();

		applyRequest(customer, request);

		Customer saved = customerRepository.saveAndFlush(customer);

		return toResponse(saved);
	}

	@Transactional
	public CustomerResponse updateCustomer(Long id, CustomerRequest request) {

		Customer customer = findCustomer(id);

		if (customerRepository.existsByCustomerCodeAndIdNot(request.customerCode(), id)) {

			throw new DuplicateCustomerCodeException(request.customerCode());
		}

		applyRequest(customer, request);

		Customer saved = customerRepository.saveAndFlush(customer);

		return toResponse(saved);
	}

	@Transactional
	public void deleteCustomer(Long id) {

		Customer customer = findCustomer(id);

		customerRepository.delete(customer);
	}

	private Customer findCustomer(Long id) {

		return customerRepository.findById(id).orElseThrow(() -> new CustomerNotFoundException(id));
	}

	private void applyRequest(Customer customer, CustomerRequest request) {

		customer.setCustomerCode(request.customerCode());
		customer.setCompanyName(request.companyName());
		customer.setContactName(request.contactName());
		customer.setEmail(request.email());
		customer.setPhone(request.phone());
		customer.setAddress(request.address());
	}

	private CustomerResponse toResponse(Customer customer) {

		return new CustomerResponse(customer.getId(), customer.getCustomerCode(), customer.getCompanyName(),
				customer.getContactName(), customer.getEmail(), customer.getPhone(), customer.getAddress(),
				customer.getCreatedAt());
	}
}
