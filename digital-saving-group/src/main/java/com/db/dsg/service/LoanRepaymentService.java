package com.db.dsg.service;

import com.db.dsg.model.Loan;
import com.db.dsg.model.LoanRepayment;
import com.db.dsg.model.LoanStatus;
import com.db.dsg.model.Member;
import com.db.dsg.repository.LoanApplicationRepository;
import com.db.dsg.repository.LoanRepaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LoanRepaymentService {
    private final LoanRepaymentRepository repaymentRepo;
    private final LoanApplicationRepository loanRepo;
    private final GroupFundService groupFundService;
    private final ProfitLossService profitLossService;

    public LoanRepayment repayAndUpdateFund(Long loanId, BigDecimal amount, Member member) {
        Loan loan = loanRepo.findById(loanId).orElseThrow(() -> new IllegalArgumentException("Loan not found"));

        if (!loan.getMember().getId().equals(member.getId())) {
            throw new AccessDeniedException("Not your loan");
        }

        if (loan.getRemainingBalance().compareTo(amount) < 0) {
            throw new IllegalArgumentException("Repayment exceeds remaining balance");
        }

        LoanRepayment repayment = new LoanRepayment();
        repayment.setLoan(loan);
        repayment.setAmount(amount);
        LoanRepayment saved = repaymentRepo.save(repayment);

        loan.setRemainingBalance(loan.getRemainingBalance().subtract(amount));

        if (loan.getRemainingBalance().compareTo(BigDecimal.ZERO) == 0) {
            loan.setStatus(LoanStatus.REPAID);
        }

        loanRepo.save(loan);

        // Fund update
        groupFundService.addToFund(member.getGroup().getId(), amount);

        // Assume 10% interest for illustration
        BigDecimal interest = amount.multiply(BigDecimal.valueOf(0.10));
        BigDecimal principal = amount.subtract(interest);

        profitLossService.recordPrincipalAndInterest(member.getGroup(), principal, interest, "Loan repayment");

        return saved;
    }

    public List<LoanRepayment> getMemberRepayments(Long memberId) {
        return repaymentRepo.findByLoan_Member_Id(memberId);
    }

    public List<LoanRepayment> getGroupRepayments(Long groupId) {
        return repaymentRepo.findByLoan_Member_Group_Id(groupId);
    }
}
