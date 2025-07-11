package com.db.dsg.service;

import com.db.dsg.dtos.SavingDepositRequest;
import com.db.dsg.dtos.SavingSummaryResponse;
import com.db.dsg.model.Member;
import com.db.dsg.model.SavingDeposit;
import com.db.dsg.repository.SavingDepositRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SavingDepositService {

    private final SavingDepositRepository savingDepositRepo;

    public SavingDeposit save(SavingDepositRequest req, Member member) {
        SavingDeposit deposit = new SavingDeposit();
        deposit.setAmount(req.getAmount());
        deposit.setRemarks(req.getRemarks());
        deposit.setMember(member);
        return savingDepositRepo.save(deposit);
    }

    public List<SavingDeposit> getDepositsForGroup(Long groupId) {
        return savingDepositRepo.findByMember_Group_Id(groupId);
    }

    public SavingSummaryResponse getGroupSavingSummary(Long groupId) {
        List<SavingDeposit> deposits = getDepositsForGroup(groupId);
        BigDecimal total = deposits.stream()
                .map(SavingDeposit::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return new SavingSummaryResponse(total, (long) deposits.size());
    }

    public List<SavingDeposit> getMemberDepositHistory(Long memberId) {
        return savingDepositRepo.findByMember_IdOrderByDateDesc(memberId);
    }

    public SavingSummaryResponse getMemberSavingSummary(Long memberId) {
        List<SavingDeposit> deposits = savingDepositRepo.findByMember_Id(memberId);
        BigDecimal total = deposits.stream()
                .map(SavingDeposit::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return new SavingSummaryResponse(total, (long) deposits.size());
    }

    public List<SavingDeposit> trackMyDeposits(Member member) {
        return savingDepositRepo.findByMember_IdOrderByDateDesc(member.getId());
    }
}