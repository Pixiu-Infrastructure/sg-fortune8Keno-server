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

    int spot3SelectedCount = 0;
    int matched3NumbersCount = 0;

    int spot4SelectedCount = 0;
    int matched4NumbersCount = 0;

    int spot5SelectedCount = 0;
    int matched5NumbersCount = 0;

    int spot6SelectedCount = 0;
    int matched6NumbersCount = 0;
    int spot7SelectedCount = 0;
    int matched7NumbersCount = 0;

    int spot8SelectedCount = 0;
    int matched8NumbersCount = 0;
    int spot9SelectedCount = 0;
    int matched9NumbersCount = 0;
    int spot10SelectedCount = 0;
    int matched10NumbersCount = 0;

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
