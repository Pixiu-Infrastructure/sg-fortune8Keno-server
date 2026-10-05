package rtp;


import com.pixiu.fortune8keno.fortune8keno.config.GameConfigService;
import com.pixiu.fortune8keno.fortune8keno.config.GameEngineProperties;
import com.pixiu.fortune8keno.fortune8keno.config.rtp.GameMathConfigCommon;
import com.pixiu.fortune8keno.fortune8keno.play.Command;
import com.pixiu.fortune8keno.fortune8keno.play.basegame.BaseCommand;
import com.pixiu.fortune8keno.fortune8keno.play.basegame.BaseGame;
import com.pixiu.fortune8keno.fortune8keno.play.spin.SpinGame;
import com.pixiu.fortune8keno.fortune8keno.service.GamePlayService;
import com.pixiu.fortune8keno.fortune8keno.spin.SpinGameResponseMapper;
import org.springframework.boot.info.GitProperties;

import java.util.Properties;
import java.util.Random;

public class RtpSetUp {

    public static GamePlayService createGamePlayService() throws IllegalAccessException {
        SpinGameResponseMapper spinGameResponseMapper = new SpinGameResponseMapper();

        GameMathConfigCommon gameMathConfigCommon = new GameMathConfigCommon();
        GameConfigService gameConfigService = new GameConfigService("1.0.0", new GitProperties(new Properties()), gameMathConfigCommon);

        GameEngineProperties gameEngineProperties = new GameEngineProperties();
//        RngClient rngClient = new RngClient();
       SpinGame spinGame = new SpinGame(null, gameEngineProperties);
        BaseGame baseGame = new BaseGame(spinGame);
        BaseCommand baseCommand = new BaseCommand(spinGameResponseMapper, baseGame);
        Command command = new Command(baseCommand);

        ;




        return new GamePlayService(gameConfigService, command);
    }
}
