package com.pixiu.fortune8keno.fortune8keno.play.spin;



import com.pixiu.fortune8keno.fortune8keno.config.GameConfiguration;
import com.pixiu.fortune8keno.fortune8keno.config.GameEngineProperties;
import com.pixiu.fortune8keno.fortune8keno.dto.play.GameState;
import com.pixiu.fortune8keno.fortune8keno.dto.play.GameStateDTO;
import com.pixiu.fortune8keno.fortune8keno.play.PlayRequestParameters;
import com.pixiu.fortune8keno.fortune8keno.results.CommandResult;
import com.pixiu.fortune8keno.fortune8keno.results.SpinResult;
import com.pixiu.fortune8keno.fortune8keno.results.StateResult;
import com.pixiu.fortune8keno.fortune8keno.service.RngClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;


@Component
@RequiredArgsConstructor
public class SpinGame {


    private final RngClient rngClient;

    private final GameEngineProperties properties;



    public SpinResult play(GameConfiguration gameConfiguration,

                           boolean freeSpinsBonusGameActive,
                           PlayRequestParameters playRequestParameters) {

        String action = playRequestParameters.getAction();

        return switch (action) {
            case "start" -> handleStart(gameConfiguration, playRequestParameters);
            case "" -> handleStart(gameConfiguration, playRequestParameters);
            case "collect" -> handleCollect(playRequestParameters);
            default -> SpinResult.error(action, "UNSUPPORTED_ACTION",
                    "Unsupported action: " + action);
        };


    }

    private SpinResult handleCollect( PlayRequestParameters playRequestParameters) {

        GameStateDTO incoming = playRequestParameters.getState().getGameState();
        if (incoming == null || incoming.getCurrentState() != GameState.GAMBLED) {
            return SpinResult.error("collect", "INVALID_STATE",
                    "No round pending collection for this session");
        }

        GameStateDTO finalState = new GameStateDTO();
        finalState.setCurrentState(GameState.COLLECTED);
        finalState.setNextState(GameState.NONE);
        finalState.setCloseRound(true);
        finalState.setSelectedNumbers(incoming.getSelectedNumbers());
        finalState.setDrawnNumbers(incoming.getDrawnNumbers());
        finalState.setMatchedNumbers(incoming.getMatchedNumbers());
        finalState.setWinAmount(incoming.getWinAmount());
//        finalState.setStake(0.0);

        StateResult stateResult = new StateResult();
        stateResult.setGameState(finalState);

        SpinResult response = new SpinResult();
        response.setAction("collect");
        response.setState(stateResult);
        response.setWinAmount(incoming.getWinAmount());




        return response;

    }
    private SpinResult handleStart(GameConfiguration gameConfiguration, PlayRequestParameters playRequestParameters) {
        SpinResult spinResult = new SpinResult();
        Set<Integer> playerNumbers = playRequestParameters.getCommand().getSelectedNumbers();
        BigDecimal stake = playRequestParameters.getStake();

        if (playerNumbers == null || playerNumbers.isEmpty()) {
            return SpinResult.error("start", "NO_NUMBERS_SELECTED",
                    "At least one number must be selected");
        }

        int picks = playerNumbers.size();
        if (!isSupportedPickCount(picks, gameConfiguration)) {
            return SpinResult.error("start", "UNSUPPORTED_PICK_COUNT",
                    "Pick count " + picks + " is not supported (max " + properties.getMaxPicks() + ")");
        }

        Set<Integer> uniqueSelected = new HashSet<>(playerNumbers);
        if (uniqueSelected.size() != playerNumbers.size()) {
            return SpinResult.error("start", "DUPLICATE_SELECTION",
                    "Selected numbers must be unique");
        }

        for (int number : uniqueSelected) {
            if (number < 1 || number > properties.getPoolSize()) {
                return SpinResult.error("start", "SELECTION_OUT_OF_RANGE",
                        "Selected numbers must be between 1 and " + properties.getPoolSize());
            }
        }

//        // The one and only outcome-determining call: draw N unique balls via
//        // the platform RNG (never generated locally).


//        List<Integer> drawn = rngClient.drawUnique(properties.getPoolSize(), properties.getDrawCount());  TODO revert for real rng

        TreeSet<Integer> drawn =  getServerDrawnNumbers(playRequestParameters.getCommand().getRandomNumber());


        int multiplier = 1; // Reset multiplier for each game;
//
//        int lastServerNum = drawn.get(drawn.size() - 1); TODO revert for real rng
        int lastServerNum = drawn.last();
        //getPlayerNumbers(random);
        Set<Integer> uniqueSelectedNumbers = new HashSet<>(drawn); // coverting drawn numbers to set to remove duplicates.


        Set<Integer> matchedNumbers =  checkMatches(playerNumbers,  uniqueSelectedNumbers);
        double winningAmount = 0;
        if(checkIfMultiplierTriggered(playerNumbers, lastServerNum)){
            multiplier = 8;
        }
        KenoPayout selectedPayTable =  gameConfiguration.getGameMathConfigSeparate().getKenoPayouts().get(playerNumbers.size());
        winningAmount = selectedPayTable.calculateWinningAmount(stake.intValue(), matchedNumbers.size());

        spinResult.setPrizeAmount(winningAmount * multiplier);

        StateResult stateResult = new StateResult();
        stateResult.setStake(stake);
        stateResult.setFreeGame("0");


        GameStateDTO state = new GameStateDTO();

        state.setCurrentState(GameState.GAMBLED);
        state.setNextState(GameState.TO_COLLECT);
        stateResult.setGameState(state);

        spinResult.setState(stateResult);

        CommandResult commandResult = new CommandResult();
        commandResult.setSelectedNumbers(playerNumbers);
        commandResult.setDrawnNumbers(uniqueSelectedNumbers);
        commandResult.setMatchedNumbers(matchedNumbers);
        commandResult.setHits(matchedNumbers.size());
        commandResult.setMultiplier(multiplier);
        commandResult.setGameState(state);
        commandResult.setLastNumber(lastServerNum);

        spinResult.setResult(commandResult);
        spinResult.setAction("start");

        spinResult.setWinAmount(spinResult.getPrizeAmount());

        return spinResult;
    }

