package com.pixiu.fortune8keno.fortune8keno.config.rtp;

import com.pixiu.fortune8keno.fortune8keno.play.spin.KenoPayout;

import java.util.Map;

public class GameMathConfigImp_89 extends GameMathConfigSeparate {
    @Override
    public String createTheoreticalRtp() {
        return "89.0";
    }

    @Override
    protected Map<Integer, KenoPayout> createKenoPayouts() {
        return Map.of(
                2, new KenoPayout(Map.of(1, 0.0, 2, 8.7)),
                3, new KenoPayout(Map.of(1, 0.0, 2, 1.0, 3, 23.0)),
                4, new KenoPayout(Map.of(1, 0.0, 2, 1.0, 3, 3.0, 4, 36.0)),
                5, new KenoPayout(Map.of(1, 0.0, 2, 0.0, 3, 2.0, 4, 15.0, 5, 62.0)),
                6, new KenoPayout(Map.of(1, 0.0, 2, 0.0, 3, 1.0, 4, 6.0, 5, 18.0, 6, 150.0)),
                7, new KenoPayout(Map.of(1, 0.0, 2, 0.0, 3, 1.0, 4, 2.0, 5, 6.0, 6, 48.0, 7, 350.0)),
                8, new KenoPayout(Map.of(1, 0.0, 2, 0.0, 3, 0.0, 4, 2.0, 5, 5.0, 6, 18.0, 7, 185.0, 8, 700.0)),
                9, new KenoPayout(Map.of(1, 0.0, 2, 0.0, 3, 0.0, 4, 1.0, 5, 3.0, 6, 12.0, 7, 45.0, 8, 300.0, 9, 1000.0)),
                10, new KenoPayout(Map.of(1, 0.0, 2, 0.0, 3, 0.0, 4, 1.0, 5, 2.0, 6, 4.0, 7, 11.0, 8, 80.0, 9, 350.0, 10, 1500.0))

        );
    }
}