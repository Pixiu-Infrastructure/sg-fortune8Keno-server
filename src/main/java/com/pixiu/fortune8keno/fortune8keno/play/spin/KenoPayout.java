package com.pixiu.fortune8keno.fortune8keno.play.spin;

import java.math.BigDecimal;
import java.util.Map;

public class KenoPayout {
    Map<Integer,Double> payoutTable;

    public KenoPayout(Map<Integer, Double> payoutTable) {
        this.payoutTable = payoutTable;
    }

    public BigDecimal calculateWinningAmount(BigDecimal stake, int matchedNumbers) {
        if (payoutTable.containsKey(matchedNumbers)) {
            return stake.multiply(BigDecimal.valueOf(payoutTable.get(matchedNumbers)));
        }
        return BigDecimal.ZERO; // No payout for unmatched numbers
    }
}
