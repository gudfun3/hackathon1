package com.db.dsg.repository;

import com.db.dsg.model.Loan;
import com.db.dsg.model.LoanStatus;
import com.db.dsg.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface LoanApplicationRepository extends JpaRepository<Loan, Long> {
    List<Loan> findByMember_Group_Id(Long groupId);
    List<Loan> findByMember(Member member);
    List<Loan> findByMember_Group_IdAndRepaymentDateBetween(Long groupId, LocalDate start, LocalDate end);
    List<Loan> findByMember_Group_IdAndStatusAndDisbursementDateBefore(Long groupId, LoanStatus status, LocalDate date);

}