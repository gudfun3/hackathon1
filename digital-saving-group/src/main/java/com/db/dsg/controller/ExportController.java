package com.db.dsg.controller;

import com.db.dsg.service.ExportService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/export")
@RequiredArgsConstructor
public class ExportController {

    private final ExportService exportService;

    @GetMapping("/savings/csv")
    public ResponseEntity<byte[]> exportSavingsAsCSV(@RequestParam Long groupId,
                                                     @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
                                                     @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return buildCSVResponse("savings.csv", exportService.exportSavingsAsCSV(groupId, from, to));
    }

    @GetMapping("/repayments/csv")
    public ResponseEntity<byte[]> exportRepaymentsAsCSV(@RequestParam Long groupId,
                                                        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
                                                        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return buildCSVResponse("repayments.csv", exportService.exportRepaymentsAsCSV(groupId, from, to));
    }

    @GetMapping("/savings/pdf")
    public ResponseEntity<byte[]> exportSavingsAsPDF(@RequestParam Long groupId,
                                                     @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
                                                     @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) throws Exception {
        return buildPDFResponse("savings.pdf", exportService.exportSavingsAsPDF(groupId, from, to));
    }

    @GetMapping("/repayments/pdf")
    public ResponseEntity<byte[]> exportRepaymentsAsPDF(@RequestParam Long groupId,
                                                        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
                                                        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) throws Exception {
        return buildPDFResponse("repayments.pdf", exportService.exportRepaymentsAsPDF(groupId, from, to));
    }

    @GetMapping("/all-savings/csv")
    public ResponseEntity<byte[]> exportAllSavingsAsCSV(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
                                                        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return buildCSVResponse("all_savings.csv", exportService.exportAllSavingsAsCSV(from, to));
    }

    @GetMapping("/all-savings/pdf")
    public ResponseEntity<byte[]> exportAllSavingsAsPDF(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
                                                        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) throws Exception {
        return buildPDFResponse("all_savings.pdf", exportService.exportAllSavingsAsPDF(from, to));
    }

    private ResponseEntity<byte[]> buildCSVResponse(String filename, byte[] data) {
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .contentType(MediaType.TEXT_PLAIN)
                .body(data);
    }

    private ResponseEntity<byte[]> buildPDFResponse(String filename, byte[] data) {
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .contentType(MediaType.APPLICATION_PDF)
                .body(data);
    }
}
