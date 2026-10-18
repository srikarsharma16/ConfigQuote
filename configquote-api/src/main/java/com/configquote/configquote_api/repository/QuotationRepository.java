
package com.configquote.configquote_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.configquote.configquote_api.entity.Quotation;

public interface QuotationRepository extends JpaRepository<Quotation, Long> {
}
