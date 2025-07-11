package com.db.dsg.controller;

import com.db.dsg.model.Attendance;
import com.db.dsg.model.Meeting;
import com.db.dsg.model.MemberUser;
import com.db.dsg.service.MeetingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/meetings")
@RequiredArgsConstructor
public class MeetingController {
    private final MeetingService service;

    @PostMapping
    @PreAuthorize("hasRole('PRESIDENT') or hasRole('ADMIN')")
    public ResponseEntity<Meeting> createMeeting(@RequestBody @Valid Meeting meeting) {
        return ResponseEntity.ok(service.createMeeting(meeting));
    }

    @GetMapping("/group/{groupId}")
    @PreAuthorize("hasRole('PRESIDENT') or hasRole('ADMIN') or hasRole('TREASURER')")
    public List<Meeting> getMeetingsByGroup(@PathVariable Long groupId) {
        return service.getMeetingsByGroup(groupId);
    }

    @PostMapping("/{meetingId}/attendance")
    @PreAuthorize("hasRole('MEMBER')")
    public ResponseEntity<Attendance> markAttendance(
            @PathVariable Long meetingId,
            @RequestParam boolean present,
            @AuthenticationPrincipal MemberUser user) {
        return ResponseEntity.ok(service.markAttendance(meetingId, user, present));
    }

    @GetMapping("/{meetingId}/attendance")
    @PreAuthorize("hasRole('PRESIDENT') or hasRole('ADMIN')")
    public List<Attendance> getAttendance(@PathVariable Long meetingId) {
        return service.getAttendanceForMeeting(meetingId);
    }
}
