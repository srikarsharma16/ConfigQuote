package com.configquote.configquote_api.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.configquote.configquote_api.entity.QuotationItemSelection;

public interface QuotationItemSelectionRepository extends JpaRepository<QuotationItemSelection, Long> {

	List<QuotationItemSelection> findByQuotationItemIdOrderByIdAsc(Long quotationItemId);
}
