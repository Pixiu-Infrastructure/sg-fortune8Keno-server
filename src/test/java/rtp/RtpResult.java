package rtp;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
@Data
@Accessors(chain = true)
public class RtpResult {

    private int matchedNumbersCount;
    private BigDecimal winAmount;
    private boolean multiplierTriggered;
    private int lastServerNum;
    private int multiplier;
    private int numberOfSpots;

    private int matched0NumberCount_spot2 = 0;
    private int matched1NumberCount_spot2 = 0;

    private int spot2MatchedCount;
    private int hitCount;
    private BigDecimal maxWinAmount;

    private int matched_0_NumberCount_spot3 = 0;
    private int matched_1_NumberCount_spot3 = 0;
    private int matched_2_NumberCount_spot3 = 0;
    private int matched_3_NumberCount_spot3 = 0;
    int spot3SelectedCount = 0;
    int matched3NumbersCount = 0;

    private int matched_0_NumberCount_spot4 = 0;
    private int matched_1_NumberCount_spot4 = 0;
    private int matched_2_NumberCount_spot4 = 0;
    private int matched_3_NumberCount_spot4 = 0;
    private int matched_4_NumberCount_spot4 = 0;
    int spot4SelectedCount = 0;
    int matched4NumbersCount = 0;

    private int spot5SelectedCount = 0;
    private int matched5NumbersCount = 0;
    private int matched_0_NumberCount_spot5 = 0;
    private int matched_1_NumberCount_spot5 = 0;
    private int matched_2_NumberCount_spot5 = 0;
    private int matched_3_NumberCount_spot5 = 0;
    private int matched_4_NumberCount_spot5 = 0;
    private int matched_5_NumberCount_spot5 = 0;

    private int matched_0_NumberCount_spot6 = 0;
    private int matched_1_NumberCount_spot6 = 0;
    private int matched_2_NumberCount_spot6 = 0;
    private int matched_3_NumberCount_spot6 = 0;
    private int matched_4_NumberCount_spot6 = 0;
    private int matched_5_NumberCount_spot6 = 0;
    private int matched_6_NumberCount_spot6 = 0;
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


}
