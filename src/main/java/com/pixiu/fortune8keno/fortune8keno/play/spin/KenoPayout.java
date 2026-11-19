package com.pixiu.fortune8keno.fortune8keno.play.spin;

import java.util.List;
import java.util.Map;

public class KenoPayout {
    Map<Integer,Double> payoutTable;

    public KenoPayout(Map<Integer, Double> payoutTable) {
        this.payoutTable = payoutTable;
    }

    public double calculateWinningAmount(int stake, int matchedNumbers) {
        if (payoutTable.containsKey(matchedNumbers)) {
            return (int) (stake * payoutTable.get(matchedNumbers));
        }
        return 0; // No payout for unmatched numbers
    }

    public Map<Integer, Double> getPayoutTable() {
        return payoutTable;
    }
}
