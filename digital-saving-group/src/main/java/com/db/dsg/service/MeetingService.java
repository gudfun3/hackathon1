package com.db.dsg.service;

import com.db.dsg.model.Attendance;
import com.db.dsg.model.Meeting;
import com.db.dsg.model.Member;
import com.db.dsg.model.MemberUser;
import com.db.dsg.repository.AttendanceRepository;
import com.db.dsg.repository.MeetingRepository;
import com.db.dsg.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MeetingService {
    private final MeetingRepository meetingRepo;
    private final AttendanceRepository attendanceRepo;
    private final MemberRepository memberRepo;

    public Meeting createMeeting(Meeting meeting) {
        return meetingRepo.save(meeting);
    }

    public List<Meeting> getMeetingsByGroup(Long groupId) {
        return meetingRepo.findByGroupId(groupId);
    }

    public Attendance markAttendance(Long meetingId, MemberUser user, boolean present) {
        Meeting meeting = meetingRepo.findById(meetingId).orElseThrow();
        Member member = memberRepo.findByUser(user).orElseThrow();

        Attendance attendance = new Attendance(null, meeting, member, present);
        return attendanceRepo.save(attendance);
    }

    public List<Attendance> getAttendanceForMeeting(Long meetingId) {
        return attendanceRepo.findByMeetingId(meetingId);
    }
}
