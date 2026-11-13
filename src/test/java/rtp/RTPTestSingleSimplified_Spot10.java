package rtp;

import com.pixiu.fortune8keno.fortune8keno.game.constants.RTP;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Slf4j
public class RTPTestSingleSimplified_Spot10 {

    private static int forceNumOfThreads = 0;
    private int availableThreads;

    private final BigDecimal rtp = RTP.VARIANT_92;
    static BigDecimal stakeValue = BigDecimal.ONE;

    private final long totalRuns = 100_0000_000L;

    private final long batchSize = 5_000_000L;

    RTPDataToPrint rtpDataToPrint = new RTPDataToPrint();

    static long startingTime;

    @Test
    void run() throws InterruptedException {
        HelperUtils.logJVMStatistics();

        if (forceNumOfThreads > 0) {
            availableThreads = forceNumOfThreads;
        } else {
            availableThreads = Runtime.getRuntime().availableProcessors();
        }

        long numBatches = (totalRuns + batchSize - 1) / batchSize;
        System.out.println("Running RTP: " + totalRuns + " total rounds, "
                + numBatches + " batches of " + batchSize + ", "
                + availableThreads + " threads per batch");

        startingTime = System.currentTimeMillis();

        long roundsRemaining = totalRuns;
        for (long batch = 1; batch <= numBatches; batch++) {
            long roundsThisBatch = Math.min(batchSize, roundsRemaining);
            runBatch(batch, numBatches, roundsThisBatch);
            roundsRemaining -= roundsThisBatch;
            System.gc();
        }

        printFinalResult();
    }

    private void runBatch(long batchNum, long totalBatches, long roundsThisBatch) throws InterruptedException {
        int threadsForBatch = (int) Math.min(availableThreads, roundsThisBatch);
        long eachRun = roundsThisBatch / threadsForBatch;
        long remainder = roundsThisBatch % threadsForBatch;

        ExecutorService executorService = Executors.newFixedThreadPool(threadsForBatch);
        BatchAccumulator accumulator = new BatchAccumulator(threadsForBatch);

        Random master = new Random();
        for (int i = 0; i < threadsForBatch; i++) {
            long runsForThisThread = eachRun + (i == threadsForBatch - 1 ? remainder : 0);
            Random threadRandom = new Random(master.nextLong()); // unique seed per thread
            executorService.submit(() -> runTask(runsForThisThread, threadRandom, accumulator));
        }

        executorService.shutdown();
        if (!executorService.awaitTermination(5, TimeUnit.MINUTES)) {
            executorService.shutdownNow();
            throw new RuntimeException("Batch " + batchNum + " did not finish within timeout");
        }

        rtpDataToPrint.setTotalWins(rtpDataToPrint.getTotalWins().add(accumulator.getWinAmount()));
        rtpDataToPrint.setTotalHitCount(rtpDataToPrint.getTotalHitCount() + accumulator.getHitCount());
        rtpDataToPrint.setMaxWinAmount(BigDecimal.valueOf(Math.max(accumulator.getMaxWinAmount().intValue(), rtpDataToPrint.getMaxWinAmount().intValue())));

        rtpDataToPrint.setMatched10NumbersCount(rtpDataToPrint.getMatched10NumbersCount() + accumulator.getMatched10NumbersCount());
        rtpDataToPrint.setSpot10SelectedCount(rtpDataToPrint.getSpot10SelectedCount() + accumulator.getSpot10SelectedCount());
        rtpDataToPrint.setMatched_0_NumberCount_spot10(rtpDataToPrint.getMatched_0_NumberCount_spot10() + accumulator.getMatched0NumberCount_spot10());
        rtpDataToPrint.setMatched_1_NumberCount_spot10(rtpDataToPrint.getMatched_1_NumberCount_spot10() + accumulator.getMatched1NumberCount_spot10());
        rtpDataToPrint.setMatched_2_NumberCount_spot10(rtpDataToPrint.getMatched_2_NumberCount_spot10() + accumulator.getMatched2NumberCount_spot10());
        rtpDataToPrint.setMatched_3_NumberCount_spot10(rtpDataToPrint.getMatched_3_NumberCount_spot10() + accumulator.getMatched3NumberCount_spot10());
        rtpDataToPrint.setMatched_4_NumberCount_spot10(rtpDataToPrint.getMatched_4_NumberCount_spot10() + accumulator.getMatched4NumberCount_spot10());
        rtpDataToPrint.setMatched_5_NumberCount_spot10(rtpDataToPrint.getMatched_5_NumberCount_spot10() + accumulator.getMatched5NumberCount_spot10());
        rtpDataToPrint.setMatched_6_NumberCount_spot10(rtpDataToPrint.getMatched_6_NumberCount_spot10() + accumulator.getMatched6NumberCount_spot10());
        rtpDataToPrint.setMatched_7_NumberCount_spot10(rtpDataToPrint.getMatched_7_NumberCount_spot10() + accumulator.getMatched7NumberCount_spot10());
        rtpDataToPrint.setMatched_8_NumberCount_spot10(rtpDataToPrint.getMatched_8_NumberCount_spot10() + accumulator.getMatched8NumberCount_spot10());
        rtpDataToPrint.setMatched_9_NumberCount_spot10(rtpDataToPrint.getMatched_9_NumberCount_spot10() + accumulator.getMatched9NumberCount_spot10());
        rtpDataToPrint.setMatched_10_NumberCount_spot10(rtpDataToPrint.getMatched_10_NumberCount_spot10() + accumulator.getMatched10NumberCount_spot10());
        rtpDataToPrint.setTotalWinSpot10(rtpDataToPrint.getTotalWinSpot10().add(accumulator.totalWinSpot10));
        rtpDataToPrint.setSumOfSquaresSpot10(rtpDataToPrint.getSumOfSquaresSpot10().add(accumulator.getSumOfSquaresSpot10()));

        rtpDataToPrint.setMultiplierTriggeredCount(rtpDataToPrint.getMultiplierTriggeredCount() + accumulator.getMultiplierTriggeredCount());
        rtpDataToPrint.setAwardGreaterThanEqualTo10(rtpDataToPrint.getAwardGreaterThanEqualTo10() + accumulator.getAwardGreaterThanEqualTo10());
        rtpDataToPrint.setAwardGreaterThanEqualTo20(rtpDataToPrint.getAwardGreaterThanEqualTo20() + accumulator.getAwardGreaterThanEqualTo20());
        rtpDataToPrint.setAwardGreaterThanEqualTo50(rtpDataToPrint.getAwardGreaterThanEqualTo50() + accumulator.getAwardGreaterThanEqualTo50());
        rtpDataToPrint.setAwardGreaterThanEqualTo100(rtpDataToPrint.getAwardGreaterThanEqualTo100() + accumulator.getAwardGreaterThanEqualTo100());
        rtpDataToPrint.setAwardGreaterThanEqualTo1000(rtpDataToPrint.getAwardGreaterThanEqualTo1000() + accumulator.getAwardGreaterThanEqualTo1000());
        accumulator.getWinAmountFrequency().forEach((k, v) ->
                rtpDataToPrint.getWinAmountFrequency().merge(k, v, Long::sum));

        System.out.println("Completed batch " + batchNum + "/" + totalBatches
                + " (" + roundsThisBatch + " rounds) — running total wins: " + rtpDataToPrint.getTotalWins());
    }

