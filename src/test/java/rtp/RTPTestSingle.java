package rtp;

import com.pixiu.fortune8keno.fortune8keno.game.constants.RTP;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

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
    private final long totalRuns = 500_000_000L;



    // How many rounds to run per batch, across all threads combined.
    // Tune this down (e.g. 1_000_000L) if 5M still struggles on your machine,
    // or up if your machine handles it comfortably and you want fewer batches.
    private final long batchSize = 5_000_000L;

    // Running total across all batches — accumulated safely between batches
    // since batches run sequentially (one batch's threads fully finish before
    // the next batch starts), so no synchronization is needed here.
    private BigDecimal totalWins = BigDecimal.ZERO;

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

        totalWins = totalWins.add(accumulator.getWinAmount());

        System.out.println("Completed batch " + batchNum + "/" + totalBatches
                + " (" + roundsThisBatch + " rounds) — running total wins: " + totalWins);
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
        int totalWin = this.totalWins.intValue();
        double rtpPercentage = (double) totalWin / totalStake.intValue() * 100;

        System.out.println("----------------------------------------");
        System.out.println("Total Stake: " + totalStake);
        System.out.println("Total Win: " + totalWin);
        System.out.println("RTP: " + rtpPercentage + "% ");

        long endTime = System.currentTimeMillis();
        System.out.println("Time taken: " + (endTime - startingTime) / 1000.0 + " seconds");
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

        BatchAccumulator(int expectedThreads) {
            this.expectedThreads = expectedThreads;
        }

        synchronized void add(RtpResult result) {
            winAmount = winAmount.add(result.getWinAmount());
            finishedThreads++;
        }

        synchronized BigDecimal getWinAmount() {
            return winAmount;
        }
    }
}