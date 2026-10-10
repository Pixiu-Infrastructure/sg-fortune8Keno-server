package rtp;

import com.pixiu.fortune8keno.fortune8keno.dto.PlayerState;
import com.pixiu.fortune8keno.fortune8keno.dto.play.PlayRequest;
import com.pixiu.fortune8keno.fortune8keno.dto.play.PlayRequestCommandData;
import com.pixiu.fortune8keno.fortune8keno.results.StateResult;
import com.pixiu.fortune8keno.fortune8keno.service.GamePlayService;
import com.pixiu.fortune8keno.fortune8keno.results.SpinResult;

import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.Callable;


@Data
@Accessors(chain = true)
@Slf4j
public class RtpTask implements Callable<RoundResult> {
    private GamePlayService gamePlayService;


    private BigDecimal stakeValue;

    private BigDecimal expectedRTP;

    private PlayerState playerState;
    private List<Integer> randomNumbers = new ArrayList<>();
    private boolean saveResults = false;
    //    private PlayRequestCommandData initialCommand;
    private Random random;

    @Override
    public RoundResult call() {

        PlayRequest playRequest = getBaseRequest(random);
        SpinResult baseGamePlayResponse;

        baseGamePlayResponse = gamePlayService.play(playRequest, expectedRTP);
        RoundResult result = getRoundResult(baseGamePlayResponse);

        if (baseGamePlayResponse.getError() != null && baseGamePlayResponse.getError().ifPresent()) {
            throw new RuntimeException("Error in game play response: " + baseGamePlayResponse.getError().getMessage());
        }
        return result;

    }

    private static RoundResult getRoundResult(SpinResult baseGamePlayResponse) {
        int matchedNumberCount = baseGamePlayResponse.getResult().getHits();
        int playerSelectedNumberCount =  baseGamePlayResponse.getResult().getSelectedNumbers().size();


        RoundResult result = new RoundResult();
        result.setWinAmount(baseGamePlayResponse.getPrizeAmount());
        result.setMultiplier(baseGamePlayResponse.getResult().getMultiplier());

        result.setHitCount(matchedNumberCount);
        result.setPlayerSelectedNumberCount(playerSelectedNumberCount);
        result.setLastServerNum(baseGamePlayResponse.getResult().getLastNumber());
        result.setMultiplierTriggered(baseGamePlayResponse.getResult().getMultiplier() > 1);
        return result;
    }


    Set<Integer> getPlayerNumbers(Random random) {
        // This method should return the player's chosen numbers.
        // player may select between 2 and 10 numbers from a pool of 80 numbers

        int numberOfSpots = random.nextInt(2, 11);
        if (numberOfSpots == 1 || numberOfSpots > 10) {
            throw new IllegalStateException("Number of spots must be between 2 and 10. Generated: " + numberOfSpots);
        }
//        numberOfSpots = 2; // For testing purposes, you can set this to a fixed value between 2 and 10

        Set<Integer> playerNumbers = new HashSet<>(); // Clear previous player numbers before generating new ones


        while (playerNumbers.size() < numberOfSpots) {
            int chosenNumber = random.nextInt(80) + 1;
            playerNumbers.add(chosenNumber);

        }
        if (numberOfSpots != playerNumbers.size()) {
            System.out.println("Duplicate numbers generated, regenerating...");
            throw new RuntimeException("Duplicate numbers generated, regenerating...");
        }

        return playerNumbers;
    }

    private PlayRequest getBaseRequest(Random random) {
        PlayRequestCommandData initialCommand = new PlayRequestCommandData();
        initialCommand.setAction("start");
        initialCommand.setStakeAmount(BigDecimal.ONE);
        initialCommand.setSelectedNumbers(getPlayerNumbers(random));
        initialCommand.setRandomNumber(random);
        StateResult stateResult = new StateResult();


        return new PlayRequest()
                .setStakeAmount(stakeValue)
                .setState(stateResult)
                .setCommand(initialCommand);
    }


}
