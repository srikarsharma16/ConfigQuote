package com.configquote.configquote_api.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.configquote.configquote_api.entity.QuotationItem;

public interface QuotationItemRepository extends JpaRepository<QuotationItem, Long> {

	List<QuotationItem> findByQuotationIdOrderByIdAsc(Long quotationId);
}
