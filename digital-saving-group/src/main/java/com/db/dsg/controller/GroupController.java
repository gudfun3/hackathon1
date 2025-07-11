package com.db.dsg.controller;

import com.db.dsg.model.Group;
import com.db.dsg.service.GroupService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/groups")
@RequiredArgsConstructor
public class GroupController {

    private final GroupService groupService;

    // ✅ Create group
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Group> createGroup(@RequestBody @Valid Group group) {
        return ResponseEntity.ok(groupService.createGroup(group));
    }

    // ✅ Update group
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Group> updateGroup(
            @PathVariable Long id,
            @RequestBody @Valid Group updated) {

        return ResponseEntity.ok(groupService.updateGroup(id, updated));
    }

    // ✅ Delete group
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteGroup(@PathVariable Long id) {
        groupService.deleteGroup(id);
        return ResponseEntity.noContent().build();
    }

    // ✅ Get all groups
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'PRESIDENT', 'TREASURER')")
    public List<Group> listGroups() {
        return groupService.listGroups();
    }

    // ✅ Get single group
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PRESIDENT', 'TREASURER')")
    public ResponseEntity<Group> getGroupById(@PathVariable Long id) {
        return groupService.getGroupById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}