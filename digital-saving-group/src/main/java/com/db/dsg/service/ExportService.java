package com.db.dsg.service;

import com.db.dsg.model.Loan;
import com.db.dsg.model.SavingDeposit;
import com.db.dsg.repository.LoanApplicationRepository;
import com.db.dsg.repository.LoanRepaymentRepository;
import com.db.dsg.repository.MemberRepository;
import com.db.dsg.repository.SavingDepositRepository;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.UnitValue;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExportService {

    private final SavingDepositRepository savingRepo;
    private final LoanRepaymentRepository repaymentRepo;
    private final MemberRepository memberRepo;
    private final LoanApplicationRepository loanRepo;

    // ----------------- Group-scoped ---------------------

    public byte[] exportSavingsAsCSV(Long groupId, LocalDate from, LocalDate to) {
        List<SavingDeposit> deposits = savingRepo.findByMember_Group_IdAndDateBetween(groupId, from, to);
        StringBuilder csv = new StringBuilder("Member,Amount,Date,Description\n");
        for (SavingDeposit s : deposits) {
            csv.append(s.getMember().getName()).append(",")
                    .append(s.getAmount()).append(",")
                    .append(s.getDate()).append(",").append("\n");
        }
        return csv.toString().getBytes();
    }

    public byte[] exportRepaymentsAsCSV(Long groupId, LocalDate from, LocalDate to) {
        List<Loan> loans = loanRepo.findByMember_Group_IdAndRepaymentDateBetween(groupId, from, to);
        StringBuilder csv = new StringBuilder("Member,Amount,Remaining,Status,Disbursed,Repaid\n");
        for (Loan l : loans) {
            csv.append(l.getMember().getName()).append(",")
                    .append(l.getAmount()).append(",")
                    .append(l.getRemainingBalance()).append(",")
                    .append(l.getStatus()).append(",")
                    .append(l.getDisbursementDate()).append(",")
                    .append(l.getRepaymentDate()).append("\n");
        }
        return csv.toString().getBytes();
    }

    public byte[] exportSavingsAsPDF(Long groupId, LocalDate from, LocalDate to) throws Exception {
        List<SavingDeposit> deposits = savingRepo.findByMember_Group_IdAndDateBetween(groupId, from, to);
        return generateSavingsPdf("Savings Deposit Report", deposits);
    }

    public byte[] exportRepaymentsAsPDF(Long groupId, LocalDate from, LocalDate to) throws Exception {
        List<Loan> loans = loanRepo.findByMember_Group_IdAndRepaymentDateBetween(groupId, from, to);
        return generateLoanPdf("Loan Repayment Report", loans);
    }

    // ----------------- All-groups (admin) ---------------------

    public byte[] exportAllSavingsAsCSV(LocalDate from, LocalDate to) {
        List<SavingDeposit> deposits = savingRepo.findByDateBetween(from, to);
        StringBuilder csv = new StringBuilder("Member,Group,Amount,Date,Description\n");
        for (SavingDeposit s : deposits) {
            csv.append(s.getMember().getName()).append(",")
                    .append(s.getMember().getGroup().getName()).append(",")
                    .append(s.getAmount()).append(",")
                    .append(s.getDate()).append(",").append("\n");
        }
        return csv.toString().getBytes();
    }

    public byte[] exportAllSavingsAsPDF(LocalDate from, LocalDate to) throws Exception {
        List<SavingDeposit> deposits = savingRepo.findByDateBetween(from, to);
        return generateSavingsPdf("All Group Savings Report", deposits);
    }

    // ----------------- Shared PDF methods ---------------------

    public byte[] generateLoanPdf(String title, List<Loan> loans) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        PdfWriter writer = new PdfWriter(out);
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document doc = new Document(pdfDoc);

        doc.add(new Paragraph(title).setBold().setFontSize(14));

        Table table = new Table(UnitValue.createPercentArray(new float[]{2, 2, 2, 2, 2, 2}));
        addHeader(table, "Member", "Amount", "Remaining", "Status", "Disbursed", "Repaid");

        for (Loan l : loans) {
            table.addCell(new Cell().add(new Paragraph(l.getMember().getName())));
            table.addCell(new Cell().add(new Paragraph(l.getAmount().toString())));
            table.addCell(new Cell().add(new Paragraph(l.getRemainingBalance().toString())));
            table.addCell(new Cell().add(new Paragraph(l.getStatus().name())));
            table.addCell(new Cell().add(new Paragraph(l.getDisbursementDate() != null ? l.getDisbursementDate().toString() : "-")));
            table.addCell(new Cell().add(new Paragraph(l.getRepaymentDate() != null ? l.getRepaymentDate().toString() : "-")));
        }

        doc.add(table);
        doc.close();
        return out.toByteArray();
    }

    public byte[] generateSavingsPdf(String title, List<SavingDeposit> deposits) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        PdfWriter writer = new PdfWriter(out);
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document doc = new Document(pdfDoc);

        // Title
        doc.add(new Paragraph(title).setBold().setFontSize(14).setMarginBottom(10));

        // Table with 4 columns: Member, Amount, Date, Description
        Table table = new Table(UnitValue.createPercentArray(new float[]{2, 2, 2, 4})).useAllAvailableWidth();

        // Header
        addHeader(table, "Member", "Amount", "Date", "Description");

        // Rows
        for (SavingDeposit s : deposits) {
            table.addCell(new Cell().add(new Paragraph(s.getMember().getName())));
            table.addCell(new Cell().add(new Paragraph(s.getAmount().toString())));
            table.addCell(new Cell().add(new Paragraph(s.getDate().toString())));

        }

        doc.add(table);
        doc.close();
        return out.toByteArray();
    }

    private void addHeader(Table table, String... headers) {
        for (String h : headers) {
            Cell headerCell = new Cell()
                    .add(new Paragraph(h).setBold())
                    .setBackgroundColor(com.itextpdf.kernel.colors.ColorConstants.LIGHT_GRAY);
            table.addHeaderCell(headerCell);
        }
    }
}
