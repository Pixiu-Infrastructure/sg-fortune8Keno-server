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
public class RTPTestSingleSimplified_Spot6 {

    private static int forceNumOfThreads = 0; // set this greater than 0 will force to run range check in number of threads/times
    private int availableThreads;

    private final BigDecimal rtp = RTP.VARIANT_92;
    static BigDecimal stakeValue = BigDecimal.ONE;

    // Total rounds across the WHOLE test (all batches combined).
    private final long totalRuns = 100_000L;



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
           // SplittableRandom random = master.split();

            executorService.submit(() -> runTask(runsForThisThread, master, accumulator));
        }

        executorService.shutdown();
        if (!executorService.awaitTermination(5, TimeUnit.MINUTES)) {
            executorService.shutdownNow();
            throw new RuntimeException("Batch " + batchNum + " did not finish within timeout");
        }

        rtpDataToPrint.setTotalWins(rtpDataToPrint.getTotalWins().add(accumulator.getWinAmount()));
        rtpDataToPrint.setTotalHitCount(rtpDataToPrint.getTotalHitCount() + accumulator.getHitCount());
        rtpDataToPrint.setMaxWinAmount(BigDecimal.valueOf(Math.max(accumulator.getMaxWinAmount().intValue(), rtpDataToPrint.getMaxWinAmount().intValue())));

        rtpDataToPrint.setMatched6NumbersCount(rtpDataToPrint.getMatched6NumbersCount() + accumulator.getMatched6NumbersCount());
        rtpDataToPrint.setSpot6SelectedCount(rtpDataToPrint.getSpot6SelectedCount() + accumulator.getSpot6SelectedCount());
        rtpDataToPrint.setMatched_0_NumberCount_spot6(rtpDataToPrint.getMatched_0_NumberCount_spot6() + accumulator.getMatched0NumberCount_spot6());
        rtpDataToPrint.setMatched_1_NumberCount_spot6(rtpDataToPrint.getMatched_1_NumberCount_spot6() + accumulator.getMatched1NumberCount_spot6());
        rtpDataToPrint.setMatched_2_NumberCount_spot6(rtpDataToPrint.getMatched_2_NumberCount_spot6() + accumulator.getMatched2NumberCount_spot6());
        rtpDataToPrint.setMatched_3_NumberCount_spot6(rtpDataToPrint.getMatched_3_NumberCount_spot6() + accumulator.getMatched3NumberCount_spot6());
        rtpDataToPrint.setMatched_4_NumberCount_spot6(rtpDataToPrint.getMatched_4_NumberCount_spot6() + accumulator.getMatched4NumberCount_spot6());
        rtpDataToPrint.setMatched_5_NumberCount_spot6(rtpDataToPrint.getMatched_5_NumberCount_spot6() + accumulator.getMatched5NumberCount_spot6());
        rtpDataToPrint.setMatched_6_NumberCount_spot6(rtpDataToPrint.getMatched_6_NumberCount_spot6() + accumulator.getMatched6NumberCount_spot6());
        rtpDataToPrint.setTotalWinSpot6(rtpDataToPrint.getTotalWinSpot6().add(accumulator.totalWinSpot6));
        rtpDataToPrint.setSumOfSquaresSpot6(rtpDataToPrint.getSumOfSquaresSpot6().add(accumulator.getSumOfSquaresSpot6()));

        rtpDataToPrint.setMultiplierTriggeredCount(rtpDataToPrint.getMultiplierTriggeredCount() + accumulator.getMultiplierTriggeredCount());
        rtpDataToPrint.setAwardGreaterThanEqualTo10(rtpDataToPrint.getAwardGreaterThanEqualTo10() + accumulator.getAwardGreaterThanEqualTo10());
        rtpDataToPrint.setAwardGreaterThanEqualTo20(rtpDataToPrint.getAwardGreaterThanEqualTo20() + accumulator.getAwardGreaterThanEqualTo20());
        rtpDataToPrint.setAwardGreaterThanEqualTo50(rtpDataToPrint.getAwardGreaterThanEqualTo50() + accumulator.getAwardGreaterThanEqualTo50());
        rtpDataToPrint.setAwardGreaterThanEqualTo100(rtpDataToPrint.getAwardGreaterThanEqualTo100() + accumulator.getAwardGreaterThanEqualTo100());
        rtpDataToPrint.setAwardGreaterThanEqualTo1000(rtpDataToPrint.getAwardGreaterThanEqualTo1000() + accumulator.getAwardGreaterThanEqualTo1000());


        System.out.println("Completed batch " + batchNum + "/" + totalBatches
                + " (" + roundsThisBatch + " rounds) — running total wins: " + rtpDataToPrint.getTotalWins());
    }

    private void runTask(long eachRun, Random random, BatchAccumulator accumulator) {
        RtpRunnerSimple_Spot6 rtpRunner = RtpRunnerSimple_Spot6.builder()
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
        String fileName = "RTP_Result_1B_Spot_6_"+rtp+".xlsx";

        try {
            RtpExcelWriterSimplified_Spot6.write(fileName, totalRuns, totalStake, rtpDataToPrint);
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

        int matched_0_NumberCount_spot6 = 0;
        int matched_1_NumberCount_spot6 = 0;
        int matched_2_NumberCount_spot6 = 0;
        int matched_3_NumberCount_spot6 = 0;
        int matched_4_NumberCount_spot6 = 0;
        int matched_5_NumberCount_spot6 = 0;
        int matched_6_NumberCount_spot6 = 0;
        int spot6SelectedCount = 0;
        int matched6NumbersCount = 0;
        BigDecimal totalWinSpot6 = BigDecimal.ZERO;
        BigDecimal sumOfSquaresSpot6 = BigDecimal.ZERO;


        int multiplierTriggeredCount = 0;
        int awardGreaterThanEqualTo10 = 0;
        int awardGreaterThanEqualTo20 = 0;
        int awardGreaterThanEqualTo50 = 0;
        int awardGreaterThanEqualTo100 = 0;
        int awardGreaterThanEqualTo1000 = 0;



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
            matched_0_NumberCount_spot6 = matched_0_NumberCount_spot6 + result.getMatched_0_NumberCount_spot6();
            matched_1_NumberCount_spot6 = matched_1_NumberCount_spot6 + result.getMatched_1_NumberCount_spot6();
            matched_2_NumberCount_spot6 = matched_2_NumberCount_spot6 + result.getMatched_2_NumberCount_spot6();
            matched_3_NumberCount_spot6 = matched_3_NumberCount_spot6 + result.getMatched_3_NumberCount_spot6();
            matched_4_NumberCount_spot6 = matched_4_NumberCount_spot6 + result.getMatched_4_NumberCount_spot6();
            matched_5_NumberCount_spot6 = matched_5_NumberCount_spot6 + result.getMatched_5_NumberCount_spot6();
            matched_6_NumberCount_spot6 = matched_6_NumberCount_spot6 + result.getMatched_6_NumberCount_spot6();
            spot6SelectedCount = spot6SelectedCount + result.getSpot6SelectedCount();
            matched6NumbersCount = matched6NumbersCount + result.getMatched6NumbersCount();
            totalWinSpot6 = totalWinSpot6.add(result.getTotalWinSpot6());
            sumOfSquaresSpot6  = sumOfSquaresSpot6.add(result.getSumOfSquaresSpot6());

            multiplierTriggeredCount = multiplierTriggeredCount + result.getMultiplierTriggeredCount();
            awardGreaterThanEqualTo10 = awardGreaterThanEqualTo10 + result.getAwardGreaterThanEqualTo10();
            awardGreaterThanEqualTo20 = awardGreaterThanEqualTo20 + result.getAwardGreaterThanEqualTo20();
            awardGreaterThanEqualTo50 = awardGreaterThanEqualTo50 + result.getAwardGreaterThanEqualTo50();
            awardGreaterThanEqualTo100 = awardGreaterThanEqualTo100 + result.getAwardGreaterThanEqualTo100();
            awardGreaterThanEqualTo1000 = awardGreaterThanEqualTo1000 + result.getAwardGreaterThanEqualTo1000();
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
        synchronized BigDecimal getSumOfSquaresSpot6()  { return sumOfSquaresSpot6; }

        synchronized int getMultiplierTriggeredCount() {
            return multiplierTriggeredCount;
        }
        synchronized int getAwardGreaterThanEqualTo10() {
            return awardGreaterThanEqualTo10;
        }
        synchronized int getAwardGreaterThanEqualTo20() {
            return awardGreaterThanEqualTo20;
        }
        synchronized int getAwardGreaterThanEqualTo50() {
            return awardGreaterThanEqualTo50;
        }
        synchronized int getAwardGreaterThanEqualTo100() {
            return awardGreaterThanEqualTo100;
        }
        synchronized int getAwardGreaterThanEqualTo1000() {
            return awardGreaterThanEqualTo1000;
        }
    }
}