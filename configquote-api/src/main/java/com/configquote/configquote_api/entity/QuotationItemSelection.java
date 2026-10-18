package com.configquote.configquote_api.entity;

import java.math.BigDecimal;

import jakarta.persistence.*;

@Entity
@Table(name = "quotation_item_selections")
public class QuotationItemSelection {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "quotation_item_id", nullable = false)
	private Long quotationItemId;

	@Column(name = "option_name", nullable = false)
	private String optionName;

	@Column(name = "selected_value", nullable = false)
	private String selectedValue;

	@Column(name = "additional_price", nullable = false)
	private BigDecimal additionalPrice;

	public Long getId() {
		return id;
	}

	public Long getQuotationItemId() {
		return quotationItemId;
	}

	public void setQuotationItemId(Long quotationItemId) {
		this.quotationItemId = quotationItemId;
	}

	public String getOptionName() {
		return optionName;
	}

	public void setOptionName(String optionName) {
		this.optionName = optionName;
	}

	public String getSelectedValue() {
		return selectedValue;
	}

	public void setSelectedValue(String selectedValue) {
		this.selectedValue = selectedValue;
	}

	public BigDecimal getAdditionalPrice() {
		return additionalPrice;
	}

	public void setAdditionalPrice(BigDecimal additionalPrice) {
		this.additionalPrice = additionalPrice;
	}
}
