package com.pixiu.fortune8keno.fortune8keno.game;

import com.pixiu.fortune8keno.fortune8keno.dto.play.PlayRequest;
import com.pixiu.fortune8keno.fortune8keno.initialState.InitialPlayerStateService;
import com.pixiu.fortune8keno.fortune8keno.service.GamePlayService;
import com.pixiu.fortune8keno.fortune8keno.results.SpinResult;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("keno-pixiugame/")
@RequiredArgsConstructor
public class GameController {

    private final GamePlayService gamePlayService;
    private final InitialPlayerStateService initialPlayerStateService;


    @PostMapping("fortune8Keno")
    public SpinResult play(@RequestHeader(value = "X-Trace-ID", required = false) String traceId,
                           @RequestHeader(value = "X-Rtp-Variant", required = false) BigDecimal rtpVariant,
                           @RequestBody PlayRequest playRequest) {
        return gamePlayService.play(playRequest, rtpVariant);
    }

    @GetMapping("fortune8Keno/version")
    public String getServerVersion(@RequestHeader(value = "X-Trace-ID", required = false) String traceId,
                           @RequestHeader(value = "X-Rtp-Variant", required = false) BigDecimal rtpVariant,
                           @RequestBody PlayRequest playRequest) {
        return "version 0.0.2";
    }




}
