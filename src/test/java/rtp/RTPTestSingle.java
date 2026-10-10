package rtp;

import com.pixiu.fortune8keno.fortune8keno.game.constants.RTP;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigDecimal;
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
    private final long totalRuns = 100_000_00L;



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
        rtpDataToPrint.setMaxWinAmount(Math.max(rtpDataToPrint.getMaxWinAmount(), accumulator.getMaxWinAmount()));
        rtpDataToPrint.setTotalHits(rtpDataToPrint.getTotalHits() + accumulator.getMatchedNumbersCount());
        rtpDataToPrint.setTotalSpotSelectedCount(rtpDataToPrint.getTotalSpotSelectedCount() + accumulator.getNumberOfSpots());

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
        int totalWin = rtpDataToPrint.getTotalWins().intValue();
        double rtpPercentage = (double) totalWin / totalStake.intValue() * 100;

        System.out.println("----------------------------------------");
        System.out.println("Total Stake: " + totalStake);
        System.out.println("Total Win: " + totalWin);
        System.out.println("RTP: " + rtpPercentage + "% ");
        System.out.println("Hit rate: " + ((double) rtpDataToPrint.getTotalHitCount() / totalRuns * 100) + "%");
        System.out.println("Matched 2 Numbers Count: " + rtpDataToPrint.getTotalHits());
        System.out.println("Total Spot Selected Count: " + rtpDataToPrint.getTotalSpotSelectedCount());
        System.out.println("Spot 2 Hit Rate: " + ((double) rtpDataToPrint.getTotalHits() / rtpDataToPrint.getTotalSpotSelectedCount() * 100) + "%");
        System.out.println("Max Win Amount: " + rtpDataToPrint.getMaxWinAmount());

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
        private double maxWinAmount = 0;
        private int matchedNumbersCount = 0;
        private int numberOfSpots = 0;

        BatchAccumulator(int expectedThreads) {
            this.expectedThreads = expectedThreads;
        }

        synchronized void add(RtpResult result) {
            winAmount = winAmount.add(result.getWinAmount());
            hitCount = hitCount + result.getHitCount();
            if(result.getMaxWinAmount() > maxWinAmount){
                maxWinAmount = result.getMaxWinAmount();
            }
            finishedThreads++;
            matchedNumbersCount = matchedNumbersCount + result.getMatchedNumbersCount();
            numberOfSpots = numberOfSpots + result.getNumberOfSpots();
        }

        synchronized BigDecimal getWinAmount() {
            return winAmount;
        }

        synchronized int getHitCount() {
            return hitCount;
        }
        synchronized double getMaxWinAmount() {
            return maxWinAmount;
        }
        synchronized int getMatchedNumbersCount() {
            return matchedNumbersCount;
        }
        synchronized int getNumberOfSpots() {
            return numberOfSpots;
        }



    }
}