package rtp;

import com.pixiu.fortune8keno.fortune8keno.game.constants.RTP;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Slf4j
public class RTPTestSingle {

    private static int forceNumOfThreads = 0; // set this greater than 0 will force to run range check in number of threads/times
    private int availableThreads;

    private final BigDecimal rtp = RTP.VARIANT_86;
    static BigDecimal stakeValue = BigDecimal.ONE;

    // Total rounds across the WHOLE test (all batches combined).
    private final long totalRuns = 10_0000_00L;



    // How many rounds to run per batch, across all threads combined.
    // Tune this down (e.g. 1_000_000L) if 5M still struggles on your machine,
    // or up if your machine handles it comfortably and you want fewer batches.
    private final long batchSize = 5_000_000L;

    // Running total across all batches — accumulated safely between batches
    // since batches run sequentially (one batch's threads fully finish before
    // the next batch starts), so no synchronization is needed here.

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

        long numBatches = (totalRuns + batchSize - 1) / batchSize; // ceiling division
        System.out.println("Running RTP: " + totalRuns + " total rounds, "
                + numBatches + " batches of " + batchSize + ", "
                + availableThreads + " threads per batch");

        startingTime = System.currentTimeMillis();

        long roundsRemaining = totalRuns;
        for (long batch = 1; batch <= numBatches; batch++) {
            long roundsThisBatch = Math.min(batchSize, roundsRemaining);
            runBatch(batch, numBatches, roundsThisBatch);
            roundsRemaining -= roundsThisBatch;

            // Give the JVM a chance to reclaim this batch's garbage before
            // the next batch starts generating more. This is a hint, not a
            // guarantee, but helps keep peak memory pressure down across batches.
            System.gc();
        }

