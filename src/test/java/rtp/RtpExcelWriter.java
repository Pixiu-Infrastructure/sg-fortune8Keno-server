package rtp;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Writes the final RTP summary to an Excel (.xlsx) file.
 * Uses Apache POI (poi + poi-ooxml), which is already in your pom.xml.
 */
public class RtpExcelWriter {

    public static void write(String filePath,
                             long totalRuns,
                             BigDecimal totalStake,
                              RTPDataToPrint rtpDataToPrint) throws IOException {

        // RTP % = totalWins / totalStake * 100, kept in BigDecimal to avoid int overflow / precision loss
        BigDecimal rtpPercent = totalStake.signum() == 0
                ? BigDecimal.ZERO
                : rtpDataToPrint.getTotalWins().multiply(BigDecimal.valueOf(100))
                .divide(totalStake, 6, RoundingMode.HALF_UP);
        BigDecimal hitRate = BigDecimal.valueOf(rtpDataToPrint.getTotalHitCount())
                .multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(totalRuns), 6, RoundingMode.HALF_UP);

        BigDecimal spot2HitRate = BigDecimal.valueOf(rtpDataToPrint.getTotalHits())
                .multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(rtpDataToPrint.getTotalSpotSelectedCount()), 6, RoundingMode.HALF_UP);

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("RTP Result");

            // Header style
            CellStyle headerStyle = workbook.createCellStyle();
            Font bold = workbook.createFont();
            bold.setBold(true);
            headerStyle.setFont(bold);

            // Header row
            Row header = sheet.createRow(0);
            createCell(header, 0, "Metric", headerStyle);
            createCell(header, 1, "Value", headerStyle);

            // Data rows
            addRow(sheet, 1, "Total Rounds", totalRuns);
            addRow(sheet, 2, "Total Stake", totalStake.doubleValue());
            addRow(sheet, 3, "Total Wins", rtpDataToPrint.getTotalWins().doubleValue());
            addRow(sheet, 4, "RTP (%)", rtpPercent.doubleValue());
            addRow(sheet, 5, "Hit rate", hitRate.doubleValue());
            addRow(sheet, 6, "Spot 2 Hit Rate", spot2HitRate.doubleValue());
            addRow(sheet, 7, "Max Win Amount", rtpDataToPrint.getMaxWinAmount());

            sheet.autoSizeColumn(0);
            sheet.autoSizeColumn(1);

            try (FileOutputStream out = new FileOutputStream(filePath)) {
                workbook.write(out);
            }
        }
    }

    private static void addRow(Sheet sheet, int rowIndex, String label, double value) {
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