package rtp;

import com.pixiu.fortune8keno.fortune8keno.dto.play.PlayRequestCommandData;
import com.pixiu.fortune8keno.fortune8keno.service.GamePlayService;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Callable;


@Builder
@Slf4j
@Data
@Accessors(chain = true)
public class RtpRunnerSimple_Spot9 implements Callable<RtpResult> {

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
    private PlayRequestCommandData initialCommand = new PlayRequestCommandData();

    @Builder.Default
    private BigDecimal stake = new BigDecimal(1);

    private int totalRuns;


    public RtpResult call() throws Exception {

        if (gamePlayService == null) gamePlayService = RtpSetUp.createGamePlayService();
        initialCommand.setAction("start");
        initialCommand.setStakeAmount(stake);

        BigDecimal totalWins = BigDecimal.ZERO;
        BigDecimal totalWinSpot9 = BigDecimal.ZERO;
        int totalHitCount = 0;

        BigDecimal maxWin = BigDecimal.ZERO;
        BigDecimal sumOfSquaresSpot9 = BigDecimal.ZERO;

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
        int matched9NumbersCount = 0;
        int multiplierTriggeredCount = 0;
        int awardGreaterThanEqualTo10 = 0;
        int awardGreaterThanEqualTo20 = 0;
        int awardGreaterThanEqualTo50 = 0;
        int awardGreaterThanEqualTo100 = 0;
        int awardGreaterThanEqualTo1000 = 0;
        Map<Long, Long> winAmountFrequency = new HashMap<>();


        for (int i = 1; i <= totalRuns; i++) {

            RoundResult roundResult = new RtpTask().setStakeValue(stake)
                    .setGamePlayService(gamePlayService)
                    .setExpectedRTP(expectedRTP)
                    .setSaveResults(saveResults)
                    .setRandom(random)
                    .call();

            totalWins = totalWins.add(roundResult.getWinAmount());
            BigDecimal roundWinAmount = roundResult.getWinAmount();
            int playerSelectedCount = roundResult.getPlayerSelectedNumberCount();
            int roundHitCount = roundResult.getHitCount();

            BigDecimal roundWinSquared = roundWinAmount.multiply(roundWinAmount);
            sumOfSquaresSpot9 = sumOfSquaresSpot9.add(roundWinSquared);

            if (roundWinAmount.compareTo(BigDecimal.ZERO) > 0) {
                totalHitCount = totalHitCount + 1;
            }
            if (roundWinAmount.compareTo(maxWin) > 0) {
                maxWin = roundWinAmount;
            }

            // For spot 9: 0–3 hits = no win, 4–9 hits = win
            if (playerSelectedCount == 9 && roundHitCount == 0 && roundWinAmount.compareTo(BigDecimal.ZERO) == 0) {
                matched_0_NumberCount_spot9++;
            }
            if (playerSelectedCount == 9 && roundHitCount == 1 && roundWinAmount.compareTo(BigDecimal.ZERO) == 0) {
                matched_1_NumberCount_spot9++;
            }
            if (playerSelectedCount == 9 && roundHitCount == 2 && roundWinAmount.compareTo(BigDecimal.ZERO) == 0) {
                matched_2_NumberCount_spot9++;
            }
            if (playerSelectedCount == 9 && roundHitCount == 3 && roundWinAmount.compareTo(BigDecimal.ZERO) == 0) {
                matched_3_NumberCount_spot9++;
            }
            if (playerSelectedCount == 9 && roundHitCount == 4 && roundWinAmount.compareTo(BigDecimal.ZERO) > 0) {
                matched_4_NumberCount_spot9++;
            }
            if (playerSelectedCount == 9 && roundHitCount == 5 && roundWinAmount.compareTo(BigDecimal.ZERO) > 0) {
                matched_5_NumberCount_spot9++;
            }
            if (playerSelectedCount == 9 && roundHitCount == 6 && roundWinAmount.compareTo(BigDecimal.ZERO) > 0) {
                matched_6_NumberCount_spot9++;
            }
            if (playerSelectedCount == 9 && roundHitCount == 7 && roundWinAmount.compareTo(BigDecimal.ZERO) > 0) {
                matched_7_NumberCount_spot9++;
            }
            if (playerSelectedCount == 9 && roundHitCount == 8 && roundWinAmount.compareTo(BigDecimal.ZERO) > 0) {
                matched_8_NumberCount_spot9++;
            }
            if (playerSelectedCount == 9 && roundHitCount == 9 && roundWinAmount.compareTo(BigDecimal.ZERO) > 0) {
                matched_9_NumberCount_spot9++;
            }

            if (playerSelectedCount == 9 && roundHitCount > 3 && roundHitCount < 10) {
                matched9NumbersCount++;
                totalWinSpot9 = totalWinSpot9.add(roundWinAmount);
            }
            if (playerSelectedCount == 9) {
                spot9SelectedCount++;
            }

            if (roundWinAmount.compareTo(new BigDecimal(10)) >= 0) {
                awardGreaterThanEqualTo10++;
            }
            if (roundWinAmount.compareTo(new BigDecimal(20)) >= 0) {
                awardGreaterThanEqualTo20++;
            }
            if (roundWinAmount.compareTo(new BigDecimal(50)) >= 0) {
                awardGreaterThanEqualTo50++;
            }
            if (roundWinAmount.compareTo(new BigDecimal(100)) >= 0) {
                awardGreaterThanEqualTo100++;
            }
            if (roundWinAmount.compareTo(new BigDecimal(1000)) >= 0) {
                awardGreaterThanEqualTo1000++;
            }
            if (playerSelectedCount == 9 && roundResult.isMultiplierTriggered()) {
                multiplierTriggeredCount++;
            }
            winAmountFrequency.merge(roundWinAmount.longValue(), 1L, Long::sum);
        }

        return new RtpResult()
                .setMaxWinAmount(maxWin)
                .setHitCount(totalHitCount)
                .setWinAmount(totalWins)
                .setTotalWinSpot9(totalWinSpot9)
                .setSpot9SelectedCount(spot9SelectedCount)
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
                .setMatched9SelectedCount(matched9NumbersCount)
                .setSumOfSquaresSpot9(sumOfSquaresSpot9)
                .setMultiplierTriggeredCount(multiplierTriggeredCount)
                .setAwardGreaterThanEqualTo10(awardGreaterThanEqualTo10)
                .setAwardGreaterThanEqualTo20(awardGreaterThanEqualTo20)
                .setAwardGreaterThanEqualTo50(awardGreaterThanEqualTo50)
                .setAwardGreaterThanEqualTo100(awardGreaterThanEqualTo100)
                .setAwardGreaterThanEqualTo1000(awardGreaterThanEqualTo1000)
                .setWinAmountFrequency(winAmountFrequency)
                ;
    }
}
