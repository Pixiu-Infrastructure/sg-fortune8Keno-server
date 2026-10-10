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
            if (roundResult.getWinAmount() > 0) {
                totalHitCount = totalHitCount + 1;
            }
            if(roundResult.getWinAmount() > maxWin ){
                maxWin = roundResult.getWinAmount();
            }
            if(roundResult.getPlayerSelectedNumberCount() == 2 && roundResult.getHitCount() == 2){
               matched2NumbersCount++;
            }
            if(roundResult.getPlayerSelectedNumberCount() == 2){
                spot2SelectedCount++;
            }

            // System.out.println("Round: " + i + " Win Amount: " + roundResult.getWinAmount() + " Total Wins: " + totalWins);

        }


        return new RtpResult()
                .setMaxWinAmount(maxWin)
                .setHitCount(totalHitCount)
                .setWinAmount(totalWins)
                .setMatchedNumbersCount(matched2NumbersCount)
                .setNumberOfSpots(spot2SelectedCount);

    }
}


