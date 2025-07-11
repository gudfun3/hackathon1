package com.db.dsg.repository;

import com.db.dsg.model.SDGProcessingStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SDGProcessingStatusRepository extends JpaRepository<SDGProcessingStatus, Long> {

    Optional<SDGProcessingStatus> findByReferenceIdAndEntityType(Long referenceId, SDGProcessingStatus.EntityType type);

    List<SDGProcessingStatus> findByEntityTypeAndProcessedFalse(SDGProcessingStatus.EntityType type);
}
