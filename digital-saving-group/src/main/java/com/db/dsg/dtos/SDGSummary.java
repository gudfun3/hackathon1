package com.db.dsg.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class SDGSummary {
    private int totalWomenEmpowered;
    private int totalJobsCreated;
    private BigDecimal totalSavingsGrowth;
}
