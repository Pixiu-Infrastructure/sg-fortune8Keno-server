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
public class RTPTestSingleSimplified_Spot5 {

    private static int forceNumOfThreads = 0; // set this greater than 0 will force to run range check in number of threads/times
    private int availableThreads;

    private final BigDecimal rtp = RTP.VARIANT_92;
    static BigDecimal stakeValue = BigDecimal.ONE;

    // Total rounds across the WHOLE test (all batches combined).
    private final long totalRuns = 1_00_00_00_00_0L;
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

        // One master per batch; split() gives each thread a statistically
        // independent sub-generator with no CAS overhead.
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

        rtpDataToPrint.setMatched5NumbersCount(rtpDataToPrint.getMatched5NumbersCount() + accumulator.getMatched5NumbersCount());
        rtpDataToPrint.setSpot5SelectedCount(rtpDataToPrint.getSpot5SelectedCount() + accumulator.getSpot5SelectedCount());
        rtpDataToPrint.setMatched_0_NumberCount_spot5(rtpDataToPrint.getMatched_0_NumberCount_spot5() + accumulator.getMatched0NumberCount_spot5());
        rtpDataToPrint.setMatched_1_NumberCount_spot5(rtpDataToPrint.getMatched_1_NumberCount_spot5() + accumulator.getMatched1NumberCount_spot5());
        rtpDataToPrint.setMatched_2_NumberCount_spot5(rtpDataToPrint.getMatched_2_NumberCount_spot5() + accumulator.getMatched2NumberCount_spot5());
        rtpDataToPrint.setMatched_3_NumberCount_spot5(rtpDataToPrint.getMatched_3_NumberCount_spot5() + accumulator.getMatched3NumberCount_spot5());
        rtpDataToPrint.setMatched_4_NumberCount_spot5(rtpDataToPrint.getMatched_4_NumberCount_spot5() + accumulator.getMatched4NumberCount_spot5());
        rtpDataToPrint.setMatched_5_NumberCount_spot5(rtpDataToPrint.getMatched_5_NumberCount_spot5() + accumulator.getMatched5NumberCount_spot5());
        rtpDataToPrint.setTotalWinSpot5(rtpDataToPrint.getTotalWinSpot5().add(accumulator.totalWinSpot5));
        rtpDataToPrint.setSumOfSquaresSpot5(rtpDataToPrint.getSumOfSquaresSpot5().add(accumulator.getSumOfSquaresSpot5()));


        System.out.println("Completed batch " + batchNum + "/" + totalBatches
                + " (" + roundsThisBatch + " rounds) — running total wins: " + rtpDataToPrint.getTotalWins());
    }

    private void runTask(long eachRun, Random random, BatchAccumulator accumulator) {
        RtpRunnerSimple_Spot5 rtpRunner = RtpRunnerSimple_Spot5.builder()
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

        System.out.println("----------------------------------------");

        System.out.println("Total Win: " + totalWin);
        System.out.println("RTP: " + rtpPercentage + "% ");


        long endTime = System.currentTimeMillis();
        long seconds = (endTime - startingTime) / 1000;
        System.out.println("Time taken: " + seconds + " seconds");
        int rtp = this.rtp.multiply(BigDecimal.valueOf(100)).intValue();
        String fileName = "RTP_Result_1B_Spot_5_"+rtp+".xlsx";

        try {
            RtpExcelWriterSimplified_Spot5.write(fileName, totalRuns, totalStake, rtpDataToPrint);
            System.out.println("Excel written: " + fileName);
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

        int spot5SelectedCount = 0;
        int matched5NumbersCount = 0;
        int matched_0_NumberCount_spot5 = 0;
        int matched_1_NumberCount_spot5 = 0;
        int matched_2_NumberCount_spot5 = 0;
        int matched_3_NumberCount_spot5 = 0;
        int matched_4_NumberCount_spot5 = 0;
        int matched_5_NumberCount_spot5 = 0;

        BigDecimal totalWinSpot5 = BigDecimal.ZERO;
        BigDecimal sumOfSquaresSpot5 = BigDecimal.ZERO;





        BatchAccumulator(int expectedThreads) {
            this.expectedThreads = expectedThreads;
        }

        synchronized void add(RtpResult result) {
            hitCount = hitCount + result.getHitCount();
            if(result.getMaxWinAmount().compareTo(maxWinAmount) > 0){
                maxWinAmount = result.getMaxWinAmount();
            }
            winAmount = winAmount.add(result.getWinAmount());
            finishedThreads++;

            spot5SelectedCount = spot5SelectedCount + result.getSpot5SelectedCount();
            matched5NumbersCount = matched5NumbersCount + result.getMatched5NumbersCount();
            matched_0_NumberCount_spot5 = matched_0_NumberCount_spot5 + result.getMatched_0_NumberCount_spot5();
            matched_1_NumberCount_spot5 = matched_1_NumberCount_spot5 + result.getMatched_1_NumberCount_spot5();
            matched_2_NumberCount_spot5 = matched_2_NumberCount_spot5 + result.getMatched_2_NumberCount_spot5();
            matched_3_NumberCount_spot5 = matched_3_NumberCount_spot5 + result.getMatched_3_NumberCount_spot5();
            matched_4_NumberCount_spot5 = matched_4_NumberCount_spot5 + result.getMatched_4_NumberCount_spot5();
            matched_5_NumberCount_spot5 = matched_5_NumberCount_spot5 + result.getMatched_5_NumberCount_spot5();
            totalWinSpot5 = totalWinSpot5.add(result.getTotalWinSpot5());
            sumOfSquaresSpot5  = sumOfSquaresSpot5.add(result.getSumOfSquaresSpot5());
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

        synchronized BigDecimal getSumOfSquaresSpot5()  { return sumOfSquaresSpot5; }




    }
}