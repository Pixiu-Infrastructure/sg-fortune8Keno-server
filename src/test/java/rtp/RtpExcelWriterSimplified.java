package rtp;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Writes the final RTP summary to an Excel (.xlsx) file.
 * Uses Apache POI (poi + poi-ooxml), which is already in your pom.xml.
 */
public class RtpExcelWriterSimplified {

    public static void write(String filePath,
                             long totalRuns,
                             BigDecimal totalStake,
                              RTPDataToPrint rtpDataToPrint) throws IOException {

        String engineName = "Fortune 8 Keno";

        // RTP % = totalWins / totalStake * 100, kept in BigDecimal to avoid int overflow / precision loss
        BigDecimal rtpPercent = totalStake.signum() == 0
                ? BigDecimal.ZERO
                : rtpDataToPrint.getTotalWins().multiply(BigDecimal.valueOf(100))
                .divide(totalStake,  new MathContext(4, RoundingMode.HALF_EVEN));

        // --- Variance and Standard Deviation per spot ---


        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("RTP Result");

            // Header style
            CellStyle headerStyle = workbook.createCellStyle();
            Font bold = workbook.createFont();
            bold.setBold(true);
            headerStyle.setFont(bold);

            // Header row
            int r = 0;
            Row header = sheet.createRow(r++);
            createCell(header, 0, "Metric", headerStyle);
            createCell(header, 1, "Value", headerStyle);
            // Data rows
            addRow(sheet, r++, "Engine Name", engineName);
            addRow(sheet, r++, "Total number of plays", BigDecimal.valueOf(totalRuns));
            addRow(sheet, r++, "Total amount staked", totalStake);
            addRow(sheet, r++, "Total Wins", rtpDataToPrint.getTotalWins());
            addRow(sheet, r++, "Game RTP (%)", rtpPercent);
            addRow(sheet, r++, "Date created: ", getCurrentDateString());


            try (FileOutputStream out = new FileOutputStream(filePath)) {
                workbook.write(out);
            }
        }
    }



    public static String getCurrentDateString(){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MMMM-dd"); //2023-December-19-01-17-08-977
        LocalDateTime now = LocalDateTime.now();

        return dtf.format(now);
    }


    private static void addRow(Sheet sheet, int rowIndex, String label, BigDecimal value) {
        Row row = sheet.createRow(rowIndex);
        row.createCell(0).setCellValue(label);
        row.createCell(1).setCellValue(value.doubleValue());
    }

    private static void addRow(Sheet sheet, int rowIndex, String label, String value) {
        Row row = sheet.createRow(rowIndex);
        row.createCell(0).setCellValue(label);
        row.createCell(1).setCellValue(value);
    }

    private static void createCell(Row row, int col, String value, CellStyle style) {
        Cell cell = row.createCell(col);
        cell.setCellValue(value);
        cell.setCellStyle(style);
    }
}