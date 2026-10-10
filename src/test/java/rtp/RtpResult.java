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


}
