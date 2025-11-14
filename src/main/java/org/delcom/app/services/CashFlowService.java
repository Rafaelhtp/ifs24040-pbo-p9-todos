package org.delcom.app.services;

import java.util.List;
import java.util.UUID;
import org.delcom.app.entities.CashFlow;
import org.delcom.app.repositories.CashFlowRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

@Service 
public class CashFlowService {

    private final CashFlowRepository cashFlowRepository;

    @Autowired 
    public CashFlowService(CashFlowRepository cashFlowRepository) {
        this.cashFlowRepository = cashFlowRepository;
    }

    public CashFlow createCashFlow(String type, String source, String label, Integer amount, String description) {
        CashFlow newCashFlow = new CashFlow(type, source, label, amount, description);
        return cashFlowRepository.save(newCashFlow);
    }

    public List<CashFlow> getAllCashFlows(String keyword) {
        if (keyword != null && !keyword.trim().isEmpty()) {
            return cashFlowRepository.findByKeyword(keyword.trim());
        }
        return cashFlowRepository.findAll();
    }

    public CashFlow getCashFlowById(UUID id) {
        return cashFlowRepository.findById(id).orElse(null);
    }

    public List<String> getCashFlowLabels() {
        return cashFlowRepository.findDistinctLabels();
    }

    public CashFlow updateCashFlow(UUID id, String type, String source, String label, Integer amount,
            String description) {
        
        return cashFlowRepository.findById(id).map(existingCashFlow -> {
            existingCashFlow.setType(type);
            existingCashFlow.setSource(source);
            existingCashFlow.setLabel(label);
            existingCashFlow.setAmount(amount);
            existingCashFlow.setDescription(description);
            return cashFlowRepository.save(existingCashFlow);
        }).orElse(null);
    }

    public boolean deleteCashFlow(UUID id) {
        if (cashFlowRepository.existsById(id)) {
            cashFlowRepository.deleteById(id);
            return true;
        }
        return false;
    }
}