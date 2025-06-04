package com.example.bookstore.service;

import com.example.bookstore.exception.ReportException;
import com.example.bookstore.persistance.repository.SaleRepository;
import com.example.bookstore.service.email.EmailService;
import com.example.bookstore.service.projection.SalePaymentProjection;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.*;
import net.sf.jasperreports.engine.type.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final SaleRepository saleRepository;
    private final EmailService emailService;

    @Value("${app.report.root.path}")
    private String reportDirectoryPath;

    public void getSalesReport(LocalDate startDate, LocalDate endDate, String emailTo) {
        Instant startInstant = startDate.atStartOfDay().toInstant(ZoneOffset.MIN);
        Instant endInstant = endDate.atStartOfDay().toInstant(ZoneOffset.MIN);
        List<SalePaymentProjection> rows = saleRepository.findSalesAndPaymentsBetweenInstants(startInstant, endInstant);
        if(rows.isEmpty()){
            emailService.sendEmail(emailTo, "Sales Report", "Hello,\n\nThe requested period of time contains no data to be reported.\nYou might want to adjust the chosen dates.\n\nBest,\nHG Books");
        }

        JasperDesign jasperDesign = new JasperDesign();
        jasperDesign.setName("sale_report");
        jasperDesign.setPageWidth(595);      // A4 width in points
        jasperDesign.setPageHeight(842);     // A4 height in points
        jasperDesign.setColumnWidth(515);    // usable width
        jasperDesign.setLeftMargin(40);
        jasperDesign.setRightMargin(40);
        jasperDesign.setTopMargin(50);
        jasperDesign.setBottomMargin(50);

        JRDesignField fSaleId = new JRDesignField();
        fSaleId.setName("saleId");
        fSaleId.setValueClass(Long.class);
        addField(fSaleId, jasperDesign);

        JRDesignField fSoldAt = new JRDesignField();
        fSoldAt.setName("soldAt");
        fSoldAt.setValueClass(Instant.class);
        addField(fSoldAt, jasperDesign);

        JRDesignField fAmount = new JRDesignField();
        fAmount.setName("amount");
        fAmount.setValueClass(BigDecimal.class);
        addField(fAmount, jasperDesign);

        JRDesignField fCurrency = new JRDesignField();
        fCurrency.setName("currency");
        fCurrency.setValueClass(String.class);
        addField(fCurrency, jasperDesign);

        JRDesignField fBookInstanceId = new JRDesignField();
        fBookInstanceId.setName("bookInstanceId");
        fBookInstanceId.setValueClass(Long.class);
        addField(fBookInstanceId, jasperDesign);

        JRDesignField fTitle = new JRDesignField();
        fTitle.setName("title");
        fTitle.setValueClass(String.class);
        addField(fTitle, jasperDesign);

        JRDesignField fAuthor = new JRDesignField();
        fAuthor.setName("author");
        fAuthor.setValueClass(String.class);
        addField(fAuthor, jasperDesign);

        JRDesignBand columnHeader = new JRDesignBand();
        getColumnHeader(columnHeader).setHeight(40);

        int x = 0;
        int colHeight = 35;
        int padding = 2;

        // Column widths:
        int wSaleId    = 50;
        int wSoldAt    = 100;
        int wAmount    = 60;
        int wCurrency  = 60;
        int wBookInst  = 60;
        int wTitle     = 90;
        int wAuthor    = 60;


        JRDesignStaticText headerSaleId = new JRDesignStaticText();
        configureStaticText(columnHeader, headerSaleId, x, padding, wSaleId, colHeight, "Sale Id");
        x += wSaleId;

        JRDesignStaticText headerSoldAt = new JRDesignStaticText();
        configureStaticText(columnHeader, headerSoldAt, x, padding, wSoldAt, colHeight, "Sold At");
        x += wSoldAt;

        int xAmount = x;
        JRDesignStaticText headerAmount = new JRDesignStaticText();
        configureStaticText(columnHeader, headerAmount, x, padding, wAmount, colHeight, "Amount");
        x += wAmount;

        JRDesignStaticText headerCurrency = new JRDesignStaticText();
        configureStaticText(columnHeader, headerCurrency, x, padding, wCurrency, colHeight, "Currency");
        x += wCurrency;

        int xBookInstance = x;
        JRDesignStaticText headerBookInst = new JRDesignStaticText();
        configureStaticText(columnHeader, headerBookInst, x, padding, wBookInst, colHeight, "Book\nInstance ID");
        x += wBookInst;

        JRDesignStaticText headerTitle = new JRDesignStaticText();
        configureStaticText(columnHeader, headerTitle, x, padding, wTitle, colHeight, "Title");
        x += wTitle;

        JRDesignStaticText headerAuthor = new JRDesignStaticText();
        configureStaticText(columnHeader, headerAuthor, x, padding, wAuthor, colHeight, "Author");

        jasperDesign.setColumnHeader(columnHeader);

        JRDesignBand detailBand = new JRDesignBand();
        detailBand.setHeight(25);

        int totalWidth = jasperDesign.getColumnWidth();
        int lineY = padding + colHeight;
        JRDesignLine separator = new JRDesignLine();
        separator.setX(0);
        separator.setY(lineY);
        separator.setWidth(totalWidth);
        separator.setHeight(1);

        columnHeader.addElement(separator);

        jasperDesign.setColumnHeader(columnHeader);
        x = 0;

        JRDesignTextField tfSaleId = new JRDesignTextField();
        configureTextField(tfSaleId, x, wSaleId, new JRDesignExpression("$F{saleId}"), detailBand);
        x += wSaleId;

        JRDesignTextField tfSoldAt = new JRDesignTextField();
        configureTextField(tfSoldAt, x, wSoldAt, new JRDesignExpression("$F{soldAt}.toString()"), detailBand);
        x += wSoldAt;

        JRDesignTextField tfAmount = new JRDesignTextField();
        configureTextField(tfAmount, x, wAmount, new JRDesignExpression("$F{amount}"), detailBand);
        x += wAmount;

        JRDesignTextField tfCurrency = new JRDesignTextField();
        configureTextField(tfCurrency, x, wCurrency, new JRDesignExpression("$F{currency}"), detailBand);
        x += wCurrency;

        JRDesignTextField tfBookInst = new JRDesignTextField();
        configureTextField(tfBookInst, x, wBookInst, new JRDesignExpression("$F{bookInstanceId}"), detailBand);
        x += wBookInst;

        JRDesignTextField tfTitle = new JRDesignTextField();
        configureTextField(tfTitle, x, wTitle, new JRDesignExpression("$F{title}"), detailBand);
        x += wTitle;

        JRDesignTextField tfAuthor = new JRDesignTextField();
        configureTextField(tfAuthor, x, wAuthor, new JRDesignExpression("$F{author}"), detailBand);

        JRDesignSection detailSection = (JRDesignSection) jasperDesign.getDetailSection();
        detailSection.addBand(detailBand);

        JRDesignBand summaryBand = new JRDesignBand();
        summaryBand.setHeight(40);

        JRDesignLine separatorLine = new JRDesignLine();
        separatorLine.setX(0);
        separatorLine.setY(0);
        separatorLine.setWidth(jasperDesign.getColumnWidth());
        separatorLine.setHeight(1);

        summaryBand.addElement(separatorLine);

        JRDesignVariable totalVariable = new JRDesignVariable();
        configureJRDesignVariable(totalVariable, "total", Integer.class, CalculationEnum.COUNT,new JRDesignExpression("$F{saleId}"));

        JRDesignTextField totalField = new JRDesignTextField();
        totalField.setX(xBookInstance);
        totalField.setY(10);
        totalField.setWidth(wBookInst);
        totalField.setHeight(20);
        totalField.setHorizontalTextAlign(HorizontalTextAlignEnum.CENTER);
        totalField.setExpression(new JRDesignExpression("$V{total}"));
        totalField.setEvaluationTime(EvaluationTimeEnum.REPORT);

        summaryBand.addElement(totalField);
        try {
            jasperDesign.addVariable(totalVariable);
        }catch (JRException e) {
            throw new ReportException("Unable to add variable 'total' to summary band");
        }

        JRDesignVariable totalAmountVariable = new JRDesignVariable();
        configureJRDesignVariable(totalAmountVariable, "totalAmount", BigDecimal.class, CalculationEnum.SUM, new JRDesignExpression("$F{amount}"));

        JRDesignTextField totalAmountField = new JRDesignTextField();
        totalAmountField.setX(xAmount);
        totalAmountField.setY(10);
        totalAmountField.setWidth(wAmount);
        totalAmountField.setHeight(20);
        totalAmountField.setHorizontalTextAlign(HorizontalTextAlignEnum.CENTER);
        totalAmountField.setExpression(new JRDesignExpression("$V{totalAmount}"));
        totalAmountField.setEvaluationTime(EvaluationTimeEnum.REPORT);

        summaryBand.addElement(totalAmountField);
        try {
            jasperDesign.addVariable(totalAmountVariable);
        }catch (JRException e){
            throw new ReportException("Unable to add a variable 'totalAmount' to summary band");
        }
        JRDesignStaticText summaryTotal = new JRDesignStaticText();
        configureStaticText(summaryBand, summaryTotal, 0, padding, wSaleId, colHeight, "Total");

        jasperDesign.setSummary(summaryBand);

        JasperReport jasperReport;
        try {
            jasperReport = JasperCompileManager.compileReport(jasperDesign);
        }
        catch (JRException e) {
            throw new ReportException("Unable to compile jasper report");
        }

        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(rows);

        JasperPrint jasperPrint;
        try {

            jasperPrint = JasperFillManager.fillReport(jasperReport,
                    new HashMap<>(),
                    dataSource);
        }catch (JRException e) {
            throw new ReportException("Unable to fill the report");
        }

        File reportDir = new File(reportDirectoryPath);
        if (!reportDir.exists()) {
            try {

                Files.createDirectories(reportDir.toPath());
            }catch (IOException e) {
                throw new ReportException("Unable to create the report directory");
            }
        }

        String filename = "sales_report_" + System.currentTimeMillis() + ".pdf";
        String fullPath = reportDirectoryPath + File.separator + filename;

        try {
            JasperExportManager.exportReportToPdfFile(jasperPrint, fullPath);
        }catch (JRException e) {
            throw new ReportException("Unable to export the report");
        }
        File pdfFile = new File(fullPath);
        String subject = "Sales Report from " + startInstant + " to " + endInstant;
        String body    = "Please find attached the sales report PDF.";
        try {
            emailService.sendEmailWithAttachment(emailTo, subject, body, pdfFile);
        }catch (MessagingException e) {
            throw new ReportException("Unable to send the email");
        }
    }

    private static void configureJRDesignVariable(JRDesignVariable jrDesignVariable, String name, Class<?> valueClass, CalculationEnum calculationEnum, JRDesignExpression jRDesignExpression ) {
        jrDesignVariable.setName(name);
        jrDesignVariable.setValueClass(valueClass);
        jrDesignVariable.setCalculation(calculationEnum);
        jrDesignVariable.setExpression(jRDesignExpression);
        jrDesignVariable.setResetType(ResetTypeEnum.REPORT);
    }

    private static JRDesignBand getColumnHeader(JRDesignBand columnHeader) {
        return columnHeader;
    }

    private static void configureTextField(JRDesignTextField textField, int x, int width, JRDesignExpression designExpression, JRDesignBand detailBand) {
        textField.setX(x);
        textField.setY(0);
        textField.setWidth(width);
        textField.setHeight(20);
        textField.setVerticalTextAlign(VerticalTextAlignEnum.MIDDLE);
        textField.setHorizontalTextAlign(HorizontalTextAlignEnum.CENTER);
        textField.setExpression(designExpression);
        detailBand.addElement(textField);
    }

    private static void configureStaticText(JRDesignBand columnHeader, JRDesignStaticText header, int x, int padding, int width, int colHeight, String text) {
        header.setX(x);
        header.setY(padding);
        header.setWidth(width);
        header.setHeight(colHeight);
        header.setVerticalTextAlign(VerticalTextAlignEnum.MIDDLE);
        header.setHorizontalTextAlign(HorizontalTextAlignEnum.CENTER);
        header.setMode(ModeEnum.OPAQUE);
        header.setText(text);
        columnHeader.addElement(header);
    }

    private static void addField(JRDesignField field, JasperDesign jasperDesign ){
        try{
            jasperDesign.addField(field);
        }catch (JRException e){
            throw new ReportException("Unable to add a field");
        }
    }
}
