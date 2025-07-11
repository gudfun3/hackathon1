package com.db.dsg.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SavingSummaryResponse {
    private BigDecimal totalDeposited;
    private Long numberOfDeposits;
}
