package com.pixiu.fortune8keno.fortune8keno.results;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.pixiu.fortune8keno.fortune8keno.dto.play.GameStateDTO;
import lombok.Data;

import java.util.Set;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommandResult {
    private Set<Integer> selectedNumbers;
    private Set<Integer> drawnNumbers;
    private Set<Integer> matchedNumbers;
    private int hits;
    private int lastNumber;
    private int multiplier;
    private GameStateDTO gameState = new GameStateDTO();
}
