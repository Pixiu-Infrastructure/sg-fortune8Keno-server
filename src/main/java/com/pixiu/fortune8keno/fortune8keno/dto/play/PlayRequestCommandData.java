package com.pixiu.fortune8keno.fortune8keno.dto.play;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;
import java.util.Set;

@Data
@NoArgsConstructor
public class PlayRequestCommandData {
    public  String action ;
    private BigDecimal stakeAmount;
    private Integer nbTickets;
    private BigDecimal betValue;

    private Set<Integer> selectedNumbers;
    private String extraParameters;
    private String addOns;
    private Random randomNumber;

    public PlayRequestCommandData(String ACTION, BigDecimal stakeAmount, int nbTickets, BigDecimal betValue, Set<Integer> selectedNumbers, String extraParameters, String addOns) {
        this.action = ACTION;
        this.stakeAmount = stakeAmount;
        this.nbTickets = nbTickets;
        this.betValue = betValue;
        this.selectedNumbers = selectedNumbers;
        this.extraParameters = extraParameters;
        this.addOns = addOns;
    }
}
