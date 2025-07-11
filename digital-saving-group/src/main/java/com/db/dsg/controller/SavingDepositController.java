package com.db.dsg.controller;

import com.db.dsg.dtos.SavingDepositRequest;
import com.db.dsg.dtos.SavingSummaryResponse;
import com.db.dsg.model.Member;
import com.db.dsg.model.SavingDeposit;
import com.db.dsg.service.SavingDepositService;
import com.db.dsg.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/saving-deposits")
@RequiredArgsConstructor
public class SavingDepositController {

    private final SavingDepositService savingDepositService;

    @PostMapping
    @PreAuthorize("hasRole('MEMBER')")
    public ResponseEntity<?> deposit(@RequestBody SavingDepositRequest req) {
        Member member = SecurityUtil.getCurrentUser().getMember();
        return ResponseEntity.ok(savingDepositService.save(req, member));
    }

    @GetMapping("/group")
    @PreAuthorize("hasAnyRole('GROUP_ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<List<SavingDeposit>> groupDeposits() {
        Long groupId = SecurityUtil.getCurrentGroupId();
        return ResponseEntity.ok(savingDepositService.getDepositsForGroup(groupId));
    }

    @GetMapping("/group/summary")
    @PreAuthorize("hasAnyRole('GROUP_ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<SavingSummaryResponse> groupSummary() {
        Long groupId = SecurityUtil.getCurrentGroupId();
        return ResponseEntity.ok(savingDepositService.getGroupSavingSummary(groupId));
    }

    @GetMapping("/member/{memberId}/summary")
    @PreAuthorize("hasAnyRole('GROUP_ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<SavingSummaryResponse> memberSummary(@PathVariable Long memberId) {
        return ResponseEntity.ok(savingDepositService.getMemberSavingSummary(memberId));
    }

    @GetMapping("/member/{memberId}/history")
    @PreAuthorize("hasAnyRole('GROUP_ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<List<SavingDeposit>> memberHistory(@PathVariable Long memberId) {
        return ResponseEntity.ok(savingDepositService.getMemberDepositHistory(memberId));
    }

    @GetMapping("/me/track")
    @PreAuthorize("hasRole('MEMBER')")
    public ResponseEntity<List<SavingDeposit>> myTrack() {
        Member member = SecurityUtil.getCurrentUser().getMember();
        return ResponseEntity.ok(savingDepositService.trackMyDeposits(member));
    }
}
