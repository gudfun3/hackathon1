package com.db.dsg.repository;

import com.db.dsg.model.SavingDeposit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface SavingDepositRepository extends JpaRepository<SavingDeposit, Long> {
    List<SavingDeposit> findByMemberIdOrderByDateDesc(Long memberId);

    List<SavingDeposit> findByMemberIdAndDateBetween(Long memberId, LocalDate start, LocalDate end);

    List<SavingDeposit> findByMember_Group_Id(Long groupId);

    List<SavingDeposit> findByMember_IdOrderByDateDesc(Long memberId);

    List<SavingDeposit> findByMember_Id(Long memberId);

    List<SavingDeposit> findByMember_Group_IdAndDateBetween(Long groupId, LocalDate from, LocalDate to);
    List<SavingDeposit> findByDateBetween(LocalDate from, LocalDate to); // for all groups

}
