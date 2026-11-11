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
import java.util.Map;
import java.util.TreeMap;

public class RtpExcelWriterSimplified_Spot10 {

    public static void write(String filePath,
                             long totalRuns,
                             BigDecimal totalStake,
                             RTPDataToPrint rtpDataToPrint) throws IOException {

        String engineName = "Fortune 8 Keno";

        BigDecimal rtpPercent = totalStake.signum() == 0
                ? BigDecimal.ZERO
                : rtpDataToPrint.getTotalWins().multiply(BigDecimal.valueOf(100))
                .divide(totalStake, new MathContext(4, RoundingMode.HALF_EVEN));

        BigDecimal hitRate = BigDecimal.valueOf(rtpDataToPrint.getTotalHitCount())
                .multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(totalRuns), new MathContext(4, RoundingMode.HALF_EVEN));

        BigDecimal multiplierTriggerPct = BigDecimal.valueOf(rtpDataToPrint.getMultiplierTriggeredCount())
                .multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(totalRuns), new MathContext(4, RoundingMode.HALF_EVEN));

        BigDecimal spot10HitRate = BigDecimal.valueOf(rtpDataToPrint.getMatched10NumbersCount())
                .multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(rtpDataToPrint.getSpot10SelectedCount()), new MathContext(4, RoundingMode.HALF_EVEN));

        BigDecimal spot0HitRateFor10Spots = BigDecimal.valueOf(rtpDataToPrint.getMatched_0_NumberCount_spot10())
                .multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(rtpDataToPrint.getSpot10SelectedCount()), new MathContext(6, RoundingMode.HALF_EVEN));
        BigDecimal spot1HitRateFor10Spots = BigDecimal.valueOf(rtpDataToPrint.getMatched_1_NumberCount_spot10())
                .multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(rtpDataToPrint.getSpot10SelectedCount()), new MathContext(6, RoundingMode.HALF_EVEN));
        BigDecimal spot2HitRateFor10Spots = BigDecimal.valueOf(rtpDataToPrint.getMatched_2_NumberCount_spot10())
                .multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(rtpDataToPrint.getSpot10SelectedCount()), new MathContext(6, RoundingMode.HALF_EVEN));
        BigDecimal spot3HitRateFor10Spots = BigDecimal.valueOf(rtpDataToPrint.getMatched_3_NumberCount_spot10())
                .multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(rtpDataToPrint.getSpot10SelectedCount()), new MathContext(6, RoundingMode.HALF_EVEN));
        BigDecimal spot4HitRateFor10Spots = BigDecimal.valueOf(rtpDataToPrint.getMatched_4_NumberCount_spot10())
                .multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(rtpDataToPrint.getSpot10SelectedCount()), new MathContext(6, RoundingMode.HALF_EVEN));
        BigDecimal spot5HitRateFor10Spots = BigDecimal.valueOf(rtpDataToPrint.getMatched_5_NumberCount_spot10())
                .multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(rtpDataToPrint.getSpot10SelectedCount()), new MathContext(6, RoundingMode.HALF_EVEN));
        BigDecimal spot6HitRateFor10Spots = BigDecimal.valueOf(rtpDataToPrint.getMatched_6_NumberCount_spot10())
                .multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(rtpDataToPrint.getSpot10SelectedCount()), new MathContext(6, RoundingMode.HALF_EVEN));
        BigDecimal spot7HitRateFor10Spots = BigDecimal.valueOf(rtpDataToPrint.getMatched_7_NumberCount_spot10())
                .multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(rtpDataToPrint.getSpot10SelectedCount()), new MathContext(6, RoundingMode.HALF_EVEN));
        BigDecimal spot8HitRateFor10Spots = BigDecimal.valueOf(rtpDataToPrint.getMatched_8_NumberCount_spot10())
                .multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(rtpDataToPrint.getSpot10SelectedCount()), new MathContext(6, RoundingMode.HALF_EVEN));
        BigDecimal spot9HitRateFor10Spots = BigDecimal.valueOf(rtpDataToPrint.getMatched_9_NumberCount_spot10())
                .multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(rtpDataToPrint.getSpot10SelectedCount()), new MathContext(6, RoundingMode.HALF_EVEN));
        BigDecimal spot10HitRateFor10Spots = BigDecimal.valueOf(rtpDataToPrint.getMatched_10_NumberCount_spot10())
                .multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(rtpDataToPrint.getSpot10SelectedCount()), new MathContext(6, RoundingMode.HALF_EVEN));

        BigDecimal spot10RTPPercentage = totalStake.signum() == 0
                ? BigDecimal.ZERO
                : rtpDataToPrint.getTotalWinSpot10().multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(rtpDataToPrint.getSpot10SelectedCount()), new MathContext(4, RoundingMode.HALF_EVEN));

        // Variance and Standard Deviation
        long spotCount = totalRuns;
        BigDecimal spotTotalWin = rtpDataToPrint.getTotalWinSpot10();
        BigDecimal spotSumSq = rtpDataToPrint.getSumOfSquaresSpot10();

        double mean = spotTotalWin.doubleValue() / spotCount;
        double e2 = spotSumSq.doubleValue() / spotCount;
        double spotVariance = e2 - mean * mean;
        double spotSD = Math.sqrt(Math.max(0, spotVariance));

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("RTP Result");

            CellStyle headerStyle = workbook.createCellStyle();
            Font bold = workbook.createFont();
            bold.setBold(true);
            headerStyle.setFont(bold);

            int r = 0;
            Row header = sheet.createRow(r++);
            createCell(header, 0, "Metric", headerStyle);
            createCell(header, 1, "Value", headerStyle);

            addRow(sheet, r++, "Engine Name", engineName);
            addRow(sheet, r++, "Total Games", totalRuns);
            addRow(sheet, r++, "Total Hits", rtpDataToPrint.getTotalHitCount());
            addRow(sheet, r++, "Overall Hit Freq (%)", hitRate);
            addRow(sheet, r++, "Total amount staked", totalStake);
            addRow(sheet, r++, "Total Wins", rtpDataToPrint.getTotalWins());
            addRow(sheet, r++, "Game RTP (%)", rtpPercent);
            addRow(sheet, r++, "Date created: ", getCurrentDateString());
            addRow(sheet, r++, "Win Hit rate (%)", hitRate);
            addRow(sheet, r++, "Largest win", rtpDataToPrint.getMaxWinAmount());
            sheet.createRow(r++);
            addRow(sheet, r++, "Multiplier Triggers (count)", rtpDataToPrint.getMultiplierTriggeredCount());
            addRow(sheet, r++, "Multiplier Triggers (%)", multiplierTriggerPct);
            sheet.createRow(r++);
            addRow(sheet, r++, "Award >= 10 (count)", rtpDataToPrint.getAwardGreaterThanEqualTo10());
            addRow(sheet, r++, "Award >= 20 (count)", rtpDataToPrint.getAwardGreaterThanEqualTo20());
            addRow(sheet, r++, "Award >= 50 (count)", rtpDataToPrint.getAwardGreaterThanEqualTo50());
            addRow(sheet, r++, "Award >= 100 (count)", rtpDataToPrint.getAwardGreaterThanEqualTo100());
            addRow(sheet, r++, "Award >= 1000 (count)", rtpDataToPrint.getAwardGreaterThanEqualTo1000());
            sheet.createRow(r++);
            sheet.createRow(r++);
            sheet.createRow(r++);

            Row header2 = sheet.createRow(r++);
            createCell(header2, 0, "Spots", headerStyle);
            createCell(header2, 1, "Hit Rate (%)", headerStyle);
            createCell(header2, 2, "RTP (%)", headerStyle);
            createCell(header2, 3, "Variance", headerStyle);
            createCell(header2, 4, "Win standard deviation", headerStyle);

            addSpotRow(sheet, r++, " Spot 10 ", spot10HitRate, spot10RTPPercentage, spotVariance, spotSD);

            sheet.createRow(r++);

            Row header3 = sheet.createRow(r++);
            createCell(header3, 0, "Spot10 Breakup", headerStyle);
            Row header4 = sheet.createRow(r++);
            createCell(header4, 0, "hits", headerStyle);
            createCell(header4, 1, "frequency (%)", headerStyle);

            addRow(sheet, r++, " 0 ", spot0HitRateFor10Spots);
            addRow(sheet, r++, " 1 ", spot1HitRateFor10Spots);
            addRow(sheet, r++, " 2 ", spot2HitRateFor10Spots);
            addRow(sheet, r++, " 3 ", spot3HitRateFor10Spots);
            addRow(sheet, r++, " 4 ", spot4HitRateFor10Spots);
            addRow(sheet, r++, " 5 ", spot5HitRateFor10Spots);
            addRow(sheet, r++, " 6 ", spot6HitRateFor10Spots);
            addRow(sheet, r++, " 7 ", spot7HitRateFor10Spots);
            addRow(sheet, r++, " 8 ", spot8HitRateFor10Spots);
            addRow(sheet, r++, " 9 ", spot9HitRateFor10Spots);
            addRow(sheet, r++, " 10 ", spot10HitRateFor10Spots);

            sheet.createRow(r++);

            Row header5 = sheet.createRow(r++);
            createCell(header5, 0, "Hit Frequencies by Win Amount", headerStyle);
            Row header6 = sheet.createRow(r++);
            createCell(header6, 0, "Win Amount", headerStyle);
            createCell(header6, 1, "Count", headerStyle);

            Map<Long, Long> sortedFreq = new TreeMap<>(rtpDataToPrint.getWinAmountFrequency());
            for (Map.Entry<Long, Long> entry : sortedFreq.entrySet()) {
                addRow(sheet, r++, String.valueOf(entry.getKey()), entry.getValue());
            }

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

    public static String getCurrentDateString() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MMMM-dd");
        LocalDateTime now = LocalDateTime.now();
        return dtf.format(now);
    }

    private static void addRow(Sheet sheet, int rowIndex, String label, BigDecimal value) {
        Row row = sheet.createRow(rowIndex);
        row.createCell(0).setCellValue(label);
        row.createCell(1).setCellValue(value.doubleValue());
    }
    private static void addRow(Sheet sheet, int rowIndex, String label, int value) {
        Row row = sheet.createRow(rowIndex);
        row.createCell(0).setCellValue(label);
        row.createCell(1).setCellValue(value);
    }
    private static void addRow(Sheet sheet, int rowIndex, String label, long value) {
        Row row = sheet.createRow(rowIndex);
        row.createCell(0).setCellValue(label);
        row.createCell(1).setCellValue(value);
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
