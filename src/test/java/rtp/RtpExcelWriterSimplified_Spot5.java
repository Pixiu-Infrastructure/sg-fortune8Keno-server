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
public class RtpExcelWriterSimplified_Spot5 {

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

        BigDecimal spot5HitRate = BigDecimal.valueOf(rtpDataToPrint.getMatched5NumbersCount())
                .multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(rtpDataToPrint.getSpot5SelectedCount()),  new MathContext(4, RoundingMode.HALF_EVEN));
        BigDecimal spot0HitRateFor5Spots = BigDecimal.valueOf(rtpDataToPrint.getMatched_0_NumberCount_spot5())
                .multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(rtpDataToPrint.getSpot5SelectedCount()),  new MathContext(6, RoundingMode.HALF_EVEN));
        BigDecimal spot1HitRateFor5Spots = BigDecimal.valueOf(rtpDataToPrint.getMatched_1_NumberCount_spot5())
                .multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(rtpDataToPrint.getSpot5SelectedCount()),  new MathContext(6, RoundingMode.HALF_EVEN));
        BigDecimal spot2HitRateFor5Spots = BigDecimal.valueOf(rtpDataToPrint.getMatched_2_NumberCount_spot5())
                .multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(rtpDataToPrint.getSpot5SelectedCount()),  new MathContext(6, RoundingMode.HALF_EVEN));
        BigDecimal spot3HitRateFor5Spots = BigDecimal.valueOf(rtpDataToPrint.getMatched_3_NumberCount_spot5())
                .multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(rtpDataToPrint.getSpot5SelectedCount()),  new MathContext(6, RoundingMode.HALF_EVEN));
        BigDecimal spot4HitRateFor5Spots = BigDecimal.valueOf(rtpDataToPrint.getMatched_4_NumberCount_spot5())
                .multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(rtpDataToPrint.getSpot5SelectedCount()),  new MathContext(6, RoundingMode.HALF_EVEN));
        BigDecimal spot5HitRateFor5Spots = BigDecimal.valueOf(rtpDataToPrint.getMatched_5_NumberCount_spot5())
                .multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(rtpDataToPrint.getSpot5SelectedCount()),  new MathContext(6, RoundingMode.HALF_EVEN));
        BigDecimal spot5RTPPercentage = totalStake.signum() == 0
                ? BigDecimal.ZERO
                : rtpDataToPrint.getTotalWinSpot5().multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(rtpDataToPrint.getSpot5SelectedCount()),  new MathContext(4, RoundingMode.HALF_EVEN));


        // --- Variance and Standard Deviation per spot ---
        double spotVariance = 0.0; // index 2..10
        double spotSD       = 0.0;
        long   spotCount    = totalRuns;


        BigDecimal spotTotalWin =  rtpDataToPrint.getTotalWinSpot5();


        BigDecimal spotSumSq =      rtpDataToPrint.getSumOfSquaresSpot5();



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

            addSpotRow(sheet, r++, " Spot 3 ",  spot5HitRate,  spot5RTPPercentage,  spotVariance,  spotSD);;

            //data rows for second header
            sheet.createRow(r++);

            Row header3 = sheet.createRow(r++);

            createCell(header3, 0, "Spot5 Breakup", headerStyle);
            Row header4 = sheet.createRow(r++);
            createCell(header4, 0, "hits", headerStyle);
            createCell(header4, 1, "frequency (%)", headerStyle);




            addRow(sheet, r++, " 0 ", spot0HitRateFor5Spots);
            addRow(sheet, r++, " 1 ", spot1HitRateFor5Spots);
            addRow(sheet, r++, " 2 ", spot2HitRateFor5Spots);
            addRow(sheet, r++, " 3 ", spot3HitRateFor5Spots);
            addRow(sheet, r++, " 4 ", spot4HitRateFor5Spots);
            addRow(sheet, r++, " 5 ", spot5HitRateFor5Spots);
            //data rows for fourth header


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