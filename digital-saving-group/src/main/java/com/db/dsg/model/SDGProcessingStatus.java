package com.db.dsg.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "sdg_processing_status", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"referenceId", "entityType"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SDGProcessingStatus {

    @Id
    @GeneratedValue
    private Long id;

    private Long referenceId; // Loan ID or Deposit ID

    @Enumerated(EnumType.STRING)
    private EntityType entityType; // LOAN or DEPOSIT

    private boolean processed = false;

    private LocalDateTime processedAt;

    public enum EntityType {
        LOAN, DEPOSIT
    }
}