    private void runTask(long eachRun, Random random, BatchAccumulator accumulator) {
        RtpRunnerSimple_Spot10 rtpRunner = RtpRunnerSimple_Spot10.builder()
                .expectedRTP(this.rtp)
                .random(random)
                .totalRuns((int) eachRun)
                .build();
        try {
            RtpResult result = rtpRunner.call();
            accumulator.add(result);
        } catch (Exception e) {
            System.out.println("RTP run encountered an error: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private void printFinalResult() {
        BigDecimal totalStake = stakeValue.multiply(BigDecimal.valueOf(totalRuns));
        BigDecimal totalWin = rtpDataToPrint.getTotalWins();

        BigDecimal rtpPercentage = totalWin.multiply(BigDecimal.valueOf(100))
                .divide(totalStake, new MathContext(4, RoundingMode.HALF_EVEN));

        System.out.println("----------------------------------------");
        System.out.println("Total Win: " + totalWin);
        System.out.println("RTP: " + rtpPercentage + "% ");

        long endTime = System.currentTimeMillis();
        long seconds = (endTime - startingTime) / 1000;
        System.out.println("Time taken: " + seconds + " seconds");
        int rtp = this.rtp.multiply(BigDecimal.valueOf(100)).intValue();
        String fileName = "RTP_Result_1B_Spot_10_" + rtp + ".xlsx";

        try {
            RtpExcelWriterSimplified_Spot10.write(fileName, totalRuns, totalStake, rtpDataToPrint);
            System.out.println("Excel written: " + fileName);
        } catch (IOException e) {
            System.out.println("Failed to write Excel: " + e.getMessage());
        }
    }


    private static class BatchAccumulator {
        private final int expectedThreads;
        private int finishedThreads = 0;
        private BigDecimal winAmount = BigDecimal.ZERO;
        private int hitCount = 0;
        private BigDecimal maxWinAmount = BigDecimal.ZERO;

        int matched_0_NumberCount_spot10 = 0;
        int matched_1_NumberCount_spot10 = 0;
        int matched_2_NumberCount_spot10 = 0;
        int matched_3_NumberCount_spot10 = 0;
        int matched_4_NumberCount_spot10 = 0;
        int matched_5_NumberCount_spot10 = 0;
        int matched_6_NumberCount_spot10 = 0;
        int matched_7_NumberCount_spot10 = 0;
        int matched_8_NumberCount_spot10 = 0;
        int matched_9_NumberCount_spot10 = 0;
        int matched_10_NumberCount_spot10 = 0;
        int spot10SelectedCount = 0;
        int matched10NumbersCount = 0;
        BigDecimal totalWinSpot10 = BigDecimal.ZERO;
        BigDecimal sumOfSquaresSpot10 = BigDecimal.ZERO;

        int multiplierTriggeredCount = 0;
        int awardGreaterThanEqualTo10 = 0;
        int awardGreaterThanEqualTo20 = 0;
        int awardGreaterThanEqualTo50 = 0;
        int awardGreaterThanEqualTo100 = 0;
        int awardGreaterThanEqualTo1000 = 0;
        Map<Long, Long> winAmountFrequency = new HashMap<>();

        BatchAccumulator(int expectedThreads) {
            this.expectedThreads = expectedThreads;
        }

        synchronized void add(RtpResult result) {
            hitCount = hitCount + result.getHitCount();
            if (result.getMaxWinAmount().compareTo(maxWinAmount) > 0) {
                maxWinAmount = result.getMaxWinAmount();
            }
            winAmount = winAmount.add(result.getWinAmount());
            finishedThreads++;

            matched_0_NumberCount_spot10 += result.getMatched_0_NumberCount_spot10();
            matched_1_NumberCount_spot10 += result.getMatched_1_NumberCount_spot10();
            matched_2_NumberCount_spot10 += result.getMatched_2_NumberCount_spot10();
            matched_3_NumberCount_spot10 += result.getMatched_3_NumberCount_spot10();
            matched_4_NumberCount_spot10 += result.getMatched_4_NumberCount_spot10();
            matched_5_NumberCount_spot10 += result.getMatched_5_NumberCount_spot10();
            matched_6_NumberCount_spot10 += result.getMatched_6_NumberCount_spot10();
            matched_7_NumberCount_spot10 += result.getMatched_7_NumberCount_spot10();
            matched_8_NumberCount_spot10 += result.getMatched_8_NumberCount_spot10();
            matched_9_NumberCount_spot10 += result.getMatched_9_NumberCount_spot10();
            matched_10_NumberCount_spot10 += result.getMatched_10_NumberCount_spot10();
            spot10SelectedCount += result.getSpot10SelectedCount();
            matched10NumbersCount += result.getMatched10SelectedCount();
            totalWinSpot10 = totalWinSpot10.add(result.getTotalWinSpot10());
            sumOfSquaresSpot10 = sumOfSquaresSpot10.add(result.getSumOfSquaresSpot10());

            multiplierTriggeredCount += result.getMultiplierTriggeredCount();
            awardGreaterThanEqualTo10 += result.getAwardGreaterThanEqualTo10();
            awardGreaterThanEqualTo20 += result.getAwardGreaterThanEqualTo20();
            awardGreaterThanEqualTo50 += result.getAwardGreaterThanEqualTo50();
            awardGreaterThanEqualTo100 += result.getAwardGreaterThanEqualTo100();
            awardGreaterThanEqualTo1000 += result.getAwardGreaterThanEqualTo1000();
            result.getWinAmountFrequency().forEach((k, v) -> winAmountFrequency.merge(k, v, Long::sum));
        }

        synchronized BigDecimal getWinAmount() { return winAmount; }
        synchronized int getHitCount() { return hitCount; }
        synchronized BigDecimal getMaxWinAmount() { return maxWinAmount; }

        synchronized int getMatched0NumberCount_spot10() { return matched_0_NumberCount_spot10; }
        synchronized int getMatched1NumberCount_spot10() { return matched_1_NumberCount_spot10; }
        synchronized int getMatched2NumberCount_spot10() { return matched_2_NumberCount_spot10; }
        synchronized int getMatched3NumberCount_spot10() { return matched_3_NumberCount_spot10; }
        synchronized int getMatched4NumberCount_spot10() { return matched_4_NumberCount_spot10; }
        synchronized int getMatched5NumberCount_spot10() { return matched_5_NumberCount_spot10; }
        synchronized int getMatched6NumberCount_spot10() { return matched_6_NumberCount_spot10; }
        synchronized int getMatched7NumberCount_spot10() { return matched_7_NumberCount_spot10; }
        synchronized int getMatched8NumberCount_spot10() { return matched_8_NumberCount_spot10; }
        synchronized int getMatched9NumberCount_spot10() { return matched_9_NumberCount_spot10; }
        synchronized int getMatched10NumberCount_spot10() { return matched_10_NumberCount_spot10; }
        synchronized int getSpot10SelectedCount() { return spot10SelectedCount; }
        synchronized int getMatched10NumbersCount() { return matched10NumbersCount; }
        synchronized BigDecimal getSumOfSquaresSpot10() { return sumOfSquaresSpot10; }
        synchronized int getMultiplierTriggeredCount() { return multiplierTriggeredCount; }
        synchronized int getAwardGreaterThanEqualTo10() { return awardGreaterThanEqualTo10; }
        synchronized int getAwardGreaterThanEqualTo20() { return awardGreaterThanEqualTo20; }
        synchronized int getAwardGreaterThanEqualTo50() { return awardGreaterThanEqualTo50; }
        synchronized int getAwardGreaterThanEqualTo100() { return awardGreaterThanEqualTo100; }
        synchronized int getAwardGreaterThanEqualTo1000() { return awardGreaterThanEqualTo1000; }
        synchronized Map<Long, Long> getWinAmountFrequency() { return winAmountFrequency; }
    }
}
