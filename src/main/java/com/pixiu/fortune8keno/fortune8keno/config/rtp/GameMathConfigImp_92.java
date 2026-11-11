package com.pixiu.fortune8keno.fortune8keno.config.rtp;


import com.pixiu.fortune8keno.fortune8keno.play.spin.KenoPayout;

import java.util.Map;


public class GameMathConfigImp_92 extends GameMathConfigSeparate {
    @Override
    public String createTheoreticalRtp() {
        return "92.0";
    }

    @Override
    protected Map<Integer, KenoPayout> createKenoPayouts() {
        return Map.of(
                2, new KenoPayout(Map.of(
                        1, 0.0,
                        2, 9.0)),
                3, new KenoPayout(Map.of(
                        1, 0.0,
                        2, 1.0,
                        3, 24.0)),
                4, new KenoPayout(Map.of(
                        1, 0.0,
                        2, 1.0,
                        3, 3.0,
                        4, 40.0)),
                5, new KenoPayout(Map.of(
                        1, 0.0,
                        2, 0.0,
                        3, 2.0,
                        4, 16.0,
                        5, 64.0)),
                6, new KenoPayout(Map.of(
                        1, 0.0,
                        2, 0.0,
                        3, 1.0,
                        4, 6.0,
                        5, 22.0,
                        6, 150.0)),
                7, new KenoPayout(Map.of(
                        1, 0.0,
                        2, 0.0,
                        3, 1.0,
                        4, 2.0,
                        5, 7.0,
                        6, 50.0,
                        7, 375.0)),
                8, new KenoPayout(Map.of(
                        1, 0.0,
                        2, 0.0,
                        3, 0.0,
                        4, 2.0,
                        5, 5.0,
                        6, 24.0,
                        7, 185.0,
                        8, 700.0)),
                9, new KenoPayout(Map.of(
                        1, 0.0,
                        2, 0.0,
                        3, 0.0,
                        4, 1.0,
                        5, 3.0,
                        6, 13.0,
                        7, 52.0,
                        8, 300.0,
                        9, 1000.0)),
                10, new KenoPayout(Map.of(
                        1, 0.0,
                        2, 0.0,
                        3, 0.0,
                        4, 1.0,
                        5, 2.0,
                        6, 4.5,
                        7, 14.0,
                        8, 75.0,
                        9, 350.0,
                        10, 1500.0))

        );
    }


}
