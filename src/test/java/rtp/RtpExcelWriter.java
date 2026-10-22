package rtp;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
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
                .divide(totalStake,  new MathContext(4, RoundingMode.HALF_EVEN));
        BigDecimal hitRate = BigDecimal.valueOf(rtpDataToPrint.getTotalHitCount())
                .multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(totalRuns), new MathContext(4, RoundingMode.HALF_EVEN));

        BigDecimal spot2HitRate = BigDecimal.valueOf(rtpDataToPrint.getTotalHits())
                .multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(rtpDataToPrint.getTotalSpotSelectedCount()),  new MathContext(4, RoundingMode.HALF_EVEN));

        BigDecimal spot3HitRate = BigDecimal.valueOf(rtpDataToPrint.getMatched3NumbersCount())
                .multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(rtpDataToPrint.getSpot3SelectedCount()),  new MathContext(4, RoundingMode.HALF_EVEN));

        BigDecimal spot4HitRate = BigDecimal.valueOf(rtpDataToPrint.getMatched4NumbersCount())
                .multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(rtpDataToPrint.getSpot4SelectedCount()),  new MathContext(4, RoundingMode.HALF_EVEN));
        BigDecimal spot5HitRate = BigDecimal.valueOf(rtpDataToPrint.getMatched5NumbersCount())
                .multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(rtpDataToPrint.getSpot5SelectedCount()),  new MathContext(4, RoundingMode.HALF_EVEN));
        BigDecimal spot6HitRate = BigDecimal.valueOf(rtpDataToPrint.getMatched6NumbersCount())
                .multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(rtpDataToPrint.getSpot6SelectedCount()),  new MathContext(4, RoundingMode.HALF_EVEN));
        BigDecimal spot7HitRate = BigDecimal.valueOf(rtpDataToPrint.getMatched7NumbersCount())
                .multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(rtpDataToPrint.getSpot7SelectedCount()),  new MathContext(4, RoundingMode.HALF_EVEN));
        BigDecimal spot8HitRate = BigDecimal.valueOf(rtpDataToPrint.getMatched8NumbersCount())
                .multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(rtpDataToPrint.getSpot8SelectedCount()),  new MathContext(4, RoundingMode.HALF_EVEN));
        BigDecimal spot9HitRate = BigDecimal.valueOf(rtpDataToPrint.getMatched9NumbersCount())
                .multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(rtpDataToPrint.getSpot9SelectedCount()),  new MathContext(4, RoundingMode.HALF_EVEN));
        BigDecimal spot10HitRate = BigDecimal.valueOf(rtpDataToPrint.getMatched10NumbersCount())
                .multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(rtpDataToPrint.getSpot10SelectedCount()),  new MathContext(4, RoundingMode.HALF_EVEN));

        BigDecimal spot2RTPPercentage = totalStake.signum() == 0
                ? BigDecimal.ZERO
                : rtpDataToPrint.getTotalWinSpot2().multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(rtpDataToPrint.getTotalSpotSelectedCount()),  new MathContext(4, RoundingMode.HALF_EVEN));

        BigDecimal spot3RTPPercentage = totalStake.signum() == 0
                ? BigDecimal.ZERO
                : rtpDataToPrint.getTotalWinSpot3().multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(rtpDataToPrint.getSpot3SelectedCount()),  new MathContext(4, RoundingMode.HALF_EVEN));
        BigDecimal spot4RTPPercentage = totalStake.signum() == 0
                ? BigDecimal.ZERO
                : rtpDataToPrint.getTotalWinSpot4().multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(rtpDataToPrint.getSpot4SelectedCount()),  new MathContext(4, RoundingMode.HALF_EVEN));
        BigDecimal spot5RTPPercentage = totalStake.signum() == 0
                ? BigDecimal.ZERO
                : rtpDataToPrint.getTotalWinSpot5().multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(rtpDataToPrint.getSpot5SelectedCount()),  new MathContext(4, RoundingMode.HALF_EVEN));
        BigDecimal spot6RTPPercentage = totalStake.signum() == 0
                ? BigDecimal.ZERO
                : rtpDataToPrint.getTotalWinSpot6().multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(rtpDataToPrint.getSpot6SelectedCount()),  new MathContext(4, RoundingMode.HALF_EVEN));
        BigDecimal spot7RTPPercentage = totalStake.signum() == 0
                ? BigDecimal.ZERO
                : rtpDataToPrint.getTotalWinSpot7().multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(rtpDataToPrint.getSpot7SelectedCount()),  new MathContext(4, RoundingMode.HALF_EVEN));
        BigDecimal spot8RTPPercentage = totalStake.signum() == 0
                ? BigDecimal.ZERO
                : rtpDataToPrint.getTotalWinSpot8().multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(rtpDataToPrint.getSpot8SelectedCount()),  new MathContext(4, RoundingMode.HALF_EVEN));
        BigDecimal spot9RTPPercentage = totalStake.signum() == 0
                ? BigDecimal.ZERO
                : rtpDataToPrint.getTotalWinSpot9().multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(rtpDataToPrint.getSpot9SelectedCount()),  new MathContext(4, RoundingMode.HALF_EVEN));
        BigDecimal spot10RTPPercentage = totalStake.signum() == 0
                ? BigDecimal.ZERO
                : rtpDataToPrint.getTotalWinSpot10().multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(rtpDataToPrint.getSpot10SelectedCount()),  new MathContext(4, RoundingMode.HALF_EVEN));

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
            addRow(sheet, r++, "Total Rounds", totalRuns);
            addRow(sheet, r++, "Total Stake", totalStake.doubleValue());
            addRow(sheet, r++, "Total Wins", rtpDataToPrint.getTotalWins().doubleValue());
            addRow(sheet, r++, "RTP (%)", rtpPercent.doubleValue());
            addRow(sheet, r++, "Hit rate", hitRate.doubleValue());
            addRow(sheet, r++, "Max Win Amount", rtpDataToPrint.getMaxWinAmount());

            Row header2 = sheet.createRow(r++);
            createCell(header2, 0, "Spots", headerStyle);
            createCell(header2, 1, "Hit Rate (%)", headerStyle);
            createCell(header2, 2, "RTP (%)", headerStyle);

            //data rows for second header
            addRow(sheet, r++, " Spot 2 ", spot2HitRate.doubleValue(), spot2RTPPercentage.doubleValue());
            addRow(sheet, r++, " Spot 3 ", spot3HitRate.doubleValue(), spot3RTPPercentage.doubleValue());
            addRow(sheet, r++, " Spot 4 ", spot4HitRate.doubleValue(), spot4RTPPercentage.doubleValue());
            addRow(sheet, r++, " Spot 5 ", spot5HitRate.doubleValue(), spot5RTPPercentage.doubleValue());
            addRow(sheet, r++, " Spot 6 ", spot6HitRate.doubleValue(), spot6RTPPercentage.doubleValue());
            addRow(sheet, r++, " Spot 7 ", spot7HitRate.doubleValue(), spot7RTPPercentage.doubleValue());
            addRow(sheet, r++, " Spot 8 ", spot8HitRate.doubleValue(), spot8RTPPercentage.doubleValue());
            addRow(sheet, r++, " Spot 9 ", spot9HitRate.doubleValue(), spot9RTPPercentage.doubleValue());
            addRow(sheet, r++, " Spot 10 ", spot10HitRate.doubleValue(), spot10RTPPercentage.doubleValue());


            sheet.autoSizeColumn(0);
            sheet.autoSizeColumn(1);

            try (FileOutputStream out = new FileOutputStream(filePath)) {
                workbook.write(out);
            }
        }
    }

    private static void addRow(Sheet sheet, int rowIndex, String label, double value, double rtp) {
        Row row = sheet.createRow(rowIndex);
        row.createCell(0).setCellValue(label);
        row.createCell(1).setCellValue(value);
        row.createCell(2).setCellValue(rtp);
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