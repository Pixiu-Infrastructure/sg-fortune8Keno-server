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

    private final BigDecimal rtp = RTP.VARIANT_92;
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

        rtpDataToPrint.setMatched3NumbersCount(rtpDataToPrint.getMatched3NumbersCount() + accumulator.getMatched3NumbersCount());
        rtpDataToPrint.setSpot3SelectedCount(rtpDataToPrint.getSpot3SelectedCount() + accumulator.getSpot3SelectedCount());
        rtpDataToPrint.setMatched4NumbersCount(rtpDataToPrint.getMatched4NumbersCount() + accumulator.getMatched4NumbersCount());
        rtpDataToPrint.setSpot4SelectedCount(rtpDataToPrint.getSpot4SelectedCount() + accumulator.getSpot4SelectedCount());
        rtpDataToPrint.setMatched5NumbersCount(rtpDataToPrint.getMatched5NumbersCount() + accumulator.getMatched5NumbersCount());
        rtpDataToPrint.setSpot5SelectedCount(rtpDataToPrint.getSpot5SelectedCount() + accumulator.getSpot5SelectedCount());

        rtpDataToPrint.setMatched6NumbersCount(rtpDataToPrint.getMatched6NumbersCount() + accumulator.getMatched6NumbersCount());
        rtpDataToPrint.setSpot6SelectedCount(rtpDataToPrint.getSpot6SelectedCount() + accumulator.getSpot6SelectedCount());
        rtpDataToPrint.setMatched7NumbersCount(rtpDataToPrint.getMatched7NumbersCount() + accumulator.getMatched7NumbersCount());
        rtpDataToPrint.setSpot7SelectedCount(rtpDataToPrint.getSpot7SelectedCount() + accumulator.getSpot7SelectedCount());
        rtpDataToPrint.setMatched8NumbersCount(rtpDataToPrint.getMatched8NumbersCount() + accumulator.getMatched8NumbersCount());
        rtpDataToPrint.setSpot8SelectedCount(rtpDataToPrint.getSpot8SelectedCount() + accumulator.getSpot8SelectedCount());
        rtpDataToPrint.setMatched9NumbersCount(rtpDataToPrint.getMatched9NumbersCount() + accumulator.getMatched9NumbersCount());
        rtpDataToPrint.setSpot9SelectedCount(rtpDataToPrint.getSpot9SelectedCount() + accumulator.getSpot9SelectedCount());
        rtpDataToPrint.setMatched10NumbersCount(rtpDataToPrint.getMatched10NumbersCount() + accumulator.getMatched10NumbersCount());
        rtpDataToPrint.setSpot10SelectedCount(rtpDataToPrint.getSpot10SelectedCount() + accumulator.getSpot10SelectedCount());

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
        private double maxWinAmount = 0;
        private int matchedNumbersCount = 0;
        private int numberOfSpots = 0;

        int spot3SelectedCount = 0;
        int matched3NumbersCount = 0;

        int spot4SelectedCount = 0;
        int matched4NumbersCount = 0;

        int spot5SelectedCount = 0;
        int matched5NumbersCount = 0;

        int spot6SelectedCount = 0;
        int matched6NumbersCount = 0;
        int spot7SelectedCount = 0;
        int matched7NumbersCount = 0;

        int spot8SelectedCount = 0;
        int matched8NumbersCount = 0;
        int spot9SelectedCount = 0;
        int matched9NumbersCount = 0;
        int spot10SelectedCount = 0;
        int matched10NumbersCount = 0;

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
            spot3SelectedCount = spot3SelectedCount + result.getSpot3SelectedCount();
            matched3NumbersCount = matched3NumbersCount + result.getMatched3NumbersCount();
            spot4SelectedCount = spot4SelectedCount + result.getSpot4SelectedCount();
            matched4NumbersCount = matched4NumbersCount + result.getMatched4NumbersCount();
            spot5SelectedCount = spot5SelectedCount + result.getSpot5SelectedCount();
            matched5NumbersCount = matched5NumbersCount + result.getMatched5NumbersCount();

            spot6SelectedCount = spot6SelectedCount + result.getSpot6SelectedCount();
            matched6NumbersCount = matched6NumbersCount + result.getMatched6NumbersCount();
            spot7SelectedCount = spot7SelectedCount + result.getSpot7SelectedCount();
            matched7NumbersCount = matched7NumbersCount + result.getMatched7SelectedCount();
            spot8SelectedCount = spot8SelectedCount + result.getSpot8SelectedCount();
            matched8NumbersCount = matched8NumbersCount + result.getMatched8SelectedCount();
            spot9SelectedCount = spot9SelectedCount + result.getSpot9SelectedCount();
            matched9NumbersCount = matched9NumbersCount + result.getMatched9SelectedCount();
            spot10SelectedCount = spot10SelectedCount + result.getSpot10SelectedCount();
            matched10NumbersCount = matched10NumbersCount + result.getMatched10SelectedCount();


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
        synchronized int getSpot3SelectedCount() {
            return spot3SelectedCount;
        }
        synchronized int getMatched3NumbersCount() {
            return matched3NumbersCount;
        }
        synchronized int getSpot4SelectedCount() {
            return spot4SelectedCount;
        }
        synchronized int getMatched4NumbersCount() {
            return matched4NumbersCount;
        }
        synchronized int getSpot5SelectedCount() {
            return spot5SelectedCount;
        }
        synchronized int getMatched5NumbersCount() {
            return matched5NumbersCount;
        }

        synchronized int getSpot6SelectedCount() {
            return spot6SelectedCount;
        }
        synchronized int getMatched6NumbersCount() {
            return matched6NumbersCount;
        }
        synchronized int getSpot7SelectedCount() {
            return spot7SelectedCount;
        }
        synchronized int getMatched7NumbersCount() {
            return matched7NumbersCount;
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