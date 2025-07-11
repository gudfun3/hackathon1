package com.db.dsg.controller;

import com.db.dsg.service.ExportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminExportController {

    private final ExportService exportService;

    @GetMapping("/savings/export")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<byte[]> exportAllSavings(@RequestParam(defaultValue = "csv") String format) throws IOException, IOException {
        byte[] data;
        String contentType, fileName;

        if (format.equalsIgnoreCase("pdf")) {
            data = exportService.exportAllSavingsAsPDF();
            contentType = "application/pdf";
            fileName = "all_savings_report.pdf";
        } else {
            data = exportService.exportAllSavingsAsCSV();
            contentType = "text/csv";
            fileName = "all_savings_report.csv";
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
                .contentType(MediaType.parseMediaType(contentType))
                .body(data);
    }

    @GetMapping("/loans/repayments/export")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<byte[]> exportAllRepayments(@RequestParam(defaultValue = "csv") String format) throws IOException {
        byte[] data;
        String contentType, fileName;

        if (format.equalsIgnoreCase("pdf")) {
            data = exportService.exportAllRepaymentsAsPDF();
            contentType = "application/pdf";
            fileName = "all_loan_repayments.pdf";
        } else {
            data = exportService.exportAllRepaymentsAsCSV();
            contentType = "text/csv";
            fileName = "all_loan_repayments.csv";
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
                .contentType(MediaType.parseMediaType(contentType))
                .body(data);
    }

}
