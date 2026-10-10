package rtp;

import com.pixiu.fortune8keno.fortune8keno.dto.play.PlayRequestCommandData;
import com.pixiu.fortune8keno.fortune8keno.service.GamePlayService;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;


@Builder
@Slf4j
@Data
@Accessors(chain = true)
public class RtpRunner implements Callable<RtpResult> {

    @Builder.Default
    private final int maxRunsToReportSTD = 1_000_000_0;
    private BigDecimal expectedRTP;
    private Random random;

    private GamePlayService gamePlayService;

    @Builder.Default
    private boolean saveResults = false;

    @Builder.Default
    List<RoundResult> roundResults = new ArrayList<>();

    private String rngContainerAddress;

    @Builder.Default
    private PlayRequestCommandData initialCommand = new PlayRequestCommandData(); //default to basic spin

    @Builder.Default
    private BigDecimal stake = new BigDecimal(1);


    private int totalRuns;


    public RtpResult call() throws Exception {


        if (gamePlayService == null) gamePlayService = RtpSetUp.createGamePlayService();
        initialCommand.setAction("start");
        initialCommand.setStakeAmount(stake);

        BigDecimal totalWins = BigDecimal.ZERO;
        int totalHitCount = 0;
        int spot2SelectedCount = 0;
        int matched2NumbersCount = 0;

        int spot3SelectedCount = 0;
        int matched3NumbersCount = 0;

        int spot4SelectedCount = 0;
        int matched4NumbersCount = 0;

        int spot5SelectedCount = 0;
        int matched5NumbersCount = 0;
        int spot6SelectedCount = 0;
        int matched6NumbersCount = 0;
        int spot7SelectedCount = 0;
        int matched7SelectedCount = 0;

        int spot8SelectedCount = 0;
        int matched8SelectedCount = 0;
        int spot9SelectedCount = 0;
        int matched9SelectedCount = 0;
        int spot10SelectedCount = 0;
        int matched10SelectedCount = 0;


        double maxWin =0;


        for (int i = 1; i <= totalRuns; i++) {

            RoundResult roundResult = new RtpTask().setStakeValue(stake)
                    .setGamePlayService(gamePlayService)
                    .setExpectedRTP(expectedRTP)
                    .setSaveResults(saveResults)
//                    .setInitialCommand(initialCommand)
                    .setRandom(random)
                    .call();

            totalWins = totalWins
                    .add(BigDecimal.valueOf(roundResult.getWinAmount()));
            int playerSelectedCount = roundResult.getPlayerSelectedNumberCount();
            double roundWinAmount = roundResult.getWinAmount();
            int roundHitCount = roundResult.getHitCount();
            if (roundWinAmount > 0) {
                totalHitCount = totalHitCount + 1;
            }
            if(roundWinAmount > maxWin ){
                maxWin = roundWinAmount;
            }
            if(playerSelectedCount == 2 && roundHitCount == 2){
               matched2NumbersCount++;
            }
            if(playerSelectedCount == 2){
                spot2SelectedCount++;
            }

            if(playerSelectedCount == 3 && roundHitCount > 1 && roundHitCount < 4){
                matched3NumbersCount++;
            }
            if (playerSelectedCount == 3){
                spot3SelectedCount++;
            }
            if(playerSelectedCount == 4 && roundHitCount > 1 && roundHitCount < 5){
                matched4NumbersCount++;
            }
            if (playerSelectedCount == 4){
                spot4SelectedCount++;
            }
            if(playerSelectedCount == 5 && roundHitCount > 2 && roundHitCount < 6){
                matched5NumbersCount++;
            }
            if (playerSelectedCount == 5){
                spot5SelectedCount++;
            }

            if(playerSelectedCount == 6 && roundHitCount > 2 && roundHitCount < 7){
                matched6NumbersCount++;
            }
            if (playerSelectedCount == 6){
                spot6SelectedCount++;
            }
            if(playerSelectedCount == 7 && roundHitCount > 2 && roundHitCount < 8){
                matched7SelectedCount++;
            }
            if (playerSelectedCount == 7){
                spot7SelectedCount++;
            }
            if(playerSelectedCount == 8 && roundHitCount > 3 && roundHitCount < 9){
                matched8SelectedCount++;
            }
            if (playerSelectedCount == 8){
                spot8SelectedCount++;
            }

            if(playerSelectedCount == 9 && roundHitCount > 3 && roundHitCount < 10){
                matched9SelectedCount++;
            }
            if (playerSelectedCount == 9) {
                spot9SelectedCount++;
            }
            if(playerSelectedCount == 10 && roundHitCount > 3 && roundHitCount < 11){
                matched10SelectedCount++;
            }
            if (playerSelectedCount == 10){
                spot10SelectedCount++;
            }

        }


        return new RtpResult()
                .setMaxWinAmount(maxWin)
                .setHitCount(totalHitCount)
                .setWinAmount(totalWins)
                .setMatchedNumbersCount(matched2NumbersCount)
                .setNumberOfSpots(spot2SelectedCount)
                .setSpot3SelectedCount(spot3SelectedCount)
                .setMatched3NumbersCount(matched3NumbersCount)
                .setSpot4SelectedCount(spot4SelectedCount)
                .setMatched4NumbersCount(matched4NumbersCount)
                .setSpot5SelectedCount(spot5SelectedCount)
                .setMatched5NumbersCount(matched5NumbersCount)
                .setSpot6SelectedCount(spot6SelectedCount)
                .setMatched6NumbersCount(matched6NumbersCount)
                .setSpot7SelectedCount(spot7SelectedCount)
                .setMatched7SelectedCount(matched7SelectedCount)
                .setSpot8SelectedCount(spot8SelectedCount)
                .setMatched8SelectedCount(matched8SelectedCount)
                .setSpot9SelectedCount(spot9SelectedCount)
                .setMatched9SelectedCount(matched9SelectedCount)
                .setSpot10SelectedCount(spot10SelectedCount)
                .setMatched10SelectedCount(matched10SelectedCount);

    }
}


