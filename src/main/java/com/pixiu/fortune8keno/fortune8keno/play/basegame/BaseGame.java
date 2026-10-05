package com.pixiu.fortune8keno.fortune8keno.play.basegame;


import com.pixiu.fortune8keno.fortune8keno.config.GameConfiguration;
import com.pixiu.fortune8keno.fortune8keno.play.PlayRequestParameters;
import com.pixiu.fortune8keno.fortune8keno.play.spin.SpinGame;
import com.pixiu.fortune8keno.fortune8keno.results.SpinResult;
import com.pixiu.fortune8keno.fortune8keno.spin.SpinGameResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;



@Component
@RequiredArgsConstructor
public class BaseGame {

    private final SpinGame spinGame;


    public SpinGameResult play(PlayRequestParameters playRequestParameters) {
        GameConfiguration gameConfig = playRequestParameters.getGameConfiguration();

        SpinResult spinResult = spinGame.play(gameConfig,  false,
                playRequestParameters);


        SpinGameResult spinGameResult = new SpinGameResult();

        List<SpinResult> spinResultsList = new ArrayList<>();
        spinResultsList.add(spinResult);
        spinGameResult.setSpinResults(spinResultsList);
        return spinGameResult;
    }


}