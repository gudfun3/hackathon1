package com.db.dsg.service;

import com.db.dsg.dtos.SDGSummary;
import com.db.dsg.model.Group;
import com.db.dsg.model.SDGGoal;
import com.db.dsg.model.SDGImpact;
import com.db.dsg.repository.GroupRepository;
import com.db.dsg.repository.SDGImpactRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SDGTrackerService {

    private final SDGImpactRepository repository;
    private final GroupRepository groupRepo;

    public SDGImpact createImpact(Long groupId, SDGImpact impact) {
        Group group = groupRepo.findById(groupId).orElseThrow();
        impact.setGroup(group);
        return repository.save(impact);
    }

    public List<SDGImpact> getGroupImpacts(Long groupId) {
        return repository.findByGroupId(groupId);
    }

    public List<SDGImpact> getGroupGoalImpacts(Long groupId, SDGGoal goal) {
        return repository.findByGroupIdAndGoal(groupId, goal);
    }

    public List<SDGImpact> getGroupYearlyImpacts(Long groupId, String year) {
        return repository.findByGroupIdAndPeriodStartingWith(groupId, year);
    }

    public Map<SDGGoal, SDGSummary> getGroupSDGSummary(Long groupId) {
        List<SDGImpact> all = repository.findByGroupId(groupId);

        return all.stream()
                .collect(Collectors.groupingBy(
                        SDGImpact::getGoal,
                        Collectors.collectingAndThen(Collectors.toList(), this::summarizeImpact)
                ));
    }

    private SDGSummary summarizeImpact(List<SDGImpact> list) {
        int women = list.stream().mapToInt(SDGImpact::getWomenEmpowered).sum();
        int jobs = list.stream().mapToInt(SDGImpact::getJobsCreated).sum();
        BigDecimal growth = list.stream()
                .map(SDGImpact::getSavingsGrowth)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new SDGSummary(women, jobs, growth);
    }

    public void deleteImpact(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("SDG Impact with ID " + id + " not found");
        }
        repository.deleteById(id);
    }
}
