package com.db.dsg.repository;

import com.db.dsg.model.LoanRepayment;
import com.db.dsg.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LoanRepaymentRepository extends JpaRepository<LoanRepayment, Long> {
    List<LoanRepayment> findByLoanId(Long loanId);

    List<LoanRepayment> findByLoan_Member(Member member);

    List<LoanRepayment> findByLoan_Member_Group_Id(Long groupId);
    List<LoanRepayment> findByLoan_Member_Id(Long memberId);
}