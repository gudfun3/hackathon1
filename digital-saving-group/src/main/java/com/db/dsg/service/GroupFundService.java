package com.db.dsg.service;

import com.db.dsg.model.GroupFund;
import com.db.dsg.repository.GroupFundRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class GroupFundService {

    private final GroupFundRepository groupFundRepository;
    public GroupFund getGroupFund(Long groupId) {
        return groupFundRepository.findByGroup_Id(groupId)
                .orElseThrow(() -> new IllegalArgumentException("Group fund not found for groupId: " + groupId));
    }

    public void addToFund(Long groupId, BigDecimal amount) {
        GroupFund fund = getOrThrow(groupId);
        fund.setBalance(fund.getBalance().add(amount));
        fund.setLastUpdated(LocalDate.now());
        groupFundRepository.save(fund);
    }

    public void subtractFromFund(Long groupId, BigDecimal amount) {
        GroupFund fund = getOrThrow(groupId);
        fund.setBalance(fund.getBalance().subtract(amount));
        fund.setLastUpdated(LocalDate.now());
        groupFundRepository.save(fund);
    }

    public void addProfit(Long groupId, BigDecimal amount) {
        GroupFund fund = getOrThrow(groupId);
        fund.setProfit(fund.getProfit().add(amount));
        fund.setBalance(fund.getBalance().add(amount));
        fund.setLastUpdated(LocalDate.now());
        groupFundRepository.save(fund);
    }

    public void addLoss(Long groupId, BigDecimal amount) {
        GroupFund fund = getOrThrow(groupId);
        fund.setLoss(fund.getLoss().add(amount));
        fund.setBalance(fund.getBalance().subtract(amount));
        fund.setLastUpdated(LocalDate.now());
        groupFundRepository.save(fund);
    }

    public BigDecimal getBalance(Long groupId) {
        return getOrThrow(groupId).getBalance();
    }

    private GroupFund getOrThrow(Long groupId) {
        return groupFundRepository.findByGroup_Id(groupId)
                .orElseThrow(() -> new IllegalArgumentException("Group fund not found for groupId " + groupId));
    }

    public void setBalance(Long groupId, BigDecimal newBalance) {
        GroupFund fund = getOrThrow(groupId);
        fund.setBalance(newBalance);
        fund.setLastUpdated(LocalDate.now());
        groupFundRepository.save(fund);
    }
}