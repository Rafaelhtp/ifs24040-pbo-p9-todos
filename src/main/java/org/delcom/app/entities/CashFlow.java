package org.delcom.app.entities;

import java.time.LocalDateTime;
import java.util.UUID;

// Import anotasi JPA dan Hibernate
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import jakarta.persistence.Column;

@Entity 
public class CashFlow {

    @Id 
    @GeneratedValue(strategy = GenerationType.AUTO) 
    private UUID id;

    private String type; 
    private String source; 
    private String label; 
    private Integer amount;
    private String description;

    @CreationTimestamp // Diatur otomatis oleh JPA
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp // Diatur otomatis oleh JPA
    private LocalDateTime updatedAt;
    
    // --- FIX: Method ditambahkan kembali agar CashFlowTests.java lolos ---
    /**
     * Method manual untuk timestamp (diperlukan oleh CashFlowTests.java).
     */
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Method manual untuk timestamp (diperlukan oleh CashFlowTests.java).
     */
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
    // --- Akhir dari FIX ---

    public CashFlow() {
    }

    public CashFlow(String type, String source, String label, Integer amount, String description) {
        this.type = type;
        this.source = source;
        this.label = label;
        this.amount = amount;
        this.description = description;
    }

    // Getters
    public UUID getId() { return id; }
    public String getType() { return type; }
    public String getSource() { return source; }
    public String getLabel() { return label; }
    public Integer getAmount() { return amount; }
    public String getDescription() { return description; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    // Setters
    public void setId(UUID id) { this.id = id; }
    public void setType(String type) { this.type = type; }
    public void setSource(String source) { this.source = source; }
    public void setLabel(String label) { this.label = label; }
    public void setAmount(Integer amount) { this.amount = amount; }
    public void setDescription(String description) { this.description = description; }
}