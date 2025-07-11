package com.db.dsg.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SDGImpact {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private Group group;

    @Enumerated(EnumType.STRING)
    private SDGGoal goal;

    private String description;

    private BigDecimal savingsGrowth;

    private Integer jobsCreated;

    private Integer womenEmpowered;

    private LocalDate impactDate;

    private String period; // e.g. 2025-07

    private Long referenceId;

    @Enumerated(EnumType.STRING)
    private SDGProcessingStatus.EntityType referenceType;
}