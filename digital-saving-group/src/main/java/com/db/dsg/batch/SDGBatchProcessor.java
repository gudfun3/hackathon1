package com.db.dsg.batch;

import com.db.dsg.model.*;
import com.db.dsg.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;

@Component
@RequiredArgsConstructor
public class SDGBatchProcessor {

    private final SavingDepositRepository depositRepo;
    private final LoanApplicationRepository loanRepo;
    private final SDGMappingRepository mappingRepo;
    private final SDGImpactRepository impactRepo;
    private final SDGProcessingStatusRepository statusRepo;

    public ItemReader<SavingDeposit> depositReader() {
        List<Long> unprocessedIds = statusRepo.findByEntityTypeAndProcessedFalse(SDGProcessingStatus.EntityType.DEPOSIT)
                .stream().map(SDGProcessingStatus::getReferenceId).toList();
        return new ListItemReader<>(depositRepo.findAllById(unprocessedIds));
    }

    public ItemProcessor<SavingDeposit, SDGImpact> depositProcessor() {
        List<SDGMapping> mappings = mappingRepo.findByActionType("DEPOSIT_TYPE");

        return deposit -> {
            for (SDGMapping mapping : mappings) {
                if (deposit.getType().toLowerCase().contains(mapping.getKeyword().toLowerCase())) {
                    return buildImpact(deposit.getMember().getGroup(), mapping, deposit.getAmount(), null, null, deposit.getId(), DEPOSIT);
                }
            }
            return null;
        };
    }

    public ItemReader<Loan> loanReader() {
        List<Long> unprocessedIds = statusRepo.findByEntityTypeAndProcessedFalse(SDGProcessingStatus.EntityType.LOAN)
                .stream().map(SDGProcessingStatus::getReferenceId).toList();
        return new ListItemReader<>(loanRepo.findAllById(unprocessedIds));
    }

    public ItemProcessor<Loan, SDGImpact> loanProcessor() {
        List<SDGMapping> mappings = mappingRepo.findByActionType("LOAN_PURPOSE");

        return loan -> {
            for (SDGMapping mapping : mappings) {
                if (loan.getPurpose().toLowerCase().contains(mapping.getKeyword().toLowerCase())) {
                    int jobs = mapping.getGoal().name().contains("DECENT_WORK") ? 1 : 0;
                    int women = loan.getMember().getGender() == Member.Gender.FEMALE ? 1 : 0;
                    return buildImpact(loan.getGroup(), mapping, null, jobs, women, loan.getId(), LOAN);
                }
            }
            return null;
        };
    }

    public ItemWriter<SDGImpact> impactWriter() {
        return impacts -> {
            for (SDGImpact impact : impacts) {
                if (impact == null) continue;
                impactRepo.save(impact);

                SDGProcessingStatus status = statusRepo
                        .findByReferenceIdAndEntityType(impact.getReferenceId(), impact.getReferenceType())
                        .orElse(new SDGProcessingStatus(null, impact.getReferenceId(), impact.getReferenceType(), false, null));

                status.setProcessed(true);
                status.setProcessedAt(LocalDateTime.now());
                statusRepo.save(status);
            }
        };
    }

    private SDGImpact buildImpact(Group group, SDGMapping mapping, BigDecimal savingsGrowth,
                                  Integer jobs, Integer women, Long refId, SDGProcessingStatus.EntityType type) {
        SDGImpact impact = new SDGImpact();
        impact.setGroup(group);
        impact.setGoal(mapping.getGoal());
        impact.setDescription(mapping.getDescription());
        impact.setImpactDate(LocalDate.now());
        impact.setPeriod(YearMonth.now().toString());
        impact.setReferenceId(refId);
        impact.setReferenceType(type);
        if (savingsGrowth != null) impact.setSavingsGrowth(savingsGrowth);
        if (jobs != null) impact.setJobsCreated(jobs);
        if (women != null) impact.setWomenEmpowered(women);
        return impact;
    }
}
