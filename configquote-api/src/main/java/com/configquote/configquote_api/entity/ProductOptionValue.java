package com.configquote.configquote_api.entity;

import java.math.BigDecimal;

import jakarta.persistence.*;

@Entity
@Table(name = "product_option_values")
public class ProductOptionValue {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "option_id", nullable = false)
	private ProductOption option;

	@Column(nullable = false, length = 100)
	private String value;

	@Column(name = "additional_price", nullable = false, precision = 12, scale = 2)
	private BigDecimal additionalPrice;

	public ProductOptionValue() {
	}

	public Long getId() {
		return id;
	}

	public ProductOption getOption() {
		return option;
	}

	public void setOption(ProductOption option) {
		this.option = option;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public BigDecimal getAdditionalPrice() {
		return additionalPrice;
	}

	public void setAdditionalPrice(BigDecimal additionalPrice) {
		this.additionalPrice = additionalPrice;
	}
}
