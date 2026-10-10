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

    private int spot2MatchedCount;
    private int hitCount;
    private double maxWinAmount;

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


}
