package com.db.dsg.repository;

import com.db.dsg.model.GroupFund;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GroupFundRepository extends JpaRepository<GroupFund, Long> {
    Optional<GroupFund> findByGroup_Id(Long groupId);
}
