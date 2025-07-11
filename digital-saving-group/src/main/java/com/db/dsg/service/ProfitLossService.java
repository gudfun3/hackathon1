package com.db.dsg.service;

import com.db.dsg.model.Group;
import com.db.dsg.model.ProfitLossRecord;
import com.db.dsg.repository.ProfitLossRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProfitLossService {

    private final ProfitLossRecordRepository profitLossRepo;

    public void recordPrincipalAndInterest(Group group, BigDecimal principal, BigDecimal interest, String desc) {
        ProfitLossRecord record = new ProfitLossRecord();
        record.setGroup(group);
        record.setPrincipalPaid(principal);
        record.setInterestEarned(interest);
        record.setDescription(desc);
        profitLossRepo.save(record);
    }

    public void recordLoss(Group group, BigDecimal lossAmount, String desc) {
        ProfitLossRecord record = new ProfitLossRecord();
        record.setGroup(group);
        record.setLossAmount(lossAmount);
        record.setDescription(desc);
        profitLossRepo.save(record);
    }

    public List<ProfitLossRecord> getGroupProfitLoss(Long groupId) {
        return profitLossRepo.findByGroup_IdOrderByDateDesc(groupId);
    }
}

