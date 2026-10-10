package rtp;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class RTPDataToPrint {

    private BigDecimal totalWins = BigDecimal.ZERO;
    private int totalHitCount = 0;
    private double maxWinAmount = 0;
    private int totalHits = 0;
    private int totalSpotSelectedCount = 0;
}
