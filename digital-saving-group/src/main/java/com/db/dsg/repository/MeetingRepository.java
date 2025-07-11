package com.db.dsg.repository;

import com.db.dsg.model.Meeting;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MeetingRepository extends JpaRepository<Meeting, Long> {
    List<Meeting> findByGroupId(Long groupId);
}
