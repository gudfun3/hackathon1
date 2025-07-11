package com.db.dsg.repository;

import com.db.dsg.model.SDGGoal;
import com.db.dsg.model.SDGImpact;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface SDGImpactRepository extends JpaRepository<SDGImpact, Long> {
    List<SDGImpact> findByGroupId(Long groupId);
    List<SDGImpact> findByGroupIdAndGoal(Long groupId, SDGGoal goal);
    List<SDGImpact> findByGroupIdAndPeriodStartingWith(Long groupId, String year);
    List<SDGImpact> findByGroupIdAndPeriod(Long groupId, String period);
}