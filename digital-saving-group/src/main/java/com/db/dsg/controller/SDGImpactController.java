package com.db.dsg.controller;

import com.db.dsg.model.SDGGoal;
import com.db.dsg.model.SDGImpact;
import com.db.dsg.repository.GroupRepository;
import com.db.dsg.repository.SDGImpactRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/sdg-impact")
@RequiredArgsConstructor
public class SDGImpactController {

    private final SDGImpactRepository impactRepo;
    private final GroupRepository groupRepo;

    @GetMapping("/group/{groupId}")
    public List<SDGImpact> getByGroup(@PathVariable Long groupId) {
        return impactRepo.findByGroupId(groupId);
    }

    @GetMapping("/group/{groupId}/month/{month}")
    public List<SDGImpact> getByGroupAndMonth(@PathVariable Long groupId, @PathVariable String month) {
        return impactRepo.findByGroupIdAndPeriod(groupId, month);
    }

    @GetMapping("/group/{groupId}/goal/{goal}")
    public List<SDGImpact> getByGroupAndGoal(@PathVariable Long groupId, @PathVariable SDGGoal goal) {
        return impactRepo.findByGroupIdAndGoal(groupId, goal);
    }

    @GetMapping("/summary/group/{groupId}")
    public Map<SDGGoal, Long> getImpactSummary(@PathVariable Long groupId) {
        return impactRepo.findByGroupId(groupId).stream()
                .collect(Collectors.groupingBy(SDGImpact::getGoal, Collectors.counting()));
    }
}
