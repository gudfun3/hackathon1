package com.db.dsg.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupFund {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "group_id", nullable = false, unique = true)
    private Group group;

    private BigDecimal balance = BigDecimal.ZERO;

    private BigDecimal profit = BigDecimal.ZERO;

    private BigDecimal loss = BigDecimal.ZERO;

    private LocalDate lastUpdated = LocalDate.now();
}
