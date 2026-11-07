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
public class RtpExcelWriterSimplified_Spot2 {

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
        BigDecimal hitRate = BigDecimal.valueOf(rtpDataToPrint.getTotalHitCount())
                .multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(totalRuns), new MathContext(4, RoundingMode.HALF_EVEN));

        BigDecimal spot2HitRate = BigDecimal.valueOf(rtpDataToPrint.getTotalSpot2Hits())
                .multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(rtpDataToPrint.getTotalSpot2SelectedCount()),  new MathContext(4, RoundingMode.HALF_EVEN));

        BigDecimal spot0HitRateFor2Spots = BigDecimal.valueOf(rtpDataToPrint.getMatched0NumberCount_spot2())
                .multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(rtpDataToPrint.getTotalSpot2SelectedCount()),  new MathContext(6, RoundingMode.HALF_EVEN));

        BigDecimal spot1HitRateFor2Spots = BigDecimal.valueOf(rtpDataToPrint.getMatched1NumberCount_spot2())
                .multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(rtpDataToPrint.getTotalSpot2SelectedCount()),  new MathContext(4, RoundingMode.HALF_EVEN));
        BigDecimal spot2RTPPercentage = totalStake.signum() == 0
                ? BigDecimal.ZERO
                : rtpDataToPrint.getTotalWins().multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(rtpDataToPrint.getTotalSpot2SelectedCount()),  new MathContext(4, RoundingMode.HALF_EVEN));


        // --- Variance and Standard Deviation per spot ---
        // --- Variance and Standard Deviation per spot ---
        double spotVariance = 0.0; // index 2..10
        double spotSD       = 0.0;
        long   spotCount    = rtpDataToPrint.getTotalSpot2SelectedCount();


        BigDecimal spotTotalWin =  rtpDataToPrint.getTotalWinSpot2();


        BigDecimal spotSumSq =      rtpDataToPrint.getSumOfSquaresSpot2();



        double mean = spotTotalWin.doubleValue() / spotCount;
        double e2   = spotSumSq.doubleValue()   / spotCount;
        spotVariance = e2 - mean * mean;
        spotSD       = Math.sqrt(Math.max(0, spotVariance));
     /**   for (int s = 2; s <= 10; s++) {
            if (spotCount[s] > 0) {
                double mean = spotTotalWin[s].doubleValue() / spotCount[s];
                double e2   = spotSumSq[s].doubleValue()   / spotCount[s];
                spotVariance[s] = e2 - mean * mean;
                spotSD[s]       = Math.sqrt(Math.max(0, spotVariance[s]));
            }
        }
      **/

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
            addRow(sheet, r++, "Win Hit rate", hitRate);
            addRow(sheet, r++, "Largest win", rtpDataToPrint.getMaxWinAmount());
            sheet.createRow(r++);
            sheet.createRow(r++);
            sheet.createRow(r++);
            Row header2 = sheet.createRow(r++);
            createCell(header2, 0, "Spots", headerStyle);
            createCell(header2, 1, "Hit Rate (%)", headerStyle);
            createCell(header2, 2, "RTP (%)", headerStyle);
            createCell(header2, 3, "Variance", headerStyle);
            createCell(header2, 4, "Win standard deviation", headerStyle);

            addSpotRow(sheet, r++, " Spot 2 ",  spot2HitRate,  spot2RTPPercentage,  spotVariance,  spotSD);

            //data rows for second header
            sheet.createRow(r++);

            Row header3 = sheet.createRow(r++);

            createCell(header3, 0, "Spot2 Breakup", headerStyle);

            Row header4 = sheet.createRow(r++);

            createCell(header4, 0, "hits", headerStyle);
            createCell(header4, 1, "frequency (%)", headerStyle);


            //data rows for fourth header

            addRow(sheet, r++, " 0 ", spot0HitRateFor2Spots);
            addRow(sheet, r++, " 1 ", spot1HitRateFor2Spots);
            addRow(sheet, r++, " 2 ", spot2HitRate);
            sheet.createRow(r++);

            try (FileOutputStream out = new FileOutputStream(filePath)) {
                workbook.write(out);
            }
        }
    }


    private static void addSpotRow(Sheet sheet, int rowIndex, String label,
                                   BigDecimal hitRate, BigDecimal rtpPct,
                                   double variance, double sd) {
        Row row = sheet.createRow(rowIndex);
        row.createCell(0).setCellValue(label);
        row.createCell(1).setCellValue(hitRate.doubleValue());
        row.createCell(2).setCellValue(rtpPct.doubleValue());
        row.createCell(3).setCellValue(variance);
        row.createCell(4).setCellValue(sd);
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