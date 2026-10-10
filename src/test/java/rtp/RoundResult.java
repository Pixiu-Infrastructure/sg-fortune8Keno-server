package rtp;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class RoundResult {


    private double winAmount;
    private boolean multiplierTriggered;
    private int lastServerNum;
    private int multiplier;
    private int playerSelectedNumberCount;


    private String error;
    private int hitCount;


}
