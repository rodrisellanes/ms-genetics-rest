package com.genetics.adn.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class StatsResponse {

    @JsonProperty("count_mutant_dna")
    private Integer totalizadorAdnMutante;

    @JsonProperty("count_human_dna")
    private Integer totalizadorAdnHumano;

    @JsonProperty("ratio")
    private Double ratio;
}
