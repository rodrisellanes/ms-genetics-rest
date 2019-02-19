package com.genetics.adn.model;

import lombok.Data;

@Data
public class StatsResponse {

    private Integer countMutantDna;
    private Integer countHumanDna;
    private Double ratio;
}
