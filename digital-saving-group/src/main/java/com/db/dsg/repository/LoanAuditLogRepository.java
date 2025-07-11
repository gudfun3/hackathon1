package com.db.dsg.repository;

import com.db.dsg.model.LoanAuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LoanAuditLogRepository extends JpaRepository<LoanAuditLog, Long> {
    List<LoanAuditLog> findByLoan_IdOrderByTimestampAsc(Long loanId);
}