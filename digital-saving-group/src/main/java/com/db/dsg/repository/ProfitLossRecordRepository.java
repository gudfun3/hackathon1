package com.db.dsg.repository;

import com.db.dsg.model.ProfitLossRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProfitLossRecordRepository extends JpaRepository<ProfitLossRecord, Long> {
    List<ProfitLossRecord> findByGroup_IdOrderByDateDesc(Long groupId);
}
