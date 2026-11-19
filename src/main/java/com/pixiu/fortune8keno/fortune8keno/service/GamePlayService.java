package com.pixiu.fortune8keno.fortune8keno.service;


import com.pixiu.fortune8keno.fortune8keno.config.GameConfigService;
import com.pixiu.fortune8keno.fortune8keno.dto.play.PlayRequest;
import com.pixiu.fortune8keno.fortune8keno.play.Command;
import com.pixiu.fortune8keno.fortune8keno.play.CommandHandler;
import com.pixiu.fortune8keno.fortune8keno.play.PlayRequestParameters;
import com.pixiu.fortune8keno.fortune8keno.results.SpinResult;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Component
@AllArgsConstructor
public class GamePlayService {


    private final GameConfigService gameConfigService;
    private final Command command;


    public SpinResult play(PlayRequest playRequest, BigDecimal rtpVariant) {
        CommandHandler commandHandler = command.getGameHandler(playRequest.getCommand().getAction());
//        PlayerState playerState = playRequest.getState();
        PlayRequestParameters parameters = PlayRequestParameters.builder()
                .stake(getStake(playRequest))
                .selectedNumbers(getSelectedNumbers(playRequest))
                .gameConfiguration(gameConfigService.getConfig(rtpVariant))
                .action(playRequest.getCommand().getAction())
                .state(playRequest.getState())
                .buyBonusCommand(command.isBuyBonusCommand(playRequest.getCommand().getAction()))
                .command(playRequest.getCommand())
                .build();
        
        return commandHandler.play(parameters);
    }

    private BigDecimal getStake(PlayRequest playRequest) {
        boolean isBuyBonusCommand = command.isBuyBonusCommand(playRequest.getCommand().getAction());

//        if(isBuyBonusCommand){
//            int ratioToDivide = Command.getBuyBonusRatio(playRequest.getCommand());
//            return new Stake()
//                    .setCoinBet(playRequest.getStakeValue().getCoinBet())
//                    .setCurrency(playRequest.getStakeValue().getCurrency())
//                    .setCashBet(playRequest.getStakeAmount().divide(BigDecimal.valueOf(ratioToDivide), MathConstant.defaultMathContext));
//        }

        return playRequest.getStakeAmount();
    }

    private Set<Integer> getSelectedNumbers(PlayRequest playRequest){
        if(playRequest.getCommand().getSelectedNumbers() == null){
            return new HashSet<>();
        }
        return playRequest.getCommand().getSelectedNumbers();
    }



}
