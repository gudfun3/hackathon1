package com.db.dsg.service;

import com.db.dsg.model.*;
import com.db.dsg.repository.LoanApplicationRepository;
import com.db.dsg.repository.LoanAuditLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LoanApplicationService {
    private final LoanApplicationRepository loanRepo;
    private final LoanAuditLogRepository auditLogRepo;
    private final GroupFundService groupFundService;

    // ✅ Member applies for a loan
    public Loan applyLoan(Member member, BigDecimal amount, String purpose) {
        Loan loan = new Loan();
        loan.setMember(member);
        loan.setAmount(amount);
        loan.setRemainingBalance(amount);
        loan.setStatus(LoanStatus.PENDING);
        loan.setPurpose(purpose);
        loan.setApplicationDate(LocalDate.now());

        Loan saved = loanRepo.save(loan);

        auditLogRepo.save(new LoanAuditLog(null, saved, LoanStatus.PENDING,
                member.getName(), LocalDateTime.now(), "Loan applied by " + member.getName()));

        return saved;
    }

    // ✅ President approves the loan
    public Loan approveLoan(Long loanId, MemberUser approver) {
        Loan loan = loanRepo.findById(loanId)
                .orElseThrow(() -> new IllegalArgumentException("Loan not found"));

        if (loan.getStatus() != LoanStatus.PENDING) {
            throw new IllegalStateException("Only PENDING loans can be approved");
        }

        loan.setStatus(LoanStatus.APPROVED);
        loan.setApprovalDate(LocalDate.now());

        Loan updated = loanRepo.save(loan);

        auditLogRepo.save(new LoanAuditLog(null, updated, LoanStatus.APPROVED,
                approver.getUsername(), LocalDateTime.now(), "Approved by " + approver.getUsername()));

        return updated;
    }

    // ✅ Treasurer disburses the loan
    public Loan disburseLoan(Long loanId, MemberUser treasurer) {
        Loan loan = loanRepo.findById(loanId)
                .orElseThrow(() -> new IllegalArgumentException("Loan not found"));

        if (loan.getStatus() != LoanStatus.APPROVED) {
            throw new IllegalStateException("Only APPROVED loans can be disbursed");
        }

        if (!treasurer.hasRole("TREASURER")) {
            throw new AccessDeniedException("Only treasurer can disburse loans");
        }

        BigDecimal amount = loan.getAmount();
        Long groupId = loan.getMember().getGroup().getId();

        // Deduct from group fund
        groupFundService.subtractFromFund(groupId, amount);

        loan.setStatus(LoanStatus.DISBURSED);
        loan.setDisbursementDate(LocalDate.now());
        loan.setRemainingBalance(amount);

        Loan updated = loanRepo.save(loan);

        auditLogRepo.save(new LoanAuditLog(null, updated, LoanStatus.DISBURSED,
                treasurer.getUsername(), LocalDateTime.now(), "Disbursed ₹" + amount + " by " + treasurer.getUsername()));

        return updated;
    }

    // ✅ Reject the loan (President or Treasurer)
    public Loan rejectLoan(Long loanId, MemberUser user) {
        Loan loan = loanRepo.findById(loanId)
                .orElseThrow(() -> new IllegalArgumentException("Loan not found"));

        if (loan.getStatus() != LoanStatus.PENDING && loan.getStatus() != LoanStatus.APPROVED) {
            throw new IllegalStateException("Only PENDING or APPROVED loans can be rejected");
        }

        loan.setStatus(LoanStatus.REJECTED);

        Loan updated = loanRepo.save(loan);

        auditLogRepo.save(new LoanAuditLog(null, updated, LoanStatus.REJECTED,
                user.getUsername(), LocalDateTime.now(), "Rejected by " + user.getUsername()));

        return updated;
    }

    public Loan repayAndUpdateFund(Long loanId, BigDecimal repaymentAmount, MemberUser memberUser) {
        Loan loan = loanRepo.findById(loanId)
                .orElseThrow(() -> new IllegalArgumentException("Loan not found"));

        Member member = memberUser.getMember();

        if (!loan.getMember().getId().equals(member.getId())) {
            throw new AccessDeniedException("This loan does not belong to the current user.");
        }

        if (loan.getStatus() != LoanStatus.DISBURSED && loan.getStatus() != LoanStatus.REPAID) {
            throw new IllegalStateException("Only DISBURSED loans can be repaid.");
        }

        BigDecimal currentBalance = loan.getRemainingBalance();
        BigDecimal newBalance = currentBalance.subtract(repaymentAmount);

        if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Repayment exceeds remaining loan amount.");
        }

        loan.setRemainingBalance(newBalance);

        // ✅ If fully paid
        if (newBalance.compareTo(BigDecimal.ZERO) == 0) {
            loan.setStatus(LoanStatus.REPAID);
            loan.setRepaymentDate(LocalDate.now());
        }

        Loan updatedLoan = loanRepo.save(loan);

        // ✅ Update GroupFund (add money back)
        Long groupId = loan.getMember().getGroup().getId();
        groupFundService.addToFund(groupId, repaymentAmount);

        // ✅ Optional: Track profit/loss (not required unless there's interest/profit sharing)
        // profitLossService.addProfit(groupId, ...);

        // ✅ Add audit log
        auditLogRepo.save(new LoanAuditLog(null, updatedLoan,
                loan.getStatus(), memberUser.getUsername(), LocalDateTime.now(),
                "Repayment of ₹" + repaymentAmount + " by " + memberUser.getUsername()));

        return updatedLoan;
    }

    // ✅ Get all loans for a member
    public List<Loan> getLoansByMember(Member member) {
        return loanRepo.findByMember(member);
    }

    // ✅ Get all loans for a group
    public List<Loan> getLoansByGroupId(Long groupId) {
        return loanRepo.findByMember_Group_Id(groupId);
    }

    // ✅ Get audit log history for a loan
    public List<LoanAuditLog> getAuditLogs(Long loanId) {
        return auditLogRepo.findByLoan_IdOrderByTimestampAsc(loanId);
    }

    // ✅ 3. Get monthly repayment history
    public List<Loan> getMonthlyRepayments(Long groupId, YearMonth month) {
        LocalDate start = month.atDay(1);
        LocalDate end = month.atEndOfMonth();
        return loanRepo.findByMember_Group_IdAndRepaymentDateBetween(groupId, start, end);
    }

    // ✅ 4. Get overdue loans (> 90 days)
    public List<Loan> getOverdueLoans(Long groupId) {
        LocalDate due = LocalDate.now().minusDays(90);
        return loanRepo.findByMember_Group_IdAndStatusAndDisbursementDateBefore(
                groupId, LoanStatus.DISBURSED, due);
    }

    // Optional: Notification candidates for overdue loans
    public List<Member> getMembersWithOverdueLoans(Long groupId) {
        return getOverdueLoans(groupId).stream()
                .map(Loan::getMember)
                .distinct()
                .toList();
    }

}