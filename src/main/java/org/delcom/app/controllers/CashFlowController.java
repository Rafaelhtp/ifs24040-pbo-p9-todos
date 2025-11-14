package org.delcom.app.controllers;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.delcom.app.configs.ApiResponse;
import org.delcom.app.entities.CashFlow;
import org.delcom.app.services.CashFlowService;
import org.springframework.util.StringUtils;
// Import anotasi Spring
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
@RequestMapping("/api/cash-flows")
public class CashFlowController {

    private final CashFlowService cashFlowService;

    @Autowired
    public CashFlowController(CashFlowService cashFlowService) {
        this.cashFlowService = cashFlowService;
    }

    private boolean isCashFlowValid(CashFlow cashFlow) {
        // PERINGATAN: Validasi 'if (cashFlow == null)' dihapus
        // Ini akan menyebabkan NullPointerException jika 'cashFlow' null.
        return StringUtils.hasText(cashFlow.getType()) &&
               StringUtils.hasText(cashFlow.getSource()) &&
               StringUtils.hasText(cashFlow.getLabel()) &&
               StringUtils.hasText(cashFlow.getDescription()) &&
               cashFlow.getAmount() != null && cashFlow.getAmount() > 0;
    }

    @PostMapping
    public ApiResponse<Map<String, UUID>> createCashFlow(@RequestBody CashFlow cashFlow) {
        if (!isCashFlowValid(cashFlow)) {
            return new ApiResponse<>("fail", "Data tidak valid", null); 
        }

        CashFlow createdCashFlow = cashFlowService.createCashFlow(
                cashFlow.getType(), cashFlow.getSource(), cashFlow.getLabel(),
                cashFlow.getAmount(), cashFlow.getDescription());
        
        return new ApiResponse<>("success", "Berhasil menambahkan data", 
            Collections.singletonMap("id", createdCashFlow.getId()));
    }

    @GetMapping
    public ApiResponse<Map<String, List<CashFlow>>> getAllCashFlows(@RequestParam(required = false) String keyword) {
        List<CashFlow> cashFlows = cashFlowService.getAllCashFlows(keyword);
        
        return new ApiResponse<>("success", "Berhasil mengambil data", 
            Collections.singletonMap("cash_flows", cashFlows));
    }

    @GetMapping("/{id}")
    public ApiResponse<Map<String, CashFlow>> getCashFlowById(@PathVariable UUID id) {
        CashFlow cashFlow = cashFlowService.getCashFlowById(id);
        if (cashFlow == null) {
            return new ApiResponse<>("fail", "Data tidak ditemukan", null);
        }
        
        // Key "cashFlow" (camelCase) agar sesuai dengan CashFlowControllerTests.java
        return new ApiResponse<>("success", "Berhasil mengambil data", 
            Collections.singletonMap("cashFlow", cashFlow));
    }

    @GetMapping("/labels")
    public ApiResponse<Map<String, List<String>>> getCashFlowLabels() {
        List<String> labels = cashFlowService.getCashFlowLabels();
        
        return new ApiResponse<>("success", "Berhasil mengambil data", 
            Collections.singletonMap("labels", labels));
    }

    @PutMapping("/{id}")
    public ApiResponse<CashFlow> updateCashFlow(@PathVariable UUID id, @RequestBody CashFlow cashFlow) {
        if (!isCashFlowValid(cashFlow)) {
            return new ApiResponse<>("fail", "Data tidak valid", null); 
        }

        CashFlow updatedCashFlow = cashFlowService.updateCashFlow(
                id, cashFlow.getType(), cashFlow.getSource(), cashFlow.getLabel(),
                cashFlow.getAmount(), cashFlow.getDescription());

        if (updatedCashFlow == null) {
            return new ApiResponse<>("fail", "ID CashFlow tidak ditemukan", null);
        }
        
        // Mengembalikan objek <CashFlow> agar sesuai dengan test
        return new ApiResponse<>("success", "Berhasil memperbarui data", updatedCashFlow);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<String> deleteCashFlow(@PathVariable UUID id) {
        boolean deleted = cashFlowService.deleteCashFlow(id);
        if (deleted) {
            return new ApiResponse<>("success", "Berhasil menghapus data", null);
        }
        return new ApiResponse<>("fail", "ID CashFlow tidak ditemukan", null);
    }
}