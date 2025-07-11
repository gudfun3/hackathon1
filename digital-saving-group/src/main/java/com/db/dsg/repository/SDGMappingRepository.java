package com.db.dsg.repository;

import com.db.dsg.model.SDGMapping;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SDGMappingRepository extends JpaRepository<SDGMapping, Long> {
    List<SDGMapping> findByActionType(String actionType);
}
