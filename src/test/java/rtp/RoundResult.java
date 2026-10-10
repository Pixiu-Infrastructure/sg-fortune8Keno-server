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
    private int spot3MatchedCount;
    private int spot3HitCount;

    private int spot4MatchedCount;
    private int spot4HitCount;

    private int spot5MatchedCount;
    private int spot5HitCount;
    private int spot6MatchedCount;
    private int spot6HitCount;
    private int spot7MatchedCount;
    private int spot7HitCount;
    private int spot8MatchedCount;
    private int spot8HitCount;
    private int spot9MatchedCount;
    private int spot9HitCount;
    private int spot10MatchedCount;
    private int spot10HitCount;



}
