package rtp;

import com.pixiu.fortune8keno.fortune8keno.config.GameEngineProperties;
import com.pixiu.fortune8keno.fortune8keno.dto.PlayerState;
import com.pixiu.fortune8keno.fortune8keno.dto.play.PlayRequest;
import com.pixiu.fortune8keno.fortune8keno.dto.play.PlayRequestCommandData;
import com.pixiu.fortune8keno.fortune8keno.results.StateResult;
import com.pixiu.fortune8keno.fortune8keno.service.GamePlayService;
import com.pixiu.fortune8keno.fortune8keno.play.spin.SpinGame;
import com.pixiu.fortune8keno.fortune8keno.results.SpinResult;


import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class KenoRTP {
    static int numberOfAvailableThreads = Runtime.getRuntime().availableProcessors();
    private static GamePlayService gamePlayService;
    //static int numberOfAvailableThreads = 1;
    static int rounds = 100_00; // Number of rounds to simulate

    static BigDecimal stakeValue = BigDecimal.ONE; // Assuming a fixed stake of 1 unit per round

    static double totalWin = 0;
    static int totalSpot2Count = 0;
    static int roundPerThread = rounds / numberOfAvailableThreads;
    static long startingTime;
    static RtpResult rtpResult = new RtpResult();
    static PlayerState playerState;
    static PlayRequestCommandData initialCommand;

    static int finishedThreadCount = 0;


    public static void main(String[] args) throws IllegalAccessException {


//        playGame();

        ExecutorService executorService = Executors.newFixedThreadPool(numberOfAvailableThreads);
        startingTime = System.currentTimeMillis();

        for(int i = 0; i < numberOfAvailableThreads; i++){
            executorService.submit(()-> {
                try {
                    simulateKenoRounds();
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            });

        }
    }



    private static double playGame() throws IllegalAccessException {
        Random random = new Random();


        if (gamePlayService == null) gamePlayService = RtpSetUp.createGamePlayService();


        SpinGame spinGame ;

        double totalWinPerThread = 0;




        int countWin = 0;


        for (int i = 0; i < roundPerThread; i++) {

            PlayRequest playRequest = getBaseRequest(random);
            SpinResult spinResult =  gamePlayService.play(playRequest, BigDecimal.valueOf(0.86));


            double winAmount = spinResult.getPrizeAmount();
            if (winAmount > 0) {
                countWin++;
            }

            totalWinPerThread += winAmount;
        }
//        int totalStake = stakeValue.intValue() * roundPerThread;
//        double rtp = (double) totalWinPerThread / totalStake * 100;
//        System.out.println("Hit rate: " + ((double) countWin / roundPerThread * 100) + "%");
//        System.out.println("Total Stake: " + totalStake);
//        System.out.println("Total Win: " + totalWinPerThread);
//        System.out.println("RTP: " + rtp + "% ");




        return totalWinPerThread;
    }

    private static synchronized void addToRtpResult(double result) {
        finishedThreadCount++;
        totalWin += result;

        if (finishedThreadCount == numberOfAvailableThreads) {
            int totalStake = stakeValue.intValue() * rounds;
            System.out.println("All threads finished. Total RTP result: " + (totalWin / totalStake * 100) + "%");

            long endTime = System.currentTimeMillis();
            System.out.println("Time taken: " + (endTime - startingTime) / 1000.0 + " seconds");
        }

    }

    private static PlayRequest getBaseRequest(Random random) {
        initialCommand = new PlayRequestCommandData();
        initialCommand.setAction("start");
        initialCommand.setStakeAmount(stakeValue);
        initialCommand.setSelectedNumbers(getPlayerNumbers(random));
        StateResult stateResult = new StateResult();


        return new PlayRequest()
                .setStakeAmount(stakeValue)
                .setState(stateResult)
                .setCommand(initialCommand);
    }


    static Set<Integer> getPlayerNumbers(Random random) {
        // This method should return the player's chosen numbers.
        // player may select between 2 and 10 numbers from a pool of 80 numbers

        int numberOfSpots = random.nextInt(2, 11) ;
        if(numberOfSpots == 1 || numberOfSpots > 10){
            throw new IllegalStateException("Number of spots must be between 2 and 10. Generated: " + numberOfSpots);
        }
//        numberOfSpots = 2; // For testing purposes, you can set this to a fixed value between 2 and 10

        Set<Integer> playerNumbers = new HashSet<>(); // Clear previous player numbers before generating new ones


        while (playerNumbers.size() < numberOfSpots) {
            int chosenNumber = random.nextInt(80) + 1;
            playerNumbers.add(chosenNumber);

        }
        if(numberOfSpots != playerNumbers.size()){
            System.out.println("Duplicate numbers generated, regenerating...");
            throw new RuntimeException("Duplicate numbers generated, regenerating...");
        }

        return playerNumbers;
    }

    private static void simulateKenoRounds() throws IllegalAccessException {

        addToRtpResult(playGame());

    }
}
