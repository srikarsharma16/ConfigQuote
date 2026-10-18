package com.configquote.configquote_api.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.configquote.configquote_api.dto.ProductRequest;
import com.configquote.configquote_api.entity.Product;
import com.configquote.configquote_api.exception.DuplicateSkuException;
import com.configquote.configquote_api.exception.ProductNotFoundException;
import com.configquote.configquote_api.repository.ProductRepository;

@Service
public class ProductService {

	private final ProductRepository productRepository;

	public ProductService(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}

	@Transactional(readOnly = true)
	public List<Product> getAllProducts() {
		return productRepository.findAll();
	}

	@Transactional(readOnly = true)
	public Product getProductById(Long id) {
		return productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException(id));
	}

	@Transactional
	public Product createProduct(ProductRequest request) {

		if (productRepository.existsBySku(request.sku())) {
			throw new DuplicateSkuException(request.sku());
		}

		Product product = new Product();

		product.setSku(request.sku());
		product.setName(request.name());
		product.setDescription(request.description());
		product.setBasePrice(request.basePrice());

		return productRepository.saveAndFlush(product);
	}

	@Transactional
	public Product updateProduct(Long id, ProductRequest request) {

		Product product = productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException(id));

		if (productRepository.existsBySkuAndIdNot(request.sku(), id)) {
			throw new DuplicateSkuException(request.sku());
		}

		product.setSku(request.sku());
		product.setName(request.name());
		product.setDescription(request.description());
		product.setBasePrice(request.basePrice());

		return productRepository.saveAndFlush(product);
	}

	@Transactional
	public void deleteProduct(Long id) {

		Product product = productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException(id));

		productRepository.delete(product);
	}
}
