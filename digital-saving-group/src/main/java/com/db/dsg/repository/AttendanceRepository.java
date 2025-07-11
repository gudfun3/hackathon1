package com.db.dsg.repository;

import com.db.dsg.model.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    List<Attendance> findByMeetingId(Long meetingId);
}
