package com.configquote.configquote_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.configquote.configquote_api.entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

	boolean existsByCustomerCode(String customerCode);

	boolean existsByCustomerCodeAndIdNot(String customerCode, Long id);
}
