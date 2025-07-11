package com.db.dsg.controller;

import com.db.dsg.model.GroupFund;
import com.db.dsg.repository.GroupRepository;
import com.db.dsg.service.GroupFundService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/group-funds")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('TREASURER', 'SUPER_ADMIN', 'PRESIDENT')")
public class GroupFundController {

    private final GroupFundService groupFundService;
    private final GroupRepository groupRepository;

    // ✅ Get full fund summary of a group
    @GetMapping("/{groupId}")
    public ResponseEntity<GroupFund> getGroupFund(@PathVariable Long groupId) {
        GroupFund fund = groupFundService.getGroupFund(groupId);
        return ResponseEntity.ok(fund);
    }

    // ✅ Get balance only
    @GetMapping("/{groupId}/balance")
    public ResponseEntity<BigDecimal> getBalance(@PathVariable Long groupId) {
        return ResponseEntity.ok(groupFundService.getBalance(groupId));
    }

    // ✅ Add profit
    @PostMapping("/{groupId}/profit")
    public ResponseEntity<?> addProfit(@PathVariable Long groupId,
                                       @RequestParam BigDecimal amount) {
        groupFundService.addProfit(groupId, amount);
        return ResponseEntity.ok("Profit added to group fund.");
    }

    // ✅ Add loss
    @PostMapping("/{groupId}/loss")
    public ResponseEntity<?> addLoss(@PathVariable Long groupId,
                                     @RequestParam BigDecimal amount) {
        groupFundService.addLoss(groupId, amount);
        return ResponseEntity.ok("Loss recorded to group fund.");
    }

    // ✅ Manually adjust balance (for corrections only)
    @PostMapping("/{groupId}/adjust-balance")
    public ResponseEntity<?> adjustBalance(@PathVariable Long groupId,
                                           @RequestParam BigDecimal newBalance) {
        groupFundService.setBalance(groupId, newBalance);
        return ResponseEntity.ok("Group fund balance updated.");
    }
}
