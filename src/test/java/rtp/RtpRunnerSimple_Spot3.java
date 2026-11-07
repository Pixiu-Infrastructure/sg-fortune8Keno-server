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
public class RtpRunnerSimple_Spot3 implements Callable<RtpResult> {

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
        int matched0NumberCount_spot3 = 0;
        int matched1NumberCount_spot3 = 0;
        int matched2NumberCount_spot3 = 0;
        int matched3NumbersCount_spot3 = 0;
        int spot3SelectedCount = 0;
        int matched3NumbersCount = 0;

        BigDecimal sumOfSquaresSpot3 = BigDecimal.ZERO;
        BigDecimal maxWin = BigDecimal.ZERO;

        BigDecimal totalWinSpot3 = BigDecimal.ZERO;
        BigDecimal sumOfSquaresSpot2 = BigDecimal.ZERO;

        for (int i = 1; i <= totalRuns; i++) {

            RoundResult roundResult = new RtpTask().setStakeValue(stake)
                    .setGamePlayService(gamePlayService)
                    .setExpectedRTP(expectedRTP)
                    .setSaveResults(saveResults)
//                    .setInitialCommand(initialCommand)
                    .setRandom(random)
                    .call();

            totalWins = totalWins
                    .add(roundResult.getWinAmount());

            int playerSelectedCount = roundResult.getPlayerSelectedNumberCount();
            BigDecimal roundWinAmount = roundResult.getWinAmount();
            int roundHitCount = roundResult.getHitCount();
            if (roundWinAmount.compareTo(BigDecimal.ZERO) > 0) {
                totalHitCount = totalHitCount + 1;
            }
            if(roundWinAmount.compareTo(maxWin) > 0 ){
                maxWin = roundWinAmount;
            }
            BigDecimal roundWinSquared = roundWinAmount.multiply(roundWinAmount);
            sumOfSquaresSpot3  = sumOfSquaresSpot3.add(roundWinSquared);

            if(playerSelectedCount == 3 && roundHitCount == 0 && roundResult.getWinAmount().compareTo(BigDecimal.ZERO) == 0){
                matched0NumberCount_spot3++;
            }
            if(playerSelectedCount == 3 && roundHitCount == 1 && roundResult.getWinAmount().compareTo(BigDecimal.ZERO) == 0){
                matched1NumberCount_spot3++;
            }
            if(playerSelectedCount == 3 && roundHitCount == 2 && roundResult.getWinAmount().compareTo(BigDecimal.ZERO) > 0) {
                matched2NumberCount_spot3++;

            }
            if(playerSelectedCount == 3 && roundHitCount == 3 && roundResult.getWinAmount().compareTo(BigDecimal.ZERO) > 0) {
                matched3NumbersCount_spot3++;
            }

            if(playerSelectedCount == 3 && roundHitCount > 1 && roundHitCount < 4){
                matched3NumbersCount++;
                totalWinSpot3 = totalWinSpot3.add(roundResult.getWinAmount());
            }
            if (playerSelectedCount == 3){
                spot3SelectedCount++;
            }

        }
        return new RtpResult()
                .setMaxWinAmount(maxWin)
                .setHitCount(totalHitCount)
                .setWinAmount(totalWins)
                .setSpot3SelectedCount(spot3SelectedCount)
                .setMatched3NumbersCount(matched3NumbersCount)
                .setSumOfSquaresSpot2(sumOfSquaresSpot2)
                .setMatched_0_NumberCount_spot3(matched0NumberCount_spot3)
                .setMatched_1_NumberCount_spot3(matched1NumberCount_spot3)
                .setMatched_2_NumberCount_spot3(matched2NumberCount_spot3)
                .setMatched_3_NumberCount_spot3(matched3NumbersCount_spot3)
                .setTotalWinSpot3(totalWinSpot3)
                .setSumOfSquaresSpot3(sumOfSquaresSpot3)

                ;

    }
}


