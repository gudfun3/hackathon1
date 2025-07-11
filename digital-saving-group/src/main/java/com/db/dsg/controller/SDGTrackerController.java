package com.db.dsg.controller;

import com.db.dsg.dtos.SDGSummary;
import com.db.dsg.model.SDGGoal;
import com.db.dsg.model.SDGImpact;
import com.db.dsg.service.SDGTrackerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/sdg")
@RequiredArgsConstructor
public class SDGTrackerController {

    private final SDGTrackerService service;

    @PostMapping("/group/{groupId}")
    @PreAuthorize("hasRole('GROUP_ADMIN') or hasRole('ADMIN')")
    public ResponseEntity<SDGImpact> addImpact(
            @PathVariable Long groupId,
            @RequestBody @Valid SDGImpact impact) {
        return ResponseEntity.ok(service.createImpact(groupId, impact));
    }

    @GetMapping("/group/{groupId}")
    @PreAuthorize("hasRole('GROUP_ADMIN') or hasRole('ADMIN')")
    public ResponseEntity<List<SDGImpact>> getGroupImpacts(@PathVariable Long groupId) {
        return ResponseEntity.ok(service.getGroupImpacts(groupId));
    }

    @GetMapping("/group/{groupId}/goal/{goal}")
    public ResponseEntity<List<SDGImpact>> getGoalImpacts(
            @PathVariable Long groupId,
            @PathVariable SDGGoal goal) {
        return ResponseEntity.ok(service.getGroupGoalImpacts(groupId, goal));
    }

    @GetMapping("/group/{groupId}/year/{year}")
    public ResponseEntity<List<SDGImpact>> getYearlyImpacts(
            @PathVariable Long groupId,
            @PathVariable String year) {
        return ResponseEntity.ok(service.getGroupYearlyImpacts(groupId, year));
    }

    @GetMapping("/group/{groupId}/summary")
    public ResponseEntity<Map<SDGGoal, SDGSummary>> getSDGSummary(@PathVariable Long groupId) {
        return ResponseEntity.ok(service.getGroupSDGSummary(groupId));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteImpact(@PathVariable Long id) {
        service.deleteImpact(id);
        return ResponseEntity.ok().build();
    }
}
