package com.db.dsg.controller;

import com.db.dsg.dtos.MemberOnboardingRequest;
import com.db.dsg.model.Member;
import com.db.dsg.model.MemberUser;
import com.db.dsg.repository.MemberRepository;
import com.db.dsg.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    // ✅ Onboard a new member to a group
    @PostMapping("/group/{groupId}/user/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PRESIDENT')")
    public ResponseEntity<Member> onboardMember(
            @PathVariable Long groupId,
            @PathVariable Long userId,
            @RequestBody @Valid Member member) {

        Member saved = memberService.onboardMember(groupId, member);
        return ResponseEntity.ok(saved);
    }

    // ✅ Get all members
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'PRESIDENT')")
    public List<Member> getAllMembers() {
        return memberService.getAllMembers();
    }

    // ✅ Get members by group
    @GetMapping("/group/{groupId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PRESIDENT', 'TREASURER')")
    public List<Member> getMembersByGroup(@PathVariable Long groupId) {
        return memberService.getMembersByGroup(groupId);
    }

    // ✅ Get single member
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PRESIDENT', 'TREASURER')")
    public ResponseEntity<Member> getMemberById(@PathVariable Long id) {
        return memberService.getMemberById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ✅ Update member info
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PRESIDENT')")
    public ResponseEntity<Member> updateMember(
            @PathVariable Long id,
            @RequestBody Member updated) {

        return ResponseEntity.ok(memberService.updateMember(id, updated));
    }

    // ✅ Delete member
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteMember(@PathVariable Long id) {
        memberService.deleteMember(id);
        return ResponseEntity.noContent().build();
    }

    // ✅ Logged-in member can fetch their profile
    @GetMapping("/me")
    @PreAuthorize("hasRole('MEMBER')")
    public ResponseEntity<Member> getMyMemberInfo(@AuthenticationPrincipal MemberUser user) {
        return ResponseEntity.ok(memberService.getByUser(user));
    }
}