    private boolean isSupportedPickCount(int picks, GameConfiguration gameConfiguration) {
        return gameConfiguration.getGameMathConfigSeparate().getKenoPayouts().containsKey(picks);
    }

    public TreeSet<Integer> getServerDrawnNumbers(Random random) {
        TreeSet<Integer> serverDrawnNumbers = new TreeSet<>(); // Clear previous drawn numbers before generating new ones

        while (serverDrawnNumbers.size() < 20) {
            int drawnNumber = random.nextInt(80) + 1;
            serverDrawnNumbers.add(drawnNumber);
            //lastServerNum = drawnNumber; // Update lastServerNum with the most recently drawn number

        }
        // check if server numbers is 20 numbers
        if(serverDrawnNumbers.size() != 20){
            System.out.println("Duplicate numbers generated for server draw, regenerating...");
            throw new RuntimeException("Duplicate numbers generated for server draw, regenerating...");
        }
        return  serverDrawnNumbers;
    }
    public Set<Integer> checkMatches(Set<Integer> playerNumbers, Set<Integer> serverDrawnNumbers) {
        Set<Integer> matchedNumbers = new HashSet<>();
        for (Integer number : playerNumbers) {
            if (serverDrawnNumbers.contains(number)) {
                matchedNumbers.add(number);
            }
        }
        return matchedNumbers;
    }

    public boolean checkIfMultiplierTriggered(Set<Integer> playerNumbers, int lastServerNum) {
     return playerNumbers.contains(lastServerNum);
    }

    Set<Integer> getPlayerNumbers(Random random) {
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



}

