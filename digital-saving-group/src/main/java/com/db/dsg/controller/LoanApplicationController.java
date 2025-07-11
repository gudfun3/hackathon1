package com.db.dsg.controller;

import com.db.dsg.model.Loan;
import com.db.dsg.model.LoanAuditLog;
import com.db.dsg.model.Member;
import com.db.dsg.model.MemberUser;
import com.db.dsg.service.LoanApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.List;

@RestController
@RequestMapping("/api/loans")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('MEMBER', 'PRESIDENT', 'TREASURER', 'SUPER_ADMIN')")
public class LoanApplicationController {

    private final LoanApplicationService loanService;

    /**
     * MEMBER applies for a loan
     */
    @PostMapping("/apply")
    @PreAuthorize("hasRole('MEMBER')")
    public ResponseEntity<Loan> applyLoan(
            @RequestParam BigDecimal amount,
            @RequestParam(required = false) String purpose,
            @AuthenticationPrincipal MemberUser memberUser
    ) {
        Member member = memberUser.getMember();
        Loan loan = loanService.applyLoan(member, amount, purpose);
        return ResponseEntity.ok(loan);
    }

    /**
     * PRESIDENT approves a loan
     */
    @PostMapping("/{loanId}/approve")
    @PreAuthorize("hasRole('PRESIDENT')")
    public ResponseEntity<Loan> approveLoan(
            @PathVariable Long loanId,
            @AuthenticationPrincipal MemberUser user
    ) {
        return ResponseEntity.ok(loanService.approveLoan(loanId, user));
    }

    /**
     * TREASURER disburses a loan
     */
    @PostMapping("/{loanId}/disburse")
    @PreAuthorize("hasRole('TREASURER')")
    public ResponseEntity<Loan> disburseLoan(
            @PathVariable Long loanId,
            @AuthenticationPrincipal MemberUser user
    ) {
        return ResponseEntity.ok(loanService.disburseLoan(loanId, user));
    }

    /**
     * PRESIDENT or TREASURER rejects a loan
     */
    @PostMapping("/{loanId}/reject")
    @PreAuthorize("hasAnyRole('PRESIDENT', 'TREASURER')")
    public ResponseEntity<Loan> rejectLoan(
            @PathVariable Long loanId,
            @AuthenticationPrincipal MemberUser user
    ) {
        return ResponseEntity.ok(loanService.rejectLoan(loanId, user));
    }

    @PostMapping("/{loanId}/repay")
    @PreAuthorize("hasRole('MEMBER')")
    public ResponseEntity<Loan> repayLoan(
            @PathVariable Long loanId,
            @RequestParam BigDecimal amount,
            @AuthenticationPrincipal MemberUser memberUser
    ) {
        return ResponseEntity.ok(loanService.repayAndUpdateFund(loanId, amount, memberUser));
    }

    /**
     * MEMBER views own loan applications
     */
    @GetMapping("/my")
    @PreAuthorize("hasRole('MEMBER')")
    public ResponseEntity<List<Loan>> getMyLoans(@AuthenticationPrincipal MemberUser user) {
        return ResponseEntity.ok(loanService.getLoansByMember(user.getMember()));
    }

    /**
     * PRESIDENT or TREASURER views loans by group
     */
    @GetMapping("/group/{groupId}")
    @PreAuthorize("hasAnyRole('PRESIDENT', 'TREASURER', 'SUPER_ADMIN')")
    public ResponseEntity<List<Loan>> getLoansByGroup(@PathVariable Long groupId) {
        return ResponseEntity.ok(loanService.getLoansByGroupId(groupId));
    }

    /**
     * Get audit trail for a loan
     */
    @GetMapping("/{loanId}/logs")
    @PreAuthorize("hasAnyRole('PRESIDENT', 'TREASURER', 'SUPER_ADMIN')")
    public ResponseEntity<List<LoanAuditLog>> getLoanLogs(@PathVariable Long loanId) {
        return ResponseEntity.ok(loanService.getAuditLogs(loanId));
    }

    @GetMapping("/group/{groupId}/repayments")
    @PreAuthorize("hasAnyRole('PRESIDENT', 'TREASURER', 'SUPER_ADMIN')")
    public ResponseEntity<List<Loan>> getMonthlyRepayments(
            @PathVariable Long groupId,
            @RequestParam int year,
            @RequestParam int month) {
        return ResponseEntity.ok(loanService.getMonthlyRepayments(groupId, YearMonth.of(year, month)));
    }

    @GetMapping("/group/{groupId}/overdue")
    @PreAuthorize("hasAnyRole('PRESIDENT', 'TREASURER', 'SUPER_ADMIN')")
    public ResponseEntity<List<Loan>> getOverdueLoans(@PathVariable Long groupId) {
        return ResponseEntity.ok(loanService.getOverdueLoans(groupId));
    }

    @GetMapping("/group/{groupId}/overdue-members")
    @PreAuthorize("hasAnyRole('PRESIDENT', 'TREASURER')")
    public ResponseEntity<List<Member>> getOverdueMembers(@PathVariable Long groupId) {
        return ResponseEntity.ok(loanService.getMembersWithOverdueLoans(groupId));
    }
}
