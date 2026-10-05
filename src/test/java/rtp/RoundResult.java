package rtp;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class RoundResult {

    private int matchedNumbersCount;
    private double winAmount;
    private boolean multiplierTriggered;
    private int lastServerNum;
    private int multiplier;
    private int numberOfSpots;

    private int spot2MatchedCount;
    private String error;

    public int getSpot2MatchedCount() {
        return spot2MatchedCount;
    }

    public void setSpot2MatchedCount(int spot2MatchedCount) {
        this.spot2MatchedCount = spot2MatchedCount;
    }

    public void setMatchedNumbersCount(int matchedNumbersCount) {
        this.matchedNumbersCount = matchedNumbersCount;
    }

    public void setWinAmount(double winAmount) {
        this.winAmount = winAmount;
    }

    public void setMultiplierTriggered(boolean multiplierTriggered) {
        this.multiplierTriggered = multiplierTriggered;
    }

    public void setLastServerNum(int lastServerNum) {
        this.lastServerNum = lastServerNum;
    }

    public void setMultiplier(int multiplier) {
        this.multiplier = multiplier;
    }

    public void setNumberOfSpots(int numberOfSpots) {
        this.numberOfSpots = numberOfSpots;
    }

    public int getMatchedNumbersCount() {
        return matchedNumbersCount;
    }

    public double getWinAmount() {
        return winAmount;
    }

    public boolean isMultiplierTriggered() {
        return multiplierTriggered;
    }

    public int getLastServerNum() {
        return lastServerNum;
    }

    public int getMultiplier() {
        return multiplier;
    }

    public int getNumberOfSpots() {
        return numberOfSpots;
    }
}
