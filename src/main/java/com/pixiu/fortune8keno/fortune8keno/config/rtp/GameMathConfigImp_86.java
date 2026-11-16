package com.pixiu.fortune8keno.fortune8keno.config.rtp;


import com.pixiu.fortune8keno.fortune8keno.play.spin.KenoPayout;


import java.util.Map;


public class GameMathConfigImp_86 extends GameMathConfigSeparate {

    @Override
    public String createTheoreticalRtp() {
        return "86.0";
    }

    @Override
    protected Map<Integer, KenoPayout> createKenoPayouts() {

        return Map.of(
                2, new KenoPayout(Map.of(
                        1, 0.0,
                        2, 8.5)),
                3, new KenoPayout(Map.of(
                        1, 0.0,
                        2, 1.0,
                        3, 22.0)),
                4, new KenoPayout(Map.of(
                        1, 0.0,
                        2, 1.0,
                        3, 3.0,
                        4, 32.0)),
                5, new KenoPayout(Map.of(
                        1, 0.0,
                        2, 0.0,
                        3, 2.0,
                        4, 15.0,
                        5, 46.0)),
                6, new KenoPayout(Map.of(
                        1, 0.0,
                        2, 0.0,
                        3, 1.0,
                        4, 6.0,
                        5, 16.0,
                        6, 125.0)),
                7, new KenoPayout(Map.of(
                        1, 0.0,
                        2, 0.0,
                        3, 1.0,
                        4, 2.0,
                        5, 6.0,
                        6, 40.0,
                        7, 250.0)),
                8, new KenoPayout(Map.of(
                        1, 0.0,
                        2, 0.0,
                        3, 0.0,
                        4, 2.0,
                        5, 4.0,
                        6, 24.0,
                        7, 155.0,
                        8, 500.0)),
                9, new KenoPayout(Map.of(
                        1, 0.0,
                        2, 0.0,
                        3, 0.0,
                        4, 1.0,
                        5, 3.0,
                        6, 11.0,
                        7, 44.0,
                        8, 250.0,
                        9, 800.0)),
                10, new KenoPayout(Map.of(
                        1, 0.0,
                        2, 0.0,
                        3, 0.0,
                        4, 1.0,
                        5, 2.0,
                        6, 4.0,
                        7, 8.0,
                        8, 62.0,
                        9, 300.0,
                        10, 1200.0))

        );
    }


}
