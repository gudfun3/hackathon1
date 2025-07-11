package com.db.dsg.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "sdg_mapping")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SDGMapping {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private SDGGoal goal; // Enum like NO_POVERTY, GENDER_EQUALITY

    private String description;

    private String keyword;

    /**
     * Can be: "DEPOSIT_TYPE", "LOAN_PURPOSE", "LOAN_PURPOSE_TAG", etc.
     */
    private String actionType;
}
