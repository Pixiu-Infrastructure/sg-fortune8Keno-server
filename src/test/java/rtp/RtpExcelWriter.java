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

        BigDecimal spot2HitRate = BigDecimal.valueOf(rtpDataToPrint.getTotalSpot2Hits())
                .multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(rtpDataToPrint.getTotalSpot2SelectedCount()),  new MathContext(4, RoundingMode.HALF_EVEN));

        BigDecimal spot0HitRateFor2Spots = BigDecimal.valueOf(rtpDataToPrint.getMatched0NumberCount_spot2())
                .multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(rtpDataToPrint.getTotalSpot2SelectedCount()),  new MathContext(6, RoundingMode.HALF_EVEN));

        BigDecimal spot1HitRateFor2Spots = BigDecimal.valueOf(rtpDataToPrint.getMatched1NumberCount_spot2())
                .multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(rtpDataToPrint.getTotalSpot2SelectedCount()),  new MathContext(4, RoundingMode.HALF_EVEN));

        BigDecimal spot3HitRate = BigDecimal.valueOf(rtpDataToPrint.getMatched3NumbersCount())
                .multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(rtpDataToPrint.getSpot3SelectedCount()),  new MathContext(4, RoundingMode.HALF_EVEN));
        BigDecimal spot0HitRateFor3Spots = BigDecimal.valueOf(rtpDataToPrint.getMatched_0_NumberCount_spot3())
                .multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(rtpDataToPrint.getSpot3SelectedCount()),  new MathContext(6, RoundingMode.HALF_EVEN));
        BigDecimal spot1HitRateFor3Spots = BigDecimal.valueOf(rtpDataToPrint.getMatched_1_NumberCount_spot3())
                .multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(rtpDataToPrint.getSpot3SelectedCount()),  new MathContext(6, RoundingMode.HALF_EVEN));
        BigDecimal spot2HitRateFor3Spots = BigDecimal.valueOf(rtpDataToPrint.getMatched_2_NumberCount_spot3())
                .multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(rtpDataToPrint.getSpot3SelectedCount()),  new MathContext(6, RoundingMode.HALF_EVEN));
        BigDecimal spot3HitRateFor3Spots = BigDecimal.valueOf(rtpDataToPrint.getMatched_3_NumberCount_spot3())
                .multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(rtpDataToPrint.getSpot3SelectedCount()),  new MathContext(6, RoundingMode.HALF_EVEN));


        BigDecimal spot4HitRate = BigDecimal.valueOf(rtpDataToPrint.getMatched4NumbersCount())
                .multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(rtpDataToPrint.getSpot4SelectedCount()),  new MathContext(4, RoundingMode.HALF_EVEN));

        BigDecimal spot0HitRateFor4Spots = BigDecimal.valueOf(rtpDataToPrint.getMatched_0_NumberCount_spot4())
                .multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(rtpDataToPrint.getSpot4SelectedCount()),  new MathContext(6, RoundingMode.HALF_EVEN));
        BigDecimal spot1HitRateFor4Spots = BigDecimal.valueOf(rtpDataToPrint.getMatched_1_NumberCount_spot4())
                .multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(rtpDataToPrint.getSpot4SelectedCount()),  new MathContext(6, RoundingMode.HALF_EVEN));
        BigDecimal spot2HitRateFor4Spots = BigDecimal.valueOf(rtpDataToPrint.getMatched_2_NumberCount_spot4())
                .multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(rtpDataToPrint.getSpot4SelectedCount()),  new MathContext(6, RoundingMode.HALF_EVEN));
        BigDecimal spot3HitRateFor4Spots = BigDecimal.valueOf(rtpDataToPrint.getMatched_3_NumberCount_spot4())
                .multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(rtpDataToPrint.getSpot4SelectedCount()),  new MathContext(6, RoundingMode.HALF_EVEN));
        BigDecimal spot4HitRateFor4Spots = BigDecimal.valueOf(rtpDataToPrint.getMatched_4_NumberCount_spot4())
                .multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(rtpDataToPrint.getSpot4SelectedCount()),  new MathContext(6, RoundingMode.HALF_EVEN));

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
        BigDecimal spot6HitRate = BigDecimal.valueOf(rtpDataToPrint.getMatched6NumbersCount())
                .multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(rtpDataToPrint.getSpot6SelectedCount()),  new MathContext(4, RoundingMode.HALF_EVEN));
        BigDecimal spot0HitRateFor6Spots = BigDecimal.valueOf(rtpDataToPrint.getMatched_0_NumberCount_spot6())
                .multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(rtpDataToPrint.getSpot6SelectedCount()),  new MathContext(6, RoundingMode.HALF_EVEN));
        BigDecimal spot1HitRateFor6Spots = BigDecimal.valueOf(rtpDataToPrint.getMatched_1_NumberCount_spot6())
                .multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(rtpDataToPrint.getSpot6SelectedCount()),  new MathContext(6, RoundingMode.HALF_EVEN));
        BigDecimal spot2HitRateFor6Spots = BigDecimal.valueOf(rtpDataToPrint.getMatched_2_NumberCount_spot6())
                .multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(rtpDataToPrint.getSpot6SelectedCount()),  new MathContext(6, RoundingMode.HALF_EVEN));
        BigDecimal spot3HitRateFor6Spots = BigDecimal.valueOf(rtpDataToPrint.getMatched_3_NumberCount_spot6())
                .multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(rtpDataToPrint.getSpot6SelectedCount()),  new MathContext(6, RoundingMode.HALF_EVEN));
        BigDecimal spot4HitRateFor6Spots = BigDecimal.valueOf(rtpDataToPrint.getMatched_4_NumberCount_spot6())
                .multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(rtpDataToPrint.getSpot6SelectedCount()),  new MathContext(6, RoundingMode.HALF_EVEN));
        BigDecimal spot5HitRateFor6Spots = BigDecimal.valueOf(rtpDataToPrint.getMatched_5_NumberCount_spot6())
                .multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(rtpDataToPrint.getSpot6SelectedCount()),  new MathContext(6, RoundingMode.HALF_EVEN));
        BigDecimal spot6HitRateFor6Spots = BigDecimal.valueOf(rtpDataToPrint.getMatched_6_NumberCount_spot6())
                .multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(rtpDataToPrint.getSpot6SelectedCount()),  new MathContext(6, RoundingMode.HALF_EVEN));
        BigDecimal spot7HitRate = BigDecimal.valueOf(rtpDataToPrint.getMatched7NumbersCount())
                .multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(rtpDataToPrint.getSpot7SelectedCount()),  new MathContext(4, RoundingMode.HALF_EVEN));
        BigDecimal spot0HitRateFor7Spots = BigDecimal.valueOf(rtpDataToPrint.getMatched_0_NumberCount_spot7())
                .multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(rtpDataToPrint.getSpot7SelectedCount()),  new MathContext(6, RoundingMode.HALF_EVEN));
        BigDecimal spot1HitRateFor7Spots = BigDecimal.valueOf(rtpDataToPrint.getMatched_1_NumberCount_spot7())
                .multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(rtpDataToPrint.getSpot7SelectedCount()),  new MathContext(6, RoundingMode.HALF_EVEN));
        BigDecimal spot2HitRateFor7Spots = BigDecimal.valueOf(rtpDataToPrint.getMatched_2_NumberCount_spot7())
                .multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(rtpDataToPrint.getSpot7SelectedCount()),  new MathContext(6, RoundingMode.HALF_EVEN));
        BigDecimal spot3HitRateFor7Spots = BigDecimal.valueOf(rtpDataToPrint.getMatched_3_NumberCount_spot7())
                .multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(rtpDataToPrint.getSpot7SelectedCount()),  new MathContext(6, RoundingMode.HALF_EVEN));
        BigDecimal spot4HitRateFor7Spots = BigDecimal.valueOf(rtpDataToPrint.getMatched_4_NumberCount_spot7())
                .multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(rtpDataToPrint.getSpot7SelectedCount()),  new MathContext(6, RoundingMode.HALF_EVEN));
        BigDecimal spot5HitRateFor7Spots = BigDecimal.valueOf(rtpDataToPrint.getMatched_5_NumberCount_spot7())
                .multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(rtpDataToPrint.getSpot7SelectedCount()),  new MathContext(6, RoundingMode.HALF_EVEN));
        BigDecimal spot6HitRateFor7Spots = BigDecimal.valueOf(rtpDataToPrint.getMatched_6_NumberCount_spot7())
                .multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(rtpDataToPrint.getSpot7SelectedCount()),  new MathContext(6, RoundingMode.HALF_EVEN));
        BigDecimal spot7HitRateFor7Spots = BigDecimal.valueOf(rtpDataToPrint.getMatched_7_NumberCount_spot7())
                .multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(rtpDataToPrint.getSpot7SelectedCount()),  new MathContext(6, RoundingMode.HALF_EVEN));

        BigDecimal spot8HitRate = BigDecimal.valueOf(rtpDataToPrint.getMatched8NumbersCount())
                .multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(rtpDataToPrint.getSpot8SelectedCount()),  new MathContext(4, RoundingMode.HALF_EVEN));
        BigDecimal spot0HitRateFor8Spots = BigDecimal.valueOf(rtpDataToPrint.getMatched_0_NumberCount_spot8())
                .multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(rtpDataToPrint.getSpot8SelectedCount()),  new MathContext(6, RoundingMode.HALF_EVEN));
        BigDecimal spot1HitRateFor8Spots = BigDecimal.valueOf(rtpDataToPrint.getMatched_1_NumberCount_spot8())
                .multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(rtpDataToPrint.getSpot8SelectedCount()),  new MathContext(6, RoundingMode.HALF_EVEN));
        BigDecimal spot2HitRateFor8Spots = BigDecimal.valueOf(rtpDataToPrint.getMatched_2_NumberCount_spot8())
                .multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(rtpDataToPrint.getSpot8SelectedCount()),  new MathContext(6, RoundingMode.HALF_EVEN));
        BigDecimal spot3HitRateFor8Spots = BigDecimal.valueOf(rtpDataToPrint.getMatched_3_NumberCount_spot8())
                .multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(rtpDataToPrint.getSpot8SelectedCount()),  new MathContext(6, RoundingMode.HALF_EVEN));
        BigDecimal spot4HitRateFor8Spots = BigDecimal.valueOf(rtpDataToPrint.getMatched_4_NumberCount_spot8())
                .multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(rtpDataToPrint.getSpot8SelectedCount()),  new MathContext(6, RoundingMode.HALF_EVEN));
        BigDecimal spot5HitRateFor8Spots = BigDecimal.valueOf(rtpDataToPrint.getMatched_5_NumberCount_spot8())
                .multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(rtpDataToPrint.getSpot8SelectedCount()),  new MathContext(6, RoundingMode.HALF_EVEN));
        BigDecimal spot6HitRateFor8Spots = BigDecimal.valueOf(rtpDataToPrint.getMatched_6_NumberCount_spot8())
                .multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(rtpDataToPrint.getSpot8SelectedCount()),  new MathContext(6, RoundingMode.HALF_EVEN));
        BigDecimal spot7HitRateFor8Spots = BigDecimal.valueOf(rtpDataToPrint.getMatched_7_NumberCount_spot8())
                .multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(rtpDataToPrint.getSpot8SelectedCount()),  new MathContext(6, RoundingMode.HALF_EVEN));
        BigDecimal spot8HitRateFor8Spots = BigDecimal.valueOf(rtpDataToPrint.getMatched_8_NumberCount_spot8())
                .multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(rtpDataToPrint.getSpot8SelectedCount()),  new MathContext(6, RoundingMode.HALF_EVEN));
        BigDecimal spot9HitRate = BigDecimal.valueOf(rtpDataToPrint.getMatched9NumbersCount())
                .multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(rtpDataToPrint.getSpot9SelectedCount()),  new MathContext(4, RoundingMode.HALF_EVEN));
        BigDecimal spot0HitRateFor9Spots = BigDecimal.valueOf(rtpDataToPrint.getMatched_0_NumberCount_spot9())
                .multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(rtpDataToPrint.getSpot9SelectedCount()),  new MathContext(6, RoundingMode.HALF_EVEN));
        BigDecimal spot1HitRateFor9Spots = BigDecimal.valueOf(rtpDataToPrint.getMatched_1_NumberCount_spot9())
                .multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(rtpDataToPrint.getSpot9SelectedCount()),  new MathContext(6, RoundingMode.HALF_EVEN));
        BigDecimal spot2HitRateFor9Spots = BigDecimal.valueOf(rtpDataToPrint.getMatched_2_NumberCount_spot9())
                .multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(rtpDataToPrint.getSpot9SelectedCount()),  new MathContext(6, RoundingMode.HALF_EVEN));
        BigDecimal spot3HitRateFor9Spots = BigDecimal.valueOf(rtpDataToPrint.getMatched_3_NumberCount_spot9())
                .multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(rtpDataToPrint.getSpot9SelectedCount()),  new MathContext(6, RoundingMode.HALF_EVEN));
        BigDecimal spot4HitRateFor9Spots = BigDecimal.valueOf(rtpDataToPrint.getMatched_4_NumberCount_spot9())
                .multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(rtpDataToPrint.getSpot9SelectedCount()),  new MathContext(6, RoundingMode.HALF_EVEN));
        BigDecimal spot5HitRateFor9Spots = BigDecimal.valueOf(rtpDataToPrint.getMatched_5_NumberCount_spot9())
                .multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(rtpDataToPrint.getSpot9SelectedCount()),  new MathContext(6, RoundingMode.HALF_EVEN));
        BigDecimal spot6HitRateFor9Spots = BigDecimal.valueOf(rtpDataToPrint.getMatched_6_NumberCount_spot9())
                .multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(rtpDataToPrint.getSpot9SelectedCount()),  new MathContext(6, RoundingMode.HALF_EVEN));
        BigDecimal spot7HitRateFor9Spots = BigDecimal.valueOf(rtpDataToPrint.getMatched_7_NumberCount_spot9())
                .multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(rtpDataToPrint.getSpot9SelectedCount()),  new MathContext(6, RoundingMode.HALF_EVEN));
        BigDecimal spot8HitRateFor9Spots = BigDecimal.valueOf(rtpDataToPrint.getMatched_8_NumberCount_spot9())
                .multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(rtpDataToPrint.getSpot9SelectedCount()),  new MathContext(6, RoundingMode.HALF_EVEN));
        BigDecimal spot9HitRateFor9Spots = BigDecimal.valueOf(rtpDataToPrint.getMatched_9_NumberCount_spot9())
                .multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(rtpDataToPrint.getSpot9SelectedCount()),  new MathContext(6, RoundingMode.HALF_EVEN));



        BigDecimal spot10HitRate = BigDecimal.valueOf(rtpDataToPrint.getMatched10NumbersCount())
                .multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(rtpDataToPrint.getSpot10SelectedCount()),  new MathContext(4, RoundingMode.HALF_EVEN));


        BigDecimal spot0HitRateFor10Spots = BigDecimal.valueOf(rtpDataToPrint.getMatched_0_NumberCount_spot10())
                .multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(rtpDataToPrint.getSpot10SelectedCount()),  new MathContext(6, RoundingMode.HALF_EVEN));
        BigDecimal spot1HitRateFor10Spots = BigDecimal.valueOf(rtpDataToPrint.getMatched_1_NumberCount_spot10())
                .multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(rtpDataToPrint.getSpot10SelectedCount()),  new MathContext(6, RoundingMode.HALF_EVEN));
        BigDecimal spot2HitRateFor10Spots = BigDecimal.valueOf(rtpDataToPrint.getMatched_2_NumberCount_spot10())
                .multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(rtpDataToPrint.getSpot10SelectedCount()),  new MathContext(6, RoundingMode.HALF_EVEN));
        BigDecimal spot3HitRateFor10Spots = BigDecimal.valueOf(rtpDataToPrint.getMatched_3_NumberCount_spot10())
                .multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(rtpDataToPrint.getSpot10SelectedCount()),  new MathContext(6, RoundingMode.HALF_EVEN));
        BigDecimal spot4HitRateFor10Spots = BigDecimal.valueOf(rtpDataToPrint.getMatched_4_NumberCount_spot10())
                .multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(rtpDataToPrint.getSpot10SelectedCount()),  new MathContext(6, RoundingMode.HALF_EVEN));
        BigDecimal spot5HitRateFor10Spots = BigDecimal.valueOf(rtpDataToPrint.getMatched_5_NumberCount_spot10())
                .multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(rtpDataToPrint.getSpot10SelectedCount()),  new MathContext(6, RoundingMode.HALF_EVEN));
        BigDecimal spot6HitRateFor10Spots = BigDecimal.valueOf(rtpDataToPrint.getMatched_6_NumberCount_spot10())
                .multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(rtpDataToPrint.getSpot10SelectedCount()),  new MathContext(6, RoundingMode.HALF_EVEN));
        BigDecimal spot7HitRateFor10Spots = BigDecimal.valueOf(rtpDataToPrint.getMatched_7_NumberCount_spot10())
                .multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(rtpDataToPrint.getSpot10SelectedCount()),  new MathContext(6, RoundingMode.HALF_EVEN));
        BigDecimal spot8HitRateFor10Spots = BigDecimal.valueOf(rtpDataToPrint.getMatched_8_NumberCount_spot10())
                .multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(rtpDataToPrint.getSpot10SelectedCount()),  new MathContext(6, RoundingMode.HALF_EVEN));
        BigDecimal spot9HitRateFor10Spots = BigDecimal.valueOf(rtpDataToPrint.getMatched_9_NumberCount_spot10())
                .multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(rtpDataToPrint.getSpot10SelectedCount()),  new MathContext(6, RoundingMode.HALF_EVEN));
        BigDecimal spot10HitRateFor10Spots = BigDecimal.valueOf(rtpDataToPrint.getMatched_10_NumberCount_spot10())
                .multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(rtpDataToPrint.getSpot10SelectedCount()),  new MathContext(6, RoundingMode.HALF_EVEN));



        BigDecimal spot2RTPPercentage = totalStake.signum() == 0
                ? BigDecimal.ZERO
                : rtpDataToPrint.getTotalWinSpot2().multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(rtpDataToPrint.getTotalSpot2SelectedCount()),  new MathContext(4, RoundingMode.HALF_EVEN));

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

        // --- Variance and Standard Deviation per spot ---
        double[] spotVariance = new double[11]; // index 2..10
        double[] spotSD       = new double[11];
        long[]   spotCount    = {
            0, 0,
            rtpDataToPrint.getTotalSpot2SelectedCount(),
            rtpDataToPrint.getSpot3SelectedCount(),
            rtpDataToPrint.getSpot4SelectedCount(),
            rtpDataToPrint.getSpot5SelectedCount(),
            rtpDataToPrint.getSpot6SelectedCount(),
            rtpDataToPrint.getSpot7SelectedCount(),
            rtpDataToPrint.getSpot8SelectedCount(),
            rtpDataToPrint.getSpot9SelectedCount(),
            rtpDataToPrint.getSpot10SelectedCount()
        };
        BigDecimal[] spotTotalWin = {
            BigDecimal.ZERO, BigDecimal.ZERO,
            rtpDataToPrint.getTotalWinSpot2(),
            rtpDataToPrint.getTotalWinSpot3(),
            rtpDataToPrint.getTotalWinSpot4(),
            rtpDataToPrint.getTotalWinSpot5(),
            rtpDataToPrint.getTotalWinSpot6(),
            rtpDataToPrint.getTotalWinSpot7(),
            rtpDataToPrint.getTotalWinSpot8(),
            rtpDataToPrint.getTotalWinSpot9(),
            rtpDataToPrint.getTotalWinSpot10()
        };
        BigDecimal[] spotSumSq = {
            BigDecimal.ZERO, BigDecimal.ZERO,
            rtpDataToPrint.getSumOfSquaresSpot2(),
            rtpDataToPrint.getSumOfSquaresSpot3(),
            rtpDataToPrint.getSumOfSquaresSpot4(),
            rtpDataToPrint.getSumOfSquaresSpot5(),
            rtpDataToPrint.getSumOfSquaresSpot6(),
            rtpDataToPrint.getSumOfSquaresSpot7(),
            rtpDataToPrint.getSumOfSquaresSpot8(),
            rtpDataToPrint.getSumOfSquaresSpot9(),
            rtpDataToPrint.getSumOfSquaresSpot10()
        };
        for (int s = 2; s <= 10; s++) {
            if (spotCount[s] > 0) {
                double mean = spotTotalWin[s].doubleValue() / spotCount[s];
                double e2   = spotSumSq[s].doubleValue()   / spotCount[s];
                spotVariance[s] = e2 - mean * mean;
                spotSD[s]       = Math.sqrt(Math.max(0, spotVariance[s]));
            }
        }

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
            addRow(sheet, r++, "Total Rounds", BigDecimal.valueOf(totalRuns));
            addRow(sheet, r++, "Total Stake", totalStake);
            addRow(sheet, r++, "Total Wins", rtpDataToPrint.getTotalWins());
            addRow(sheet, r++, "RTP (%)", rtpPercent);
            addRow(sheet, r++, "Date created: ", getCurrentDateString());
            addRow(sheet, r++, "Hit rate", hitRate);
            addRow(sheet, r++, "Max Win Amount", rtpDataToPrint.getMaxWinAmount());

            sheet.createRow(r++);
            sheet.createRow(r++);
            sheet.createRow(r++);

            Row header2 = sheet.createRow(r++);
            createCell(header2, 0, "Spots", headerStyle);
            createCell(header2, 1, "Hit Rate (%)", headerStyle);
            createCell(header2, 2, "RTP (%)", headerStyle);
            createCell(header2, 3, "Variance", headerStyle);
            createCell(header2, 4, "Std Dev", headerStyle);
            //data rows for second header
            addSpotRow(sheet, r++, " Spot 2 ",  spot2HitRate,  spot2RTPPercentage,  spotVariance[2],  spotSD[2]);
            addSpotRow(sheet, r++, " Spot 3 ",  spot3HitRate,  spot3RTPPercentage,  spotVariance[3],  spotSD[3]);
            addSpotRow(sheet, r++, " Spot 4 ",  spot4HitRate,  spot4RTPPercentage,  spotVariance[4],  spotSD[4]);
            addSpotRow(sheet, r++, " Spot 5 ",  spot5HitRate,  spot5RTPPercentage,  spotVariance[5],  spotSD[5]);
            addSpotRow(sheet, r++, " Spot 6 ",  spot6HitRate,  spot6RTPPercentage,  spotVariance[6],  spotSD[6]);
            addSpotRow(sheet, r++, " Spot 7 ",  spot7HitRate,  spot7RTPPercentage,  spotVariance[7],  spotSD[7]);
            addSpotRow(sheet, r++, " Spot 8 ",  spot8HitRate,  spot8RTPPercentage,  spotVariance[8],  spotSD[8]);
            addSpotRow(sheet, r++, " Spot 9 ",  spot9HitRate,  spot9RTPPercentage,  spotVariance[9],  spotSD[9]);
            addSpotRow(sheet, r++, " Spot 10 ", spot10HitRate, spot10RTPPercentage, spotVariance[10], spotSD[10]);

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

            Row header5 = sheet.createRow(r++);
            createCell(header5, 0, "Spot3 Breakup", headerStyle);
            Row header6 = sheet.createRow(r++);
            createCell(header6, 0, "hits", headerStyle);
            createCell(header6, 1, "frequency (%)", headerStyle);

            //data rows for sixth header

            addRow(sheet, r++, " 0 ", spot0HitRateFor3Spots);
            addRow(sheet, r++, " 1 ", spot1HitRateFor3Spots);
            addRow(sheet, r++, " 2 ", spot2HitRateFor3Spots);
            addRow(sheet, r++, " 3 ", spot3HitRateFor3Spots);

            sheet.createRow(r++);

            Row header7 = sheet.createRow(r++);
            createCell(header7, 0, "Spot4 Breakup", headerStyle);
            Row header8 = sheet.createRow(r++);
            createCell(header8, 0, "hits", headerStyle);
            createCell(header8, 1, "frequency (%)", headerStyle);

            //data rows for eighth header
            addRow(sheet, r++, " 0 ", spot0HitRateFor4Spots);
            addRow(sheet, r++, " 1 ", spot1HitRateFor4Spots);
            addRow(sheet, r++, " 2 ", spot2HitRateFor4Spots);
            addRow(sheet, r++, " 3 ", spot3HitRateFor4Spots);
            addRow(sheet, r++, " 4 ", spot4HitRateFor4Spots);

            sheet.createRow(r++);
            Row header9 = sheet.createRow(r++);
            createCell(header9, 0, "Spot5 Breakup", headerStyle);
            Row header10 = sheet.createRow(r++);
            createCell(header10, 0, "hits", headerStyle);
            createCell(header10, 1, "frequency (%)", headerStyle);

            //data rows for tenth header
            addRow(sheet, r++, " 0 ", spot0HitRateFor5Spots);
            addRow(sheet, r++, " 1 ", spot1HitRateFor5Spots);
            addRow(sheet, r++, " 2 ", spot2HitRateFor5Spots);
            addRow(sheet, r++, " 3 ", spot3HitRateFor5Spots);
            addRow(sheet, r++, " 4 ", spot4HitRateFor5Spots);
            addRow(sheet, r++, " 5 ", spot5HitRateFor5Spots);

            sheet.createRow(r++);
            Row header11 = sheet.createRow(r++);
            createCell(header11, 0, "Spot6 Breakup", headerStyle);
            Row header12 = sheet.createRow(r++);
            createCell(header12, 0, "hits", headerStyle);
            createCell(header12, 1, "frequency (%)", headerStyle);

            //data rows for twelfth header

            addRow(sheet, r++, " 0 ", spot0HitRateFor6Spots);
            addRow(sheet, r++, " 1 ", spot1HitRateFor6Spots);
            addRow(sheet, r++, " 2 ", spot2HitRateFor6Spots);
            addRow(sheet, r++, " 3 ", spot3HitRateFor6Spots);
            addRow(sheet, r++, " 4 ", spot4HitRateFor6Spots);
            addRow(sheet, r++, " 5 ", spot5HitRateFor6Spots);
            addRow(sheet, r++, " 6 ", spot6HitRateFor6Spots);

            sheet.createRow(r++);
            Row header13 = sheet.createRow(r++);
            createCell(header13, 0, "Spot7 Breakup", headerStyle);
            Row header14 = sheet.createRow(r++);
            createCell(header14, 0, "hits", headerStyle);
            createCell(header14, 1, "frequency (%)", headerStyle);

            //data rows for fourteenth header

            addRow(sheet, r++, " 0 ", spot0HitRateFor7Spots);
            addRow(sheet, r++, " 1 ", spot1HitRateFor7Spots);
            addRow(sheet, r++, " 2 ", spot2HitRateFor7Spots);
            addRow(sheet, r++, " 3 ", spot3HitRateFor7Spots);
            addRow(sheet, r++, " 4 ", spot4HitRateFor7Spots);
            addRow(sheet, r++, " 5 ", spot5HitRateFor7Spots);
            addRow(sheet, r++, " 6 ", spot6HitRateFor7Spots);
            addRow(sheet, r++, " 7 ", spot7HitRateFor7Spots);

            sheet.createRow(r++);
            Row header15 = sheet.createRow(r++);
            createCell(header15, 0, "Spot8 Breakup", headerStyle);
            Row header16 = sheet.createRow(r++);
            createCell(header16, 0, "hits", headerStyle);
            createCell(header16, 1, "frequency (%)", headerStyle);

            //data rows for sixteenth header

            addRow(sheet, r++, " 0 ", spot0HitRateFor8Spots);
            addRow(sheet, r++, " 1 ", spot1HitRateFor8Spots);
            addRow(sheet, r++, " 2 ", spot2HitRateFor8Spots);
            addRow(sheet, r++, " 3 ", spot3HitRateFor8Spots);
            addRow(sheet, r++, " 4 ", spot4HitRateFor8Spots);
            addRow(sheet, r++, " 5 ", spot5HitRateFor8Spots);
            addRow(sheet, r++, " 6 ", spot6HitRateFor8Spots);
            addRow(sheet, r++, " 7 ", spot7HitRateFor8Spots);
            addRow(sheet, r++, " 8 ", spot8HitRateFor8Spots);

            sheet.createRow(r++);
            Row header17 = sheet.createRow(r++);
            createCell(header17, 0, "Spot9 Breakup", headerStyle);
            Row header18 = sheet.createRow(r++);
            createCell(header18, 0, "hits", headerStyle);
            createCell(header18, 1, "frequency (%)", headerStyle);

            //data rows for eighteenth header

            addRow(sheet, r++, " 0 ", spot0HitRateFor9Spots);
            addRow(sheet, r++, " 1 ", spot1HitRateFor9Spots);
            addRow(sheet, r++, " 2 ", spot2HitRateFor9Spots);
            addRow(sheet, r++, " 3 ", spot3HitRateFor9Spots);
            addRow(sheet, r++, " 4 ", spot4HitRateFor9Spots);
            addRow(sheet, r++, " 5 ", spot5HitRateFor9Spots);
            addRow(sheet, r++, " 6 ", spot6HitRateFor9Spots);
            addRow(sheet, r++, " 7 ", spot7HitRateFor9Spots);
            addRow(sheet, r++, " 8 ", spot8HitRateFor9Spots);
            addRow(sheet, r++, " 9 ", spot9HitRateFor9Spots);

            sheet.createRow(r++);
            Row header19 = sheet.createRow(r++);
            createCell(header19, 0, "Spot10 Breakup", headerStyle);
            Row header20 = sheet.createRow(r++);
            createCell(header20, 0, "hits", headerStyle);
            createCell(header20, 1, "frequency (%)", headerStyle);

            //data rows for twentieth header
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

            sheet.autoSizeColumn(0);
            sheet.autoSizeColumn(1);
            sheet.autoSizeColumn(2);
            sheet.autoSizeColumn(3);
            sheet.autoSizeColumn(4);

            try (FileOutputStream out = new FileOutputStream(filePath)) {
                workbook.write(out);
            }
        }
    }

    private static void addRow(Sheet sheet, int rowIndex, String label, BigDecimal value, BigDecimal rtp) {
        Row row = sheet.createRow(rowIndex);
        row.createCell(0).setCellValue(label);
        row.createCell(1).setCellValue(value.doubleValue());
        row.createCell(2).setCellValue(rtp.doubleValue());
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