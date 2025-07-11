package com.db.dsg.controller;

import com.db.dsg.service.ProfitLossService;
import com.db.dsg.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/pnl")
@RequiredArgsConstructor
public class ProfitLossController {

    private final ProfitLossService pnlService;

    @GetMapping("/group")
    @PreAuthorize("hasAnyRole('GROUP_ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<?> getGroupPnl() {
        Long groupId = SecurityUtil.getCurrentGroupId();
        return ResponseEntity.ok(pnlService.getGroupProfitLoss(groupId));
    }
}
