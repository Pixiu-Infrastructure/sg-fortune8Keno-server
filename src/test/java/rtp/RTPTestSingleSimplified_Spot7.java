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
public class RTPTestSingleSimplified_Spot7 {

    private static int forceNumOfThreads = 0;
    private int availableThreads;

    private final BigDecimal rtp = RTP.VARIANT_92;
    static BigDecimal stakeValue = BigDecimal.ONE;

    private final long totalRuns = 1_00_00_00_00_0L;

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
            Random threadRandom = new Random(master.nextLong());
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

        rtpDataToPrint.setMatched7NumbersCount(rtpDataToPrint.getMatched7NumbersCount() + accumulator.getMatched7NumbersCount());
        rtpDataToPrint.setSpot7SelectedCount(rtpDataToPrint.getSpot7SelectedCount() + accumulator.getSpot7SelectedCount());
        rtpDataToPrint.setMatched_0_NumberCount_spot7(rtpDataToPrint.getMatched_0_NumberCount_spot7() + accumulator.getMatched0NumberCount_spot7());
        rtpDataToPrint.setMatched_1_NumberCount_spot7(rtpDataToPrint.getMatched_1_NumberCount_spot7() + accumulator.getMatched1NumberCount_spot7());
        rtpDataToPrint.setMatched_2_NumberCount_spot7(rtpDataToPrint.getMatched_2_NumberCount_spot7() + accumulator.getMatched2NumberCount_spot7());
        rtpDataToPrint.setMatched_3_NumberCount_spot7(rtpDataToPrint.getMatched_3_NumberCount_spot7() + accumulator.getMatched3NumberCount_spot7());
        rtpDataToPrint.setMatched_4_NumberCount_spot7(rtpDataToPrint.getMatched_4_NumberCount_spot7() + accumulator.getMatched4NumberCount_spot7());
        rtpDataToPrint.setMatched_5_NumberCount_spot7(rtpDataToPrint.getMatched_5_NumberCount_spot7() + accumulator.getMatched5NumberCount_spot7());
        rtpDataToPrint.setMatched_6_NumberCount_spot7(rtpDataToPrint.getMatched_6_NumberCount_spot7() + accumulator.getMatched6NumberCount_spot7());
        rtpDataToPrint.setMatched_7_NumberCount_spot7(rtpDataToPrint.getMatched_7_NumberCount_spot7() + accumulator.getMatched7NumberCount_spot7());
        rtpDataToPrint.setTotalWinSpot7(rtpDataToPrint.getTotalWinSpot7().add(accumulator.totalWinSpot7));
        rtpDataToPrint.setSumOfSquaresSpot7(rtpDataToPrint.getSumOfSquaresSpot7().add(accumulator.getSumOfSquaresSpot7()));

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
        RtpRunnerSimple_Spot7 rtpRunner = RtpRunnerSimple_Spot7.builder()
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
        String fileName = "RTP_Result_1B_Spot_7_" + rtp + ".xlsx";

        try {
            RtpExcelWriterSimplified_Spot7.write(fileName, totalRuns, totalStake, rtpDataToPrint);
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

        int matched_0_NumberCount_spot7 = 0;
        int matched_1_NumberCount_spot7 = 0;
        int matched_2_NumberCount_spot7 = 0;
        int matched_3_NumberCount_spot7 = 0;
        int matched_4_NumberCount_spot7 = 0;
        int matched_5_NumberCount_spot7 = 0;
        int matched_6_NumberCount_spot7 = 0;
        int matched_7_NumberCount_spot7 = 0;
        int spot7SelectedCount = 0;
        int matched7NumbersCount = 0;
        BigDecimal totalWinSpot7 = BigDecimal.ZERO;
        BigDecimal sumOfSquaresSpot7 = BigDecimal.ZERO;

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

            matched_0_NumberCount_spot7 += result.getMatched_0_NumberCount_spot7();
            matched_1_NumberCount_spot7 += result.getMatched_1_NumberCount_spot7();
            matched_2_NumberCount_spot7 += result.getMatched_2_NumberCount_spot7();
            matched_3_NumberCount_spot7 += result.getMatched_3_NumberCount_spot7();
            matched_4_NumberCount_spot7 += result.getMatched_4_NumberCount_spot7();
            matched_5_NumberCount_spot7 += result.getMatched_5_NumberCount_spot7();
            matched_6_NumberCount_spot7 += result.getMatched_6_NumberCount_spot7();
            matched_7_NumberCount_spot7 += result.getMatched_7_NumberCount_spot7();
            spot7SelectedCount += result.getSpot7SelectedCount();
            matched7NumbersCount += result.getMatched7SelectedCount();
            totalWinSpot7 = totalWinSpot7.add(result.getTotalWinSpot7());
            sumOfSquaresSpot7 = sumOfSquaresSpot7.add(result.getSumOfSquaresSpot7());

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

        synchronized int getMatched0NumberCount_spot7() { return matched_0_NumberCount_spot7; }
        synchronized int getMatched1NumberCount_spot7() { return matched_1_NumberCount_spot7; }
        synchronized int getMatched2NumberCount_spot7() { return matched_2_NumberCount_spot7; }
        synchronized int getMatched3NumberCount_spot7() { return matched_3_NumberCount_spot7; }
        synchronized int getMatched4NumberCount_spot7() { return matched_4_NumberCount_spot7; }
        synchronized int getMatched5NumberCount_spot7() { return matched_5_NumberCount_spot7; }
        synchronized int getMatched6NumberCount_spot7() { return matched_6_NumberCount_spot7; }
        synchronized int getMatched7NumberCount_spot7() { return matched_7_NumberCount_spot7; }
        synchronized int getSpot7SelectedCount() { return spot7SelectedCount; }
        synchronized int getMatched7NumbersCount() { return matched7NumbersCount; }
        synchronized BigDecimal getSumOfSquaresSpot7() { return sumOfSquaresSpot7; }
        synchronized int getMultiplierTriggeredCount() { return multiplierTriggeredCount; }
        synchronized int getAwardGreaterThanEqualTo10() { return awardGreaterThanEqualTo10; }
        synchronized int getAwardGreaterThanEqualTo20() { return awardGreaterThanEqualTo20; }
        synchronized int getAwardGreaterThanEqualTo50() { return awardGreaterThanEqualTo50; }
        synchronized int getAwardGreaterThanEqualTo100() { return awardGreaterThanEqualTo100; }
        synchronized int getAwardGreaterThanEqualTo1000() { return awardGreaterThanEqualTo1000; }
        synchronized Map<Long, Long> getWinAmountFrequency() { return winAmountFrequency; }
    }
}
