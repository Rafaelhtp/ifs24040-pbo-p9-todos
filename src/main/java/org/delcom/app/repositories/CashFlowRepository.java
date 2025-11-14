package org.delcom.app.repositories;

import java.util.List;
import java.util.UUID;
import org.delcom.app.entities.CashFlow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CashFlowRepository extends JpaRepository<CashFlow, UUID> {

    @Query("SELECT cf FROM CashFlow cf WHERE " +
           "LOWER(cf.type) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(cf.source) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(cf.label) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(cf.description) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<CashFlow> findByKeyword(String keyword);

    @Query("SELECT DISTINCT cf.label FROM CashFlow cf ORDER BY cf.label")
    List<String> findDistinctLabels();
}