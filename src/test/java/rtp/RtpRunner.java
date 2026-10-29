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
        int matched1NumberCount_spot2 = 0;
        int matched0NumberCount_spot2 = 0;

         int matched0NumberCount_spot3 = 0;
         int matched1NumberCount_spot3 = 0;
         int matched2NumberCount_spot3 = 0;
         int matched3NumbersCount_spot3 = 0;
        int spot3SelectedCount = 0;
        int matched3NumbersCount = 0;

         int matched_0_NumberCount_spot4 = 0;
         int matched_1_NumberCount_spot4 = 0;
         int matched_2_NumberCount_spot4 = 0;
         int matched_3_NumberCount_spot4 = 0;
         int matched_4_NumberCount_spot4 = 0;
        int spot4SelectedCount = 0;
        int matched4NumbersCount = 0;

        int matched_0_NumberCount_spot5 = 0;
        int matched_1_NumberCount_spot5 = 0;
        int matched_2_NumberCount_spot5 = 0;
        int matched_3_NumberCount_spot5 = 0;
        int matched_4_NumberCount_spot5 = 0;
        int matched_5_NumberCount_spot5 = 0;
        int spot5SelectedCount = 0;
        int matched5NumbersCount = 0;

        int matched_0_NumberCount_spot6 = 0;
        int matched_1_NumberCount_spot6 = 0;
        int matched_2_NumberCount_spot6 = 0;
        int matched_3_NumberCount_spot6 = 0;
        int matched_4_NumberCount_spot6 = 0;
        int matched_5_NumberCount_spot6 = 0;
        int matched_6_NumberCount_spot6 = 0;
        int spot6SelectedCount = 0;
        int matched6NumbersCount = 0;

        int matched_0_NumberCount_spot7 = 0;
        int matched_1_NumberCount_spot7 = 0;
        int matched_2_NumberCount_spot7 = 0;
        int matched_3_NumberCount_spot7 = 0;
        int matched_4_NumberCount_spot7 = 0;
        int matched_5_NumberCount_spot7 = 0;
        int matched_6_NumberCount_spot7 = 0;
        int matched_7_NumberCount_spot7 = 0;
        int spot7SelectedCount = 0;
        int matched7SelectedCount = 0;

        int matched_0_NumberCount_spot8 = 0;
        int matched_1_NumberCount_spot8 = 0;
        int matched_2_NumberCount_spot8 = 0;
        int matched_3_NumberCount_spot8 = 0;
        int matched_4_NumberCount_spot8 = 0;
        int matched_5_NumberCount_spot8 = 0;
        int matched_6_NumberCount_spot8 = 0;
        int matched_7_NumberCount_spot8 = 0;
        int matched_8_NumberCount_spot8 = 0;
        int spot8SelectedCount = 0;
        int matched8SelectedCount = 0;

        int matched_0_NumberCount_spot9 = 0;
        int matched_1_NumberCount_spot9 = 0;
        int matched_2_NumberCount_spot9 = 0;
        int matched_3_NumberCount_spot9 = 0;
        int matched_4_NumberCount_spot9 = 0;
        int matched_5_NumberCount_spot9 = 0;
        int matched_6_NumberCount_spot9 = 0;
        int matched_7_NumberCount_spot9 = 0;
        int matched_8_NumberCount_spot9 = 0;
        int matched_9_NumberCount_spot9 = 0;
        int spot9SelectedCount = 0;
        int matched9SelectedCount = 0;

        int matched_0_NumberCount_spot10 = 0;
        int matched_1_NumberCount_spot10 = 0;
        int matched_2_NumberCount_spot10 = 0;
        int matched_3_NumberCount_spot10 = 0;
        int matched_4_NumberCount_spot10 = 0;
        int matched_5_NumberCount_spot10 = 0;
        int matched_6_NumberCount_spot10 = 0;
        int matched_7_NumberCount_spot10 = 0;
        int matched_8_NumberCount_spot10 = 0;
        int matched_9_NumberCount_spot10 = 0;
        int matched_10_NumberCount_spot10 = 0;
        int spot10SelectedCount = 0;
        int matched10SelectedCount = 0;

        BigDecimal totalWinSpot2 =  BigDecimal.ZERO;
        BigDecimal totalWinSpot3 = BigDecimal.ZERO;
        BigDecimal totalWinSpot4 = BigDecimal.ZERO;
        BigDecimal totalWinSpot5 = BigDecimal.ZERO;
        BigDecimal totalWinSpot6 = BigDecimal.ZERO;
        BigDecimal totalWinSpot7 = BigDecimal.ZERO;
        BigDecimal totalWinSpot8 = BigDecimal.ZERO;
        BigDecimal totalWinSpot9 = BigDecimal.ZERO;
        BigDecimal totalWinSpot10 = BigDecimal.ZERO;

        BigDecimal sumOfSquaresSpot2 = BigDecimal.ZERO;
        BigDecimal sumOfSquaresSpot3 = BigDecimal.ZERO;
        BigDecimal sumOfSquaresSpot4 = BigDecimal.ZERO;
        BigDecimal sumOfSquaresSpot5 = BigDecimal.ZERO;
        BigDecimal sumOfSquaresSpot6 = BigDecimal.ZERO;
        BigDecimal sumOfSquaresSpot7 = BigDecimal.ZERO;
        BigDecimal sumOfSquaresSpot8 = BigDecimal.ZERO;
        BigDecimal sumOfSquaresSpot9 = BigDecimal.ZERO;
        BigDecimal sumOfSquaresSpot10 = BigDecimal.ZERO;

        BigDecimal maxWin = BigDecimal.ZERO;


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
            switch (playerSelectedCount) {
                case 2:  sumOfSquaresSpot2  = sumOfSquaresSpot2.add(roundWinSquared);  break;
                case 3:  sumOfSquaresSpot3  = sumOfSquaresSpot3.add(roundWinSquared);  break;
                case 4:  sumOfSquaresSpot4  = sumOfSquaresSpot4.add(roundWinSquared);  break;
                case 5:  sumOfSquaresSpot5  = sumOfSquaresSpot5.add(roundWinSquared);  break;
                case 6:  sumOfSquaresSpot6  = sumOfSquaresSpot6.add(roundWinSquared);  break;
                case 7:  sumOfSquaresSpot7  = sumOfSquaresSpot7.add(roundWinSquared);  break;
                case 8:  sumOfSquaresSpot8  = sumOfSquaresSpot8.add(roundWinSquared);  break;
                case 9:  sumOfSquaresSpot9  = sumOfSquaresSpot9.add(roundWinSquared);  break;
                case 10: sumOfSquaresSpot10 = sumOfSquaresSpot10.add(roundWinSquared); break;
            }
            if(playerSelectedCount == 2 && roundHitCount == 2){
               matched2NumbersCount++;
               totalWinSpot2 = totalWinSpot2.add(roundResult.getWinAmount());
            }
            if(playerSelectedCount == 2 && roundHitCount == 1 && roundResult.getWinAmount().compareTo(BigDecimal.ZERO) == 0){
                matched1NumberCount_spot2++;

            }
            if(playerSelectedCount == 2 && roundHitCount == 0 && roundResult.getWinAmount().compareTo(BigDecimal.ZERO) == 0){
                matched0NumberCount_spot2++;

            }
            if(playerSelectedCount == 2){
                spot2SelectedCount++;
            }
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
            if(playerSelectedCount ==5 && roundHitCount == 0 && roundResult.getWinAmount().compareTo(BigDecimal.ZERO) == 0){
                matched_0_NumberCount_spot5++;
            }
            if(playerSelectedCount == 5 && roundHitCount == 1 && roundResult.getWinAmount().compareTo(BigDecimal.ZERO) == 0){
                matched_1_NumberCount_spot5++;
            }
            if(playerSelectedCount == 5 && roundHitCount == 2 && roundResult.getWinAmount().compareTo(BigDecimal.ZERO) == 0){
                matched_2_NumberCount_spot5++;
            }
            if(playerSelectedCount == 5 && roundHitCount == 3 && roundResult.getWinAmount().compareTo(BigDecimal.ZERO) > 0){
                matched_3_NumberCount_spot5++;
            }
            if(playerSelectedCount == 5 && roundHitCount == 4 && roundResult.getWinAmount().compareTo(BigDecimal.ZERO) > 0){
                matched_4_NumberCount_spot5++;
            }
            if(playerSelectedCount == 5 && roundHitCount == 5 && roundResult.getWinAmount().compareTo(BigDecimal.ZERO) > 0){
                matched_5_NumberCount_spot5++;
            }
            if(playerSelectedCount == 5 && roundHitCount > 2 && roundHitCount < 6){
                matched5NumbersCount++;
                totalWinSpot5 = totalWinSpot5.add(roundResult.getWinAmount());
            }
            if (playerSelectedCount == 5){
                spot5SelectedCount++;
            }

            if(playerSelectedCount == 6 && roundHitCount == 0 && roundResult.getWinAmount().compareTo(BigDecimal.ZERO) == 0){
                matched_0_NumberCount_spot6++;
            }
            if(playerSelectedCount ==6 && roundHitCount == 1 && roundResult.getWinAmount().compareTo(BigDecimal.ZERO) == 0){
                matched_1_NumberCount_spot6++;
            }
            if(playerSelectedCount ==6 && roundHitCount == 2 && roundResult.getWinAmount().compareTo(BigDecimal.ZERO) == 0){
                matched_2_NumberCount_spot6++;
            }
            if(playerSelectedCount ==6 && roundHitCount == 3 && roundResult.getWinAmount().compareTo(BigDecimal.ZERO) > 0){
                matched_3_NumberCount_spot6++;
            }
            if(playerSelectedCount ==6 && roundHitCount == 4 && roundResult.getWinAmount().compareTo(BigDecimal.ZERO) > 0){
                matched_4_NumberCount_spot6++;
            }
            if(playerSelectedCount ==6 && roundHitCount == 5 && roundResult.getWinAmount().compareTo(BigDecimal.ZERO) > 0){
                matched_5_NumberCount_spot6++;
            }
            if(playerSelectedCount ==6 && roundHitCount == 6 && roundResult.getWinAmount().compareTo(BigDecimal.ZERO) > 0){
                matched_6_NumberCount_spot6++;
            }

            if(playerSelectedCount == 6 && roundHitCount > 2 && roundHitCount < 7){
                matched6NumbersCount++;
                totalWinSpot6 = totalWinSpot6.add(roundResult.getWinAmount());
            }
            if (playerSelectedCount == 6){
                spot6SelectedCount++;
            }
            if(playerSelectedCount ==7 && roundHitCount == 0 && roundResult.getWinAmount().compareTo(BigDecimal.ZERO) == 0){
                matched_0_NumberCount_spot7++;
            }
            if(playerSelectedCount ==7 && roundHitCount == 1 && roundResult.getWinAmount().compareTo(BigDecimal.ZERO) == 0){
                matched_1_NumberCount_spot7++;
            }
            if(playerSelectedCount ==7 && roundHitCount == 2 && roundResult.getWinAmount().compareTo(BigDecimal.ZERO) == 0){
                matched_2_NumberCount_spot7++;
            }
            if(playerSelectedCount ==7 && roundHitCount == 3 && roundResult.getWinAmount().compareTo(BigDecimal.ZERO) > 0){
                matched_3_NumberCount_spot7++;
            }
            if(playerSelectedCount ==7 && roundHitCount == 4 && roundResult.getWinAmount().compareTo(BigDecimal.ZERO) > 0){
                matched_4_NumberCount_spot7++;
            }
            if(playerSelectedCount ==7 && roundHitCount == 5 && roundResult.getWinAmount().compareTo(BigDecimal.ZERO) > 0){
                matched_5_NumberCount_spot7++;
            }
            if(playerSelectedCount ==7 && roundHitCount == 6 && roundResult.getWinAmount().compareTo(BigDecimal.ZERO) > 0){
                matched_6_NumberCount_spot7++;
            }
            if(playerSelectedCount ==7 && roundHitCount == 7 && roundResult.getWinAmount().compareTo(BigDecimal.ZERO) > 0){
                matched_7_NumberCount_spot7++;
            }
            if(playerSelectedCount == 7 && roundHitCount > 2 && roundHitCount < 8){
                matched7SelectedCount++;
                totalWinSpot7 = totalWinSpot7.add(roundResult.getWinAmount());
            }
            if (playerSelectedCount == 7){
                spot7SelectedCount++;
            }
            if(playerSelectedCount == 8 && roundHitCount == 0 && roundResult.getWinAmount().compareTo(BigDecimal.ZERO) == 0){
                matched_0_NumberCount_spot8++;
            }
            if(playerSelectedCount == 8 && roundHitCount == 1 && roundResult.getWinAmount().compareTo(BigDecimal.ZERO) == 0){
                matched_1_NumberCount_spot8++;
            }
            if(playerSelectedCount == 8 && roundHitCount == 2 && roundResult.getWinAmount().compareTo(BigDecimal.ZERO) == 0){
                matched_2_NumberCount_spot8++;
            }
            if(playerSelectedCount == 8 && roundHitCount == 3 && roundResult.getWinAmount().compareTo(BigDecimal.ZERO) == 0){
                matched_3_NumberCount_spot8++;
            }
            if(playerSelectedCount == 8 && roundHitCount == 4 && roundResult.getWinAmount().compareTo(BigDecimal.ZERO) > 0){
                matched_4_NumberCount_spot8++;
            }
            if(playerSelectedCount == 8 && roundHitCount == 5 && roundResult.getWinAmount().compareTo(BigDecimal.ZERO) > 0){
                matched_5_NumberCount_spot8++;
            }
            if(playerSelectedCount == 8 && roundHitCount == 6 && roundResult.getWinAmount().compareTo(BigDecimal.ZERO) > 0){
                matched_6_NumberCount_spot8++;
            }
            if(playerSelectedCount == 8 && roundHitCount == 7 && roundResult.getWinAmount().compareTo(BigDecimal.ZERO) > 0){
                matched_7_NumberCount_spot8++;
            }
            if(playerSelectedCount == 8 && roundHitCount == 8 && roundResult.getWinAmount().compareTo(BigDecimal.ZERO) > 0){
                matched_8_NumberCount_spot8++;
            }
            if(playerSelectedCount == 8 && roundHitCount > 3 && roundHitCount < 9){
                matched8SelectedCount++;
                totalWinSpot8 = totalWinSpot8.add(roundResult.getWinAmount());
            }
            if (playerSelectedCount == 8){
                spot8SelectedCount++;
            }

            if(playerSelectedCount == 9 && roundHitCount == 0 && roundResult.getWinAmount().compareTo(BigDecimal.ZERO) == 0) {
                matched_0_NumberCount_spot9++;
            }
            if(playerSelectedCount == 9 && roundHitCount == 1 && roundResult.getWinAmount().compareTo(BigDecimal.ZERO) == 0){
                matched_1_NumberCount_spot9++;
            }
            if(playerSelectedCount == 9 && roundHitCount == 2 && roundResult.getWinAmount().compareTo(BigDecimal.ZERO) == 0){
                matched_2_NumberCount_spot9++;
            }
            if(playerSelectedCount == 9 && roundHitCount == 3 && roundResult.getWinAmount().compareTo(BigDecimal.ZERO) == 0){
                matched_3_NumberCount_spot9++;
            }
            if(playerSelectedCount == 9 && roundHitCount == 4 && roundResult.getWinAmount().compareTo(BigDecimal.ZERO) > 0){
                matched_4_NumberCount_spot9++;
            }
            if(playerSelectedCount == 9 && roundHitCount == 5 && roundResult.getWinAmount().compareTo(BigDecimal.ZERO) > 0){
                matched_5_NumberCount_spot9++;
            }
            if(playerSelectedCount == 9 && roundHitCount == 6 && roundResult.getWinAmount().compareTo(BigDecimal.ZERO) > 0){
                matched_6_NumberCount_spot9++;
            }
            if(playerSelectedCount == 9 && roundHitCount == 7 && roundResult.getWinAmount().compareTo(BigDecimal.ZERO) > 0){
                matched_7_NumberCount_spot9++;
            }
            if(playerSelectedCount == 9 && roundHitCount == 8 && roundResult.getWinAmount().compareTo(BigDecimal.ZERO) > 0){
                matched_8_NumberCount_spot9++;
            }

            if(playerSelectedCount == 9 && roundHitCount == 9 && roundResult.getWinAmount().compareTo(BigDecimal.ZERO) > 0){
                matched_9_NumberCount_spot9++;
            }

            if(playerSelectedCount == 9 && roundHitCount > 3 && roundHitCount < 10){
                matched9SelectedCount++;
                totalWinSpot9 = totalWinSpot9.add(roundResult.getWinAmount());
            }
            if (playerSelectedCount == 9) {
                spot9SelectedCount++;
            }


            if(playerSelectedCount == 10 && roundHitCount == 0 && roundResult.getWinAmount().compareTo(BigDecimal.ZERO) == 0) {
                matched_0_NumberCount_spot10++;
            }
            if(playerSelectedCount == 10 && roundHitCount == 1 && roundResult.getWinAmount().compareTo(BigDecimal.ZERO) == 0){
                matched_1_NumberCount_spot10++;
            }
            if(playerSelectedCount == 10 && roundHitCount == 2 && roundResult.getWinAmount().compareTo(BigDecimal.ZERO) == 0){
                matched_2_NumberCount_spot10++;
            }
            if(playerSelectedCount == 10 && roundHitCount == 3 && roundResult.getWinAmount().compareTo(BigDecimal.ZERO) == 0){
                matched_3_NumberCount_spot10++;
            }
            if(playerSelectedCount == 10 && roundHitCount == 4 && roundResult.getWinAmount().compareTo(BigDecimal.ZERO) > 0){
                matched_4_NumberCount_spot10++;
            }
            if(playerSelectedCount == 10 && roundHitCount == 5 && roundResult.getWinAmount().compareTo(BigDecimal.ZERO) > 0){
                matched_5_NumberCount_spot10++;
            }
            if(playerSelectedCount == 10 && roundHitCount == 6 && roundResult.getWinAmount().compareTo(BigDecimal.ZERO) > 0){
                matched_6_NumberCount_spot10++;
            }
            if(playerSelectedCount == 10 && roundHitCount == 7 && roundResult.getWinAmount().compareTo(BigDecimal.ZERO) > 0){
                matched_7_NumberCount_spot10++;
            }
            if(playerSelectedCount == 10 && roundHitCount == 8 && roundResult.getWinAmount().compareTo(BigDecimal.ZERO) > 0){
                matched_8_NumberCount_spot10++;
            }

            if(playerSelectedCount == 10 && roundHitCount == 9 && roundResult.getWinAmount().compareTo(BigDecimal.ZERO) > 0){
                matched_9_NumberCount_spot10++;
            }

            if(playerSelectedCount == 10 && roundHitCount == 10 && roundResult.getWinAmount().compareTo(BigDecimal.ZERO) > 0){
                matched_10_NumberCount_spot10++;
            }
            if(playerSelectedCount == 10 && roundHitCount > 3 && roundHitCount < 11){
                matched10SelectedCount++;
                totalWinSpot10 = totalWinSpot10.add(roundResult.getWinAmount());
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
                .setMatched10SelectedCount(matched10SelectedCount)
                .setTotalWinSpot2(totalWinSpot2)
                .setTotalWinSpot3(totalWinSpot3)
                .setTotalWinSpot4(totalWinSpot4)
                .setTotalWinSpot5(totalWinSpot5)
                .setTotalWinSpot6(totalWinSpot6)
                .setTotalWinSpot7(totalWinSpot7)
                .setTotalWinSpot8(totalWinSpot8)
                .setTotalWinSpot9(totalWinSpot9)
                .setTotalWinSpot10(totalWinSpot10)
                .setMatched0NumberCount_spot2(matched0NumberCount_spot2)
                .setMatched1NumberCount_spot2(matched1NumberCount_spot2)
                .setMatched_0_NumberCount_spot3(matched0NumberCount_spot3)
                .setMatched_1_NumberCount_spot3(matched1NumberCount_spot3)
                .setMatched_2_NumberCount_spot3(matched2NumberCount_spot3)
                .setMatched_3_NumberCount_spot3(matched3NumbersCount_spot3)
                .setMatched_0_NumberCount_spot4(matched_0_NumberCount_spot4)
                .setMatched_1_NumberCount_spot4(matched_1_NumberCount_spot4)
                .setMatched_2_NumberCount_spot4(matched_2_NumberCount_spot4)
                .setMatched_3_NumberCount_spot4(matched_3_NumberCount_spot4)
                .setMatched_4_NumberCount_spot4(matched_4_NumberCount_spot4)
                .setMatched_0_NumberCount_spot5(matched_0_NumberCount_spot5)
                .setMatched_1_NumberCount_spot5(matched_1_NumberCount_spot5)
                .setMatched_2_NumberCount_spot5(matched_2_NumberCount_spot5)
                .setMatched_3_NumberCount_spot5(matched_3_NumberCount_spot5)
                .setMatched_4_NumberCount_spot5(matched_4_NumberCount_spot5)
                .setMatched_5_NumberCount_spot5(matched_5_NumberCount_spot5)
                .setMatched_0_NumberCount_spot6(matched_0_NumberCount_spot6)
                .setMatched_1_NumberCount_spot6(matched_1_NumberCount_spot6)
                .setMatched_2_NumberCount_spot6(matched_2_NumberCount_spot6)
                .setMatched_3_NumberCount_spot6(matched_3_NumberCount_spot6)
                .setMatched_4_NumberCount_spot6(matched_4_NumberCount_spot6)
                .setMatched_5_NumberCount_spot6(matched_5_NumberCount_spot6)
                .setMatched_6_NumberCount_spot6(matched_6_NumberCount_spot6)
                .setMatched_0_NumberCount_spot7(matched_0_NumberCount_spot7)
                .setMatched_1_NumberCount_spot7(matched_1_NumberCount_spot7)
                .setMatched_2_NumberCount_spot7(matched_2_NumberCount_spot7)
                .setMatched_3_NumberCount_spot7(matched_3_NumberCount_spot7)
                .setMatched_4_NumberCount_spot7(matched_4_NumberCount_spot7)
                .setMatched_5_NumberCount_spot7(matched_5_NumberCount_spot7)
                .setMatched_6_NumberCount_spot7(matched_6_NumberCount_spot7)
                .setMatched_7_NumberCount_spot7(matched_7_NumberCount_spot7)
                .setMatched_0_NumberCount_spot8(matched_0_NumberCount_spot8)
                .setMatched_1_NumberCount_spot8(matched_1_NumberCount_spot8)
                .setMatched_2_NumberCount_spot8(matched_2_NumberCount_spot8)
                .setMatched_3_NumberCount_spot8(matched_3_NumberCount_spot8)
                .setMatched_4_NumberCount_spot8(matched_4_NumberCount_spot8)
                .setMatched_5_NumberCount_spot8(matched_5_NumberCount_spot8)
                .setMatched_6_NumberCount_spot8(matched_6_NumberCount_spot8)
                .setMatched_7_NumberCount_spot8(matched_7_NumberCount_spot8)
                .setMatched_8_NumberCount_spot8(matched_8_NumberCount_spot8)

                .setMatched_0_NumberCount_spot9(matched_0_NumberCount_spot9)
                .setMatched_1_NumberCount_spot9(matched_1_NumberCount_spot9)
                .setMatched_2_NumberCount_spot9(matched_2_NumberCount_spot9)
                .setMatched_3_NumberCount_spot9(matched_3_NumberCount_spot9)
                .setMatched_4_NumberCount_spot9(matched_4_NumberCount_spot9)
                .setMatched_5_NumberCount_spot9(matched_5_NumberCount_spot9)
                .setMatched_6_NumberCount_spot9(matched_6_NumberCount_spot9)
                .setMatched_7_NumberCount_spot9(matched_7_NumberCount_spot9)
                .setMatched_8_NumberCount_spot9(matched_8_NumberCount_spot9)
                .setMatched_9_NumberCount_spot9(matched_9_NumberCount_spot9)


                .setMatched_0_NumberCount_spot10(matched_0_NumberCount_spot10)
                .setMatched_1_NumberCount_spot10(matched_1_NumberCount_spot10)
                .setMatched_2_NumberCount_spot10(matched_2_NumberCount_spot10)
                .setMatched_3_NumberCount_spot10(matched_3_NumberCount_spot10)
                .setMatched_4_NumberCount_spot10(matched_4_NumberCount_spot10)
                .setMatched_5_NumberCount_spot10(matched_5_NumberCount_spot10)
                .setMatched_6_NumberCount_spot10(matched_6_NumberCount_spot10)
                .setMatched_7_NumberCount_spot10(matched_7_NumberCount_spot10)
                .setMatched_8_NumberCount_spot10(matched_8_NumberCount_spot10)
                .setMatched_9_NumberCount_spot10(matched_9_NumberCount_spot10)
                .setMatched_10_NumberCount_spot10(matched_10_NumberCount_spot10)
                .setSumOfSquaresSpot2(sumOfSquaresSpot2)
                .setSumOfSquaresSpot3(sumOfSquaresSpot3)
                .setSumOfSquaresSpot4(sumOfSquaresSpot4)
                .setSumOfSquaresSpot5(sumOfSquaresSpot5)
                .setSumOfSquaresSpot6(sumOfSquaresSpot6)
                .setSumOfSquaresSpot7(sumOfSquaresSpot7)
                .setSumOfSquaresSpot8(sumOfSquaresSpot8)
                .setSumOfSquaresSpot9(sumOfSquaresSpot9)
                .setSumOfSquaresSpot10(sumOfSquaresSpot10)
                ;

    }
}