        printFinalResult();
    }

    /**
     * Runs a single batch of `roundsThisBatch` rounds, split across
     * availableThreads threads, and blocks until the batch completes before
     * returning. Each thread gets its own Random instance to avoid the
     * contention/slowdown caused by sharing one Random across threads.
     */
    private void runBatch(long batchNum, long totalBatches, long roundsThisBatch) throws InterruptedException {
        int threadsForBatch = (int) Math.min(availableThreads, roundsThisBatch);
        long eachRun = roundsThisBatch / threadsForBatch;
        long remainder = roundsThisBatch % threadsForBatch; // give leftover rounds to the last thread

        ExecutorService executorService = Executors.newFixedThreadPool(threadsForBatch);
        BatchAccumulator accumulator = new BatchAccumulator(threadsForBatch);

        for (int i = 0; i < threadsForBatch; i++) {
            long runsForThisThread = eachRun + (i == threadsForBatch - 1 ? remainder : 0);
            // IMPORTANT: a fresh Random per thread — never share one Random
            // instance across threads, it causes heavy CAS contention and
            // effectively serializes the threads under high call volume.
            Random random = new Random();
            executorService.submit(() -> runTask(runsForThisThread, random, accumulator));
        }

        executorService.shutdown();
        if (!executorService.awaitTermination(5, TimeUnit.MINUTES)) {
            executorService.shutdownNow();
            throw new RuntimeException("Batch " + batchNum + " did not finish within timeout");
        }

        rtpDataToPrint.setTotalWins(rtpDataToPrint.getTotalWins().add(accumulator.getWinAmount()));
        rtpDataToPrint.setTotalHitCount(rtpDataToPrint.getTotalHitCount() + accumulator.getHitCount());
        rtpDataToPrint.setMaxWinAmount(BigDecimal.valueOf(Math.max(accumulator.getMaxWinAmount().intValue(), rtpDataToPrint.getMaxWinAmount().intValue())));
        rtpDataToPrint.setTotalSpot2Hits(rtpDataToPrint.getTotalSpot2Hits() + accumulator.getMatchedNumbersCount());
        rtpDataToPrint.setTotalSpot2SelectedCount(rtpDataToPrint.getTotalSpot2SelectedCount() + accumulator.getNumberOfSpots());

        rtpDataToPrint.setMatched0NumberCount_spot2(rtpDataToPrint.getMatched0NumberCount_spot2() + accumulator.getMatched0NumberCount_spot2());
        rtpDataToPrint.setMatched1NumberCount_spot2(rtpDataToPrint.getMatched1NumberCount_spot2() + accumulator.getMatched1NumberCount_spot2());

        rtpDataToPrint.setMatched3NumbersCount(rtpDataToPrint.getMatched3NumbersCount() + accumulator.getMatched3NumbersCount());
        rtpDataToPrint.setSpot3SelectedCount(rtpDataToPrint.getSpot3SelectedCount() + accumulator.getSpot3SelectedCount());
        rtpDataToPrint.setMatched_0_NumberCount_spot3(rtpDataToPrint.getMatched_0_NumberCount_spot3() + accumulator.getMatched0NumberCount_spot3());
        rtpDataToPrint.setMatched_1_NumberCount_spot3(rtpDataToPrint.getMatched_1_NumberCount_spot3() + accumulator.getMatched1NumberCount_spot3());
        rtpDataToPrint.setMatched_2_NumberCount_spot3(rtpDataToPrint.getMatched_2_NumberCount_spot3() + accumulator.getMatched2NumberCount_spot3());
        rtpDataToPrint.setMatched_3_NumberCount_spot3(rtpDataToPrint.getMatched_3_NumberCount_spot3() + accumulator.getMatched3NumberCount_spot3());

        rtpDataToPrint.setMatched4NumbersCount(rtpDataToPrint.getMatched4NumbersCount() + accumulator.getMatched4NumbersCount());
        rtpDataToPrint.setSpot4SelectedCount(rtpDataToPrint.getSpot4SelectedCount() + accumulator.getSpot4SelectedCount());
        rtpDataToPrint.setMatched_0_NumberCount_spot4(rtpDataToPrint.getMatched_0_NumberCount_spot4() + accumulator.getMatched0NumberCount_spot4());
        rtpDataToPrint.setMatched_1_NumberCount_spot4(rtpDataToPrint.getMatched_1_NumberCount_spot4() + accumulator.getMatched1NumberCount_spot4());
        rtpDataToPrint.setMatched_2_NumberCount_spot4(rtpDataToPrint.getMatched_2_NumberCount_spot4() + accumulator.getMatched2NumberCount_spot4());
        rtpDataToPrint.setMatched_3_NumberCount_spot4(rtpDataToPrint.getMatched_3_NumberCount_spot4() + accumulator.getMatched3NumberCount_spot4());
        rtpDataToPrint.setMatched_4_NumberCount_spot4(rtpDataToPrint.getMatched_4_NumberCount_spot4() + accumulator.getMatched4NumberCount_spot4());

        rtpDataToPrint.setMatched5NumbersCount(rtpDataToPrint.getMatched5NumbersCount() + accumulator.getMatched5NumbersCount());
        rtpDataToPrint.setSpot5SelectedCount(rtpDataToPrint.getSpot5SelectedCount() + accumulator.getSpot5SelectedCount());
        rtpDataToPrint.setMatched_0_NumberCount_spot5(rtpDataToPrint.getMatched_0_NumberCount_spot5() + accumulator.getMatched0NumberCount_spot5());
        rtpDataToPrint.setMatched_1_NumberCount_spot5(rtpDataToPrint.getMatched_1_NumberCount_spot5() + accumulator.getMatched1NumberCount_spot5());
        rtpDataToPrint.setMatched_2_NumberCount_spot5(rtpDataToPrint.getMatched_2_NumberCount_spot5() + accumulator.getMatched2NumberCount_spot5());
        rtpDataToPrint.setMatched_3_NumberCount_spot5(rtpDataToPrint.getMatched_3_NumberCount_spot5() + accumulator.getMatched3NumberCount_spot5());
        rtpDataToPrint.setMatched_4_NumberCount_spot5(rtpDataToPrint.getMatched_4_NumberCount_spot5() + accumulator.getMatched4NumberCount_spot5());
        rtpDataToPrint.setMatched_5_NumberCount_spot5(rtpDataToPrint.getMatched_5_NumberCount_spot5() + accumulator.getMatched5NumberCount_spot5());

        rtpDataToPrint.setMatched6NumbersCount(rtpDataToPrint.getMatched6NumbersCount() + accumulator.getMatched6NumbersCount());
        rtpDataToPrint.setSpot6SelectedCount(rtpDataToPrint.getSpot6SelectedCount() + accumulator.getSpot6SelectedCount());
        rtpDataToPrint.setMatched_0_NumberCount_spot6(rtpDataToPrint.getMatched_0_NumberCount_spot6() + accumulator.getMatched0NumberCount_spot6());
        rtpDataToPrint.setMatched_1_NumberCount_spot6(rtpDataToPrint.getMatched_1_NumberCount_spot6() + accumulator.getMatched1NumberCount_spot6());
        rtpDataToPrint.setMatched_2_NumberCount_spot6(rtpDataToPrint.getMatched_2_NumberCount_spot6() + accumulator.getMatched2NumberCount_spot6());
        rtpDataToPrint.setMatched_3_NumberCount_spot6(rtpDataToPrint.getMatched_3_NumberCount_spot6() + accumulator.getMatched3NumberCount_spot6());
        rtpDataToPrint.setMatched_4_NumberCount_spot6(rtpDataToPrint.getMatched_4_NumberCount_spot6() + accumulator.getMatched4NumberCount_spot6());
        rtpDataToPrint.setMatched_5_NumberCount_spot6(rtpDataToPrint.getMatched_5_NumberCount_spot6() + accumulator.getMatched5NumberCount_spot6());
        rtpDataToPrint.setMatched_6_NumberCount_spot6(rtpDataToPrint.getMatched_6_NumberCount_spot6() + accumulator.getMatched6NumberCount_spot6());

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

        rtpDataToPrint.setMatched8NumbersCount(rtpDataToPrint.getMatched8NumbersCount() + accumulator.getMatched8NumbersCount());
        rtpDataToPrint.setSpot8SelectedCount(rtpDataToPrint.getSpot8SelectedCount() + accumulator.getSpot8SelectedCount());
        rtpDataToPrint.setMatched_0_NumberCount_spot8(rtpDataToPrint.getMatched_0_NumberCount_spot8() + accumulator.getMatched0NumberCount_spot8());
        rtpDataToPrint.setMatched_1_NumberCount_spot8(rtpDataToPrint.getMatched_1_NumberCount_spot8() + accumulator.getMatched1NumberCount_spot8());
        rtpDataToPrint.setMatched_2_NumberCount_spot8(rtpDataToPrint.getMatched_2_NumberCount_spot8() + accumulator.getMatched2NumberCount_spot8());
        rtpDataToPrint.setMatched_3_NumberCount_spot8(rtpDataToPrint.getMatched_3_NumberCount_spot8() + accumulator.getMatched3NumberCount_spot8());
        rtpDataToPrint.setMatched_4_NumberCount_spot8(rtpDataToPrint.getMatched_4_NumberCount_spot8() + accumulator.getMatched4NumberCount_spot8());
        rtpDataToPrint.setMatched_5_NumberCount_spot8(rtpDataToPrint.getMatched_5_NumberCount_spot8() + accumulator.getMatched5NumberCount_spot8());
        rtpDataToPrint.setMatched_6_NumberCount_spot8(rtpDataToPrint.getMatched_6_NumberCount_spot8() + accumulator.getMatched6NumberCount_spot8());
        rtpDataToPrint.setMatched_7_NumberCount_spot8(rtpDataToPrint.getMatched_7_NumberCount_spot8() + accumulator.getMatched7NumberCount_spot8());
        rtpDataToPrint.setMatched_8_NumberCount_spot8(rtpDataToPrint.getMatched_8_NumberCount_spot8() + accumulator.getMatched8NumberCount_spot8());

        rtpDataToPrint.setMatched9NumbersCount(rtpDataToPrint.getMatched9NumbersCount() + accumulator.getMatched9NumbersCount());
        rtpDataToPrint.setSpot9SelectedCount(rtpDataToPrint.getSpot9SelectedCount() + accumulator.getSpot9SelectedCount());
        rtpDataToPrint.setMatched10NumbersCount(rtpDataToPrint.getMatched10NumbersCount() + accumulator.getMatched10NumbersCount());
        rtpDataToPrint.setSpot10SelectedCount(rtpDataToPrint.getSpot10SelectedCount() + accumulator.getSpot10SelectedCount());

        rtpDataToPrint.setTotalWinSpot2(rtpDataToPrint.getTotalWinSpot2().add(accumulator.totalWinSpot2));
        rtpDataToPrint.setTotalWinSpot3(rtpDataToPrint.getTotalWinSpot3().add(accumulator.totalWinSpot3));
        rtpDataToPrint.setTotalWinSpot4(rtpDataToPrint.getTotalWinSpot4().add(accumulator.totalWinSpot4));
        rtpDataToPrint.setTotalWinSpot5(rtpDataToPrint.getTotalWinSpot5().add(accumulator.totalWinSpot5));
        rtpDataToPrint.setTotalWinSpot6(rtpDataToPrint.getTotalWinSpot6().add(accumulator.totalWinSpot6));
        rtpDataToPrint.setTotalWinSpot7(rtpDataToPrint.getTotalWinSpot7().add(accumulator.totalWinSpot7));
        rtpDataToPrint.setTotalWinSpot8(rtpDataToPrint.getTotalWinSpot8().add(accumulator.totalWinSpot8));
        rtpDataToPrint.setTotalWinSpot9(rtpDataToPrint.getTotalWinSpot9().add(accumulator.totalWinSpot9));
        rtpDataToPrint.setTotalWinSpot10(rtpDataToPrint.getTotalWinSpot10().add(accumulator.totalWinSpot10));

        System.out.println("Completed batch " + batchNum + "/" + totalBatches
                + " (" + roundsThisBatch + " rounds) — running total wins: " + rtpDataToPrint.getTotalWins());
    }

    private void runTask(long eachRun, Random random, BatchAccumulator accumulator) {
        RtpRunner rtpRunner = RtpRunner.builder()
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
                .divide(totalStake,  new MathContext(4, RoundingMode.HALF_EVEN));
//        BigDecimal rtpPercentage =  totalWin.divide(totalStake, 2, BigDecimal.ROUND_HALF_UP).multiply(BigDecimal.valueOf(100));
        BigDecimal spot2RTPPercentage = rtpDataToPrint.getTotalWinSpot2().divide(BigDecimal.valueOf(rtpDataToPrint.getTotalSpot2SelectedCount()), 2, BigDecimal.ROUND_HALF_UP).multiply(BigDecimal.valueOf(100));
        BigDecimal spot3RTPPercentage = rtpDataToPrint.getTotalWinSpot3().divide(BigDecimal.valueOf(rtpDataToPrint.getSpot3SelectedCount()), 2, BigDecimal.ROUND_HALF_UP).multiply(BigDecimal.valueOf(100));
        BigDecimal spot4RTPPercentage = rtpDataToPrint.getTotalWinSpot4().divide(BigDecimal.valueOf(rtpDataToPrint.getSpot4SelectedCount()), 2, BigDecimal.ROUND_HALF_UP).multiply(BigDecimal.valueOf(100));
        BigDecimal spot5RTPPercentage = rtpDataToPrint.getTotalWinSpot5().divide(BigDecimal.valueOf(rtpDataToPrint.getSpot5SelectedCount()), 2, BigDecimal.ROUND_HALF_UP).multiply(BigDecimal.valueOf(100));
        BigDecimal spot6RTPPercentage = rtpDataToPrint.getTotalWinSpot6().divide(BigDecimal.valueOf(rtpDataToPrint.getSpot6SelectedCount()), 2, BigDecimal.ROUND_HALF_UP).multiply(BigDecimal.valueOf(100));
        double spot7RTPPercentage = rtpDataToPrint.getTotalWinSpot7().doubleValue() / rtpDataToPrint.getSpot7SelectedCount() * 100;
        double spot8RTPPercentage = rtpDataToPrint.getTotalWinSpot8().doubleValue() / rtpDataToPrint.getSpot8SelectedCount() * 100;
        double spot9RTPPercentage = rtpDataToPrint.getTotalWinSpot9().doubleValue() / rtpDataToPrint.getSpot9SelectedCount() * 100;
        double spot10RTPPercentage = rtpDataToPrint.getTotalWinSpot10().doubleValue() / rtpDataToPrint.getSpot10SelectedCount() * 100;

        System.out.println("----------------------------------------");
        System.out.println("Total Stake: " + totalStake);
        System.out.println("Total Win: " + totalWin);
        System.out.println("RTP: " + rtpPercentage + "% ");
        System.out.println("Hit rate: " + ((double) rtpDataToPrint.getTotalHitCount() / totalRuns * 100) + "%");
        System.out.println("Matched 2 Numbers Count: " + rtpDataToPrint.getTotalSpot2Hits());
        System.out.println("Total Spot Selected Count: " + rtpDataToPrint.getTotalSpot2SelectedCount());
        System.out.println("Spot 2 Hit Rate: " + ((double) rtpDataToPrint.getTotalSpot2Hits() / rtpDataToPrint.getTotalSpot2SelectedCount() * 100) + "%");
        System.out.println("Max Win Amount: " + rtpDataToPrint.getMaxWinAmount());

        System.out.println("spot2RTPPercentage: " + spot2RTPPercentage + "%");
        System.out.println("spot3RTPPercentage: " + spot3RTPPercentage + "%");
        System.out.println("spot4RTPPercentage: " + spot4RTPPercentage + "%");
        System.out.println("spot5RTPPercentage: " + spot5RTPPercentage + "%");
        System.out.println("spot6RTPPercentage: " + spot6RTPPercentage + "%");
        System.out.println("spot7RTPPercentage: " + spot7RTPPercentage + "%");
        System.out.println("spot8RTPPercentage: " + spot8RTPPercentage + "%");
        System.out.println("spot9RTPPercentage: " + spot9RTPPercentage + "%");
        System.out.println("spot10RTPPercentage: " + spot10RTPPercentage + "%");


        System.out.println("Matched 3 Numbers Count: " + rtpDataToPrint.getMatched3NumbersCount());
        System.out.println("Spot 3 Selected Count: " + rtpDataToPrint.getSpot3SelectedCount());
        System.out.println("Spot 3 Hit Rate: " + ((double) rtpDataToPrint.getMatched3NumbersCount() / rtpDataToPrint.getSpot3SelectedCount() * 100) + "%");
        System.out.println("Matched 4 Numbers Count: " + rtpDataToPrint.getMatched4NumbersCount());
        System.out.println("Spot 4 Selected Count: " + rtpDataToPrint.getSpot4SelectedCount());
        System.out.println("Spot 4 Hit Rate: " + ((double) rtpDataToPrint.getMatched4NumbersCount() / rtpDataToPrint.getSpot4SelectedCount() * 100) + "%");
        System.out.println("Matched 5 Numbers Count: " + rtpDataToPrint.getMatched5NumbersCount());
        System.out.println("Spot 5 Selected Count: " + rtpDataToPrint.getSpot5SelectedCount());
        System.out.println("Spot 5 Hit Rate: " + ((double) rtpDataToPrint.getMatched5NumbersCount() / rtpDataToPrint.getSpot5SelectedCount() * 100) + "%");

        System.out.println("Matched 6 Numbers Count: " + rtpDataToPrint.getMatched6NumbersCount());
        System.out.println("Spot 6 Selected Count: " + rtpDataToPrint.getSpot6SelectedCount());
        System.out.println("Spot 6 Hit Rate: " + ((double) rtpDataToPrint.getMatched6NumbersCount() / rtpDataToPrint.getSpot6SelectedCount() * 100) + "%");
        System.out.println("Matched 7 Numbers   Count: " + rtpDataToPrint.getMatched7NumbersCount());
        System.out.println("Spot 7 Selected Count: " + rtpDataToPrint.getSpot7SelectedCount());
        System.out.println("Spot 7 Hit Rate: " + ((double) rtpDataToPrint.getMatched7NumbersCount() / rtpDataToPrint.getSpot7SelectedCount() * 100) + "%");
        System.out.println("Matched 8 Numbers Count: " + rtpDataToPrint.getMatched8NumbersCount());
        System.out.println("Spot 8 Selected Count: " + rtpDataToPrint.getSpot8SelectedCount());
        System.out.println("Spot 8 Hit Rate: " + ((double) rtpDataToPrint.getMatched8NumbersCount() / rtpDataToPrint.getSpot8SelectedCount() * 100) + "%");
        System.out.println("Matched 9 Numbers Count: " + rtpDataToPrint.getMatched9NumbersCount());
        System.out.println("Spot 9 Selected Count: " + rtpDataToPrint.getSpot9SelectedCount());
        System.out.println("Spot 9 Hit Rate: " + ((double) rtpDataToPrint.getMatched9NumbersCount() / rtpDataToPrint.getSpot9SelectedCount() * 100) + "% ");
        System.out.println("Matched 10 Numbers Count: " + rtpDataToPrint.getMatched10NumbersCount());
        System.out.println("Spot 10 Selected Count: " + rtpDataToPrint.getSpot10SelectedCount());
        System.out.println("Spot 10 Hit Rate: " + ((double) rtpDataToPrint.getMatched10NumbersCount() / rtpDataToPrint.getSpot10SelectedCount() * 100) + "% ");

        long endTime = System.currentTimeMillis();
        long seconds = (endTime - startingTime) / 1000;
        System.out.println("Time taken: " + seconds + " seconds");

        try {
            RtpExcelWriter.write("RTP_Result_500M.xlsx", totalRuns, totalStake,
                   rtpDataToPrint);
            System.out.println("Excel written: RTP_Result_500M.xlsx");
        } catch (IOException e) {
            System.out.println("Failed to write Excel: " + e.getMessage());
        }
    }

    /**
     * Thread-safe accumulator for a single batch's results. Replaces the
     * original's `synchronized addToRtpResult` approach, scoped per-batch
     * instead of per-whole-run.
     */
    private static class BatchAccumulator {
        private final int expectedThreads;
        private int finishedThreads = 0;
        private BigDecimal winAmount = BigDecimal.ZERO;
        private int hitCount = 0;
        private BigDecimal maxWinAmount = BigDecimal.ZERO;
        private int matchedNumbersCount = 0;
        private int numberOfSpots = 0;

        private int matched0NumberCount_spot2 = 0;
        private int matched1NumberCount_spot2 = 0;

        int matched0NumberCount_spot3 = 0;
        int matched1NumberCount_spot3 = 0;
        int matched2NumberCount_spot3 = 0;
        int matched3NumbersCount_spot3 = 0;
        int spot3SelectedCount = 0;
        int matched3NumbersCount = 0;

        int spot4SelectedCount = 0;
        int matched4NumbersCount = 0;
        int matched0NumberCount_spot4 = 0;
        int matched1NumberCount_spot4 = 0;
        int matched2NumberCount_spot4 = 0;
        int matched3NumberCount_spot4 = 0;
        int matched4NumberCount_spot4 = 0;

        int spot5SelectedCount = 0;
        int matched5NumbersCount = 0;
         int matched_0_NumberCount_spot5 = 0;
         int matched_1_NumberCount_spot5 = 0;
         int matched_2_NumberCount_spot5 = 0;
         int matched_3_NumberCount_spot5 = 0;
         int matched_4_NumberCount_spot5 = 0;
         int matched_5_NumberCount_spot5 = 0;

         int matched_0_NumberCount_spot6 = 0;
         int matched_1_NumberCount_spot6 = 0;
         int matched_2_NumberCount_spot6 = 0;
         int matched_3_NumberCount_spot6 = 0;
         int matched_4_NumberCount_spot6 = 0;
         int matched_5_NumberCount_spot6 = 0;
         int matched_6_NumberCount_spot6 = 0;
        int spot6SelectedCount = 0;
        int matched6NumbersCount = 0;

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

        int matched_0_NumberCount_spot8 = 0;
        int matched_1_NumberCount_spot8 = 0;
        int matched_2_NumberCount_spot8 = 0;
        int matched_3_NumberCount_spot8 = 0;
        int matched_4_NumberCount_spot8 = 0;
        int matched_5_NumberCount_spot8 = 0;
        int matched_6_NumberCount_spot8 = 0;
        int matched_7_NumberCount_spot8 = 0;
        int matched_8_NumberCount_spot8 = 0;
        int spot8SelectedCount = 0;
        int matched8NumbersCount = 0;

        int matched_0_NumberCount_spot9 = 0;
        int matched_1_NumberCount_spot9 = 0;
        int matched_2_NumberCount_spot9 = 0;
        int matched_3_NumberCount_spot9 = 0;
        int matched_4_NumberCount_spot9 = 0;
        int matched_5_NumberCount_spot9 = 0;
        int matched_6_NumberCount_spot9 = 0;
        int matched_7_NumberCount_spot9 = 0;
        int matched_8_NumberCount_spot9 = 0;
        int matched_9_NumberCount_spot9 = 0;
        int spot9SelectedCount = 0;
        int matched9NumbersCount = 0;

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

        BigDecimal totalWinSpot2 =  BigDecimal.ZERO;
        BigDecimal totalWinSpot3 = BigDecimal.ZERO;
        BigDecimal totalWinSpot4 = BigDecimal.ZERO;
        BigDecimal totalWinSpot5 = BigDecimal.ZERO;
        BigDecimal totalWinSpot6 = BigDecimal.ZERO;
        BigDecimal totalWinSpot7 = BigDecimal.ZERO;
        BigDecimal totalWinSpot8 = BigDecimal.ZERO;
        BigDecimal totalWinSpot9 = BigDecimal.ZERO;
        BigDecimal totalWinSpot10 = BigDecimal.ZERO;

        BatchAccumulator(int expectedThreads) {
            this.expectedThreads = expectedThreads;
        }

        synchronized void add(RtpResult result) {
            winAmount = winAmount.add(result.getWinAmount());
            hitCount = hitCount + result.getHitCount();
            if(result.getMaxWinAmount().compareTo(maxWinAmount) > 0){
                maxWinAmount = result.getMaxWinAmount();
            }
            finishedThreads++;
            matched0NumberCount_spot2 = matched0NumberCount_spot2 + result.getMatched0NumberCount_spot2();
            matched1NumberCount_spot2 = matched1NumberCount_spot2 + result.getMatched1NumberCount_spot2();
            matchedNumbersCount = matchedNumbersCount + result.getMatchedNumbersCount();
            numberOfSpots = numberOfSpots + result.getNumberOfSpots();

            spot3SelectedCount = spot3SelectedCount + result.getSpot3SelectedCount();
            matched3NumbersCount = matched3NumbersCount + result.getMatched3NumbersCount();
            matched0NumberCount_spot3 = matched0NumberCount_spot3 + result.getMatched_0_NumberCount_spot3();
            matched1NumberCount_spot3 = matched1NumberCount_spot3 + result.getMatched_1_NumberCount_spot3();
            matched2NumberCount_spot3 = matched2NumberCount_spot3 + result.getMatched_2_NumberCount_spot3();
            matched3NumbersCount_spot3 = matched3NumbersCount_spot3 + result.getMatched_3_NumberCount_spot3();

            spot4SelectedCount = spot4SelectedCount + result.getSpot4SelectedCount();
            matched4NumbersCount = matched4NumbersCount + result.getMatched4NumbersCount();
            matched0NumberCount_spot4 = matched0NumberCount_spot4 + result.getMatched_0_NumberCount_spot4();
            matched1NumberCount_spot4 = matched1NumberCount_spot4 + result.getMatched_1_NumberCount_spot4();
            matched2NumberCount_spot4 = matched2NumberCount_spot4 + result.getMatched_2_NumberCount_spot4();
            matched3NumberCount_spot4 = matched3NumberCount_spot4 + result.getMatched_3_NumberCount_spot4();
            matched4NumberCount_spot4 = matched4NumberCount_spot4 + result.getMatched_4_NumberCount_spot4();

            spot5SelectedCount = spot5SelectedCount + result.getSpot5SelectedCount();
            matched5NumbersCount = matched5NumbersCount + result.getMatched5NumbersCount();
            matched_0_NumberCount_spot5 = matched_0_NumberCount_spot5 + result.getMatched_0_NumberCount_spot5();
            matched_1_NumberCount_spot5 = matched_1_NumberCount_spot5 + result.getMatched_1_NumberCount_spot5();
            matched_2_NumberCount_spot5 = matched_2_NumberCount_spot5 + result.getMatched_2_NumberCount_spot5();
            matched_3_NumberCount_spot5 = matched_3_NumberCount_spot5 + result.getMatched_3_NumberCount_spot5();
            matched_4_NumberCount_spot5 = matched_4_NumberCount_spot5 + result.getMatched_4_NumberCount_spot5();
            matched_5_NumberCount_spot5 = matched_5_NumberCount_spot5 + result.getMatched_5_NumberCount_spot5();



            matched_0_NumberCount_spot6 = matched_0_NumberCount_spot6 + result.getMatched_0_NumberCount_spot6();
            matched_1_NumberCount_spot6 = matched_1_NumberCount_spot6 + result.getMatched_1_NumberCount_spot6();
            matched_2_NumberCount_spot6 = matched_2_NumberCount_spot6 + result.getMatched_2_NumberCount_spot6();
            matched_3_NumberCount_spot6 = matched_3_NumberCount_spot6 + result.getMatched_3_NumberCount_spot6();
            matched_4_NumberCount_spot6 = matched_4_NumberCount_spot6 + result.getMatched_4_NumberCount_spot6();
            matched_5_NumberCount_spot6 = matched_5_NumberCount_spot6 + result.getMatched_5_NumberCount_spot6();
            matched_6_NumberCount_spot6 = matched_6_NumberCount_spot6 + result.getMatched_6_NumberCount_spot6();
            spot6SelectedCount = spot6SelectedCount + result.getSpot6SelectedCount();
            matched6NumbersCount = matched6NumbersCount + result.getMatched6NumbersCount();

            matched_0_NumberCount_spot7 = matched_0_NumberCount_spot7 + result.getMatched_0_NumberCount_spot7();
            matched_1_NumberCount_spot7 = matched_1_NumberCount_spot7 + result.getMatched_1_NumberCount_spot7();
            matched_2_NumberCount_spot7 = matched_2_NumberCount_spot7 + result.getMatched_2_NumberCount_spot7();
            matched_3_NumberCount_spot7 = matched_3_NumberCount_spot7 + result.getMatched_3_NumberCount_spot7();
            matched_4_NumberCount_spot7 = matched_4_NumberCount_spot7 + result.getMatched_4_NumberCount_spot7();
            matched_5_NumberCount_spot7 = matched_5_NumberCount_spot7 + result.getMatched_5_NumberCount_spot7();
            matched_6_NumberCount_spot7 = matched_6_NumberCount_spot7 + result.getMatched_6_NumberCount_spot7();
            matched_7_NumberCount_spot7 = matched_7_NumberCount_spot7 + result.getMatched_7_NumberCount_spot7();
            spot7SelectedCount = spot7SelectedCount + result.getSpot7SelectedCount();
            matched7NumbersCount = matched7NumbersCount + result.getMatched7SelectedCount();

            matched_0_NumberCount_spot8 = matched_0_NumberCount_spot8 + result.getMatched_0_NumberCount_spot8();
            matched_1_NumberCount_spot8 = matched_1_NumberCount_spot8 + result.getMatched_1_NumberCount_spot8();
            matched_2_NumberCount_spot8 = matched_2_NumberCount_spot8 + result.getMatched_2_NumberCount_spot8();
            matched_3_NumberCount_spot8 = matched_3_NumberCount_spot8 + result.getMatched_3_NumberCount_spot8();
            matched_4_NumberCount_spot8 = matched_4_NumberCount_spot8 + result.getMatched_4_NumberCount_spot8();
            matched_5_NumberCount_spot8 = matched_5_NumberCount_spot8 + result.getMatched_5_NumberCount_spot8();
            matched_6_NumberCount_spot8 = matched_6_NumberCount_spot8 + result.getMatched_6_NumberCount_spot8();
            matched_7_NumberCount_spot8 = matched_7_NumberCount_spot8 + result.getMatched_7_NumberCount_spot8();
            matched_8_NumberCount_spot8 = matched_8_NumberCount_spot8 + result.getMatched_8_NumberCount_spot8();
            spot8SelectedCount = spot8SelectedCount + result.getSpot8SelectedCount();
            matched8NumbersCount = matched8NumbersCount + result.getMatched8SelectedCount();

            matched_0_NumberCount_spot9 = matched_0_NumberCount_spot9 + result.getMatched_0_NumberCount_spot9();
            matched_1_NumberCount_spot9 = matched_1_NumberCount_spot9 + result.getMatched_1_NumberCount_spot9();
            matched_2_NumberCount_spot9 = matched_2_NumberCount_spot9 + result.getMatched_2_NumberCount_spot9();
            matched_3_NumberCount_spot9 = matched_3_NumberCount_spot9 + result.getMatched_3_NumberCount_spot9();
            matched_4_NumberCount_spot9 = matched_4_NumberCount_spot9 + result.getMatched_4_NumberCount_spot9();
            matched_5_NumberCount_spot9 = matched_5_NumberCount_spot9 + result.getMatched_5_NumberCount_spot9();
            matched_6_NumberCount_spot9 = matched_6_NumberCount_spot9 + result.getMatched_6_NumberCount_spot9();
            matched_7_NumberCount_spot9 = matched_7_NumberCount_spot9 + result.getMatched_7_NumberCount_spot9();
            matched_8_NumberCount_spot9 = matched_8_NumberCount_spot9 + result.getMatched_8_NumberCount_spot9();
            matched_9_NumberCount_spot9 = matched_9_NumberCount_spot9 + result.getMatched_9_NumberCount_spot9();
            spot9SelectedCount = spot9SelectedCount + result.getSpot9SelectedCount();
            matched9NumbersCount = matched9NumbersCount + result.getMatched9SelectedCount();


            matched_0_NumberCount_spot10 = matched_0_NumberCount_spot10 + result.getMatched_0_NumberCount_spot10();
            matched_1_NumberCount_spot10 = matched_1_NumberCount_spot10 + result.getMatched_1_NumberCount_spot10();
            matched_2_NumberCount_spot10 = matched_2_NumberCount_spot10 + result.getMatched_2_NumberCount_spot10();
            matched_3_NumberCount_spot10 = matched_3_NumberCount_spot10 + result.getMatched_3_NumberCount_spot10();
            matched_4_NumberCount_spot10 = matched_4_NumberCount_spot10 + result.getMatched_4_NumberCount_spot10();
            matched_5_NumberCount_spot10 = matched_5_NumberCount_spot10 + result.getMatched_5_NumberCount_spot10();
            matched_6_NumberCount_spot10 = matched_6_NumberCount_spot10 + result.getMatched_6_NumberCount_spot10();
            matched_7_NumberCount_spot10 = matched_7_NumberCount_spot10 + result.getMatched_7_NumberCount_spot10();
            matched_8_NumberCount_spot10 = matched_8_NumberCount_spot10 + result.getMatched_8_NumberCount_spot10();
            matched_9_NumberCount_spot10 = matched_9_NumberCount_spot10 + result.getMatched_9_NumberCount_spot10();
            matched_10_NumberCount_spot10 = matched_10_NumberCount_spot10 + result.getMatched_10_NumberCount_spot10();
            spot10SelectedCount = spot10SelectedCount + result.getSpot10SelectedCount();
            matched10NumbersCount = matched10NumbersCount + result.getMatched10SelectedCount();

            totalWinSpot2 = totalWinSpot2.add(result.getTotalWinSpot2());
            totalWinSpot3 = totalWinSpot3.add(result.getTotalWinSpot3());
            totalWinSpot4 = totalWinSpot4.add(result.getTotalWinSpot4());
            totalWinSpot5 = totalWinSpot5.add(result.getTotalWinSpot5());
            totalWinSpot6 = totalWinSpot6.add(result.getTotalWinSpot6());
            totalWinSpot7 = totalWinSpot7.add(result.getTotalWinSpot7());
            totalWinSpot8 = totalWinSpot8.add(result.getTotalWinSpot8());
            totalWinSpot9 = totalWinSpot9.add(result.getTotalWinSpot9());
            totalWinSpot10 = totalWinSpot10.add(result.getTotalWinSpot10());


        }

        synchronized BigDecimal getWinAmount() {
            return winAmount;
        }

        synchronized int getHitCount() {
            return hitCount;
        }
        synchronized BigDecimal getMaxWinAmount() {
            return maxWinAmount;
        }
        synchronized int getMatchedNumbersCount() {
            return matchedNumbersCount;
        }
        synchronized int getNumberOfSpots() {
            return numberOfSpots;
        }

        synchronized int getMatched0NumberCount_spot2() {
            return matched0NumberCount_spot2;
        }
        synchronized int getMatched1NumberCount_spot2() {
            return matched1NumberCount_spot2;
        }
        synchronized int getSpot3SelectedCount() {
            return spot3SelectedCount;
        }
        synchronized int getMatched3NumbersCount() {
            return matched3NumbersCount;
        }
        synchronized int getMatched0NumberCount_spot3() {
            return matched0NumberCount_spot3;
        }
        synchronized int getMatched1NumberCount_spot3() {
            return matched1NumberCount_spot3;
        }
        synchronized int getMatched2NumberCount_spot3() {
            return matched2NumberCount_spot3;
        }
        synchronized int getMatched3NumberCount_spot3() {
            return matched3NumbersCount_spot3;
        }
        synchronized int getSpot4SelectedCount() {
            return spot4SelectedCount;
        }
        synchronized int getMatched4NumbersCount() {
            return matched4NumbersCount;
        }
        synchronized int getMatched0NumberCount_spot4() {
            return matched0NumberCount_spot4;
        }
        synchronized int getMatched1NumberCount_spot4() {
            return matched1NumberCount_spot4;
        }
        synchronized int getMatched2NumberCount_spot4() {
            return matched2NumberCount_spot4;
        }
        synchronized int getMatched3NumberCount_spot4() {
            return matched3NumberCount_spot4;
        }
        synchronized int getMatched4NumberCount_spot4() {
            return matched4NumberCount_spot4;
        }

        synchronized int getSpot5SelectedCount() {
            return spot5SelectedCount;
        }
        synchronized int getMatched5NumbersCount() {
            return matched5NumbersCount;
        }
        synchronized int getMatched0NumberCount_spot5() {
            return matched_0_NumberCount_spot5;
        }
        synchronized int getMatched1NumberCount_spot5() {
            return matched_1_NumberCount_spot5;
        }
        synchronized int getMatched2NumberCount_spot5() {
            return matched_2_NumberCount_spot5;
        }
        synchronized int getMatched3NumberCount_spot5() {
            return matched_3_NumberCount_spot5;
        }
        synchronized int getMatched4NumberCount_spot5() {
            return matched_4_NumberCount_spot5;
        }
        synchronized int getMatched5NumberCount_spot5() {
            return matched_5_NumberCount_spot5;
        }

        synchronized int getMatched0NumberCount_spot6() {
            return matched_0_NumberCount_spot6;
        }
        synchronized int getMatched1NumberCount_spot6() {
            return matched_1_NumberCount_spot6;
        }
        synchronized int getMatched2NumberCount_spot6() {
            return matched_2_NumberCount_spot6;
        }
        synchronized int getMatched3NumberCount_spot6() {
            return matched_3_NumberCount_spot6;
        }
        synchronized int getMatched4NumberCount_spot6() {
            return matched_4_NumberCount_spot6;
        }
        synchronized int getMatched5NumberCount_spot6() {
            return matched_5_NumberCount_spot6;
        }
        synchronized int getMatched6NumberCount_spot6() {
            return matched_6_NumberCount_spot6;
        }
        synchronized int getSpot6SelectedCount() {
            return spot6SelectedCount;
        }
        synchronized int getMatched6NumbersCount() {
            return matched6NumbersCount;
        }
        synchronized  int getMatched0NumberCount_spot7() {
            return matched_0_NumberCount_spot7;
        }
        synchronized int getMatched1NumberCount_spot7() {
            return matched_1_NumberCount_spot7;
        }
        synchronized int getMatched2NumberCount_spot7() {
            return matched_2_NumberCount_spot7;
        }
        synchronized int getMatched3NumberCount_spot7() {
            return matched_3_NumberCount_spot7;
        }
        synchronized int getMatched4NumberCount_spot7() {
            return matched_4_NumberCount_spot7;
        }
        synchronized int getMatched5NumberCount_spot7() {
            return matched_5_NumberCount_spot7;
        }
        synchronized int getMatched6NumberCount_spot7() {
            return matched_6_NumberCount_spot7;
        }
        synchronized int getMatched7NumberCount_spot7() {
            return matched_7_NumberCount_spot7;
        }
        synchronized int getSpot7SelectedCount() {
            return spot7SelectedCount;
        }
        synchronized int getMatched7NumbersCount() {
            return matched7NumbersCount;
        }
        synchronized int getMatched0NumberCount_spot8() {
            return matched_0_NumberCount_spot8;
        }
        synchronized int getMatched1NumberCount_spot8() {
            return matched_1_NumberCount_spot8;
        }
        synchronized int getMatched2NumberCount_spot8() {
            return matched_2_NumberCount_spot8;
        }
        synchronized int getMatched3NumberCount_spot8() {
            return matched_3_NumberCount_spot8;
        }
        synchronized int getMatched4NumberCount_spot8() {
            return matched_4_NumberCount_spot8;
        }
        synchronized int getMatched5NumberCount_spot8() {
            return matched_5_NumberCount_spot8;
        }
        synchronized int getMatched6NumberCount_spot8() {
            return matched_6_NumberCount_spot8;
        }
        synchronized int getMatched7NumberCount_spot8() {
            return matched_7_NumberCount_spot8;
        }
        synchronized int getMatched8NumberCount_spot8() {
            return matched_8_NumberCount_spot8;
        }
        synchronized int getSpot8SelectedCount() {
            return spot8SelectedCount;
        }
        synchronized int getMatched8NumbersCount() {
            return matched8NumbersCount;
        }



        synchronized int getSpot9SelectedCount() {
            return spot9SelectedCount;
        }
        synchronized int getMatched9NumbersCount() {
            return matched9NumbersCount;
        }
        synchronized int getSpot10SelectedCount() {
            return spot10SelectedCount;
        }
        synchronized int getMatched10NumbersCount() {
            return matched10NumbersCount;
        }



    }
}