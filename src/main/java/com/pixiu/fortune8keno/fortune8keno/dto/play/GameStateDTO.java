package com.pixiu.fortune8keno.fortune8keno.dto.play;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GameStateDTO {

    private GameState currentState;
    private GameState nextState;
    private boolean closeRound;

    private Set<Integer> selectedNumbers;
    private Set<Integer> drawnNumbers;
    private Set<Integer> matchedNumbers;
    private BigDecimal winAmount;
    private BigDecimal stake;
}
