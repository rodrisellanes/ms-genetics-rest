package com.genetics.adn.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StatsResponse {

    @JsonProperty("count_mutant_dna")
    private Integer contadorAdnMutante;

    @JsonProperty("count_human_dna")
    private Integer contadorAdnHumano;

    @JsonProperty("ratio")
    private Double ratio;
}
