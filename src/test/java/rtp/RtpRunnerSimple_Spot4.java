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
public class RtpRunnerSimple_Spot4 implements Callable<RtpResult> {

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


        BigDecimal maxWin = BigDecimal.ZERO;

        BigDecimal sumOfSquaresSpot4 = BigDecimal.ZERO;
        int matched_0_NumberCount_spot4 = 0;
        int matched_1_NumberCount_spot4 = 0;
        int matched_2_NumberCount_spot4 = 0;
        int matched_3_NumberCount_spot4 = 0;
        int matched_4_NumberCount_spot4 = 0;
        int spot4SelectedCount = 0;
        int matched4NumbersCount = 0;

        BigDecimal totalWinSpot4 = BigDecimal.ZERO;


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
            sumOfSquaresSpot4  = sumOfSquaresSpot4.add(roundWinSquared);

            if (playerSelectedCount == 4 && roundHitCount == 0 && roundResult.getWinAmount().compareTo(BigDecimal.ZERO) == 0){
                matched_0_NumberCount_spot4++;
            }
            if (playerSelectedCount == 4 && roundHitCount == 1 && roundResult.getWinAmount().compareTo(BigDecimal.ZERO) == 0){
                matched_1_NumberCount_spot4++;
            }
            if (playerSelectedCount == 4 && roundHitCount == 2 && roundResult.getWinAmount().compareTo(BigDecimal.ZERO) > 0){
                matched_2_NumberCount_spot4++;
            }
            if (playerSelectedCount == 4 && roundHitCount == 3 && roundResult.getWinAmount().compareTo(BigDecimal.ZERO) > 0){
                matched_3_NumberCount_spot4++;
            }
            if (playerSelectedCount == 4 && roundHitCount == 4 && roundResult.getWinAmount().compareTo(BigDecimal.ZERO) > 0){
                matched_4_NumberCount_spot4++;
            }
            if(playerSelectedCount == 4 && roundHitCount > 1 && roundHitCount < 5){
                matched4NumbersCount++;
                totalWinSpot4 = totalWinSpot4.add(roundResult.getWinAmount());
            }
            if (playerSelectedCount == 4){
                spot4SelectedCount++;
            }

        }
        return new RtpResult()
                .setMaxWinAmount(maxWin)
                .setHitCount(totalHitCount)
                .setWinAmount(totalWins)
                .setSpot4SelectedCount(spot4SelectedCount)
                .setMatched4NumbersCount(matched4NumbersCount)
                .setTotalWinSpot4(totalWinSpot4)
                .setMatched_0_NumberCount_spot4(matched_0_NumberCount_spot4)
                .setMatched_1_NumberCount_spot4(matched_1_NumberCount_spot4)
                .setMatched_2_NumberCount_spot4(matched_2_NumberCount_spot4)
                .setMatched_3_NumberCount_spot4(matched_3_NumberCount_spot4)
                .setMatched_4_NumberCount_spot4(matched_4_NumberCount_spot4)
                .setSumOfSquaresSpot4(sumOfSquaresSpot4)


                ;

    }
}


