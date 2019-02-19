package com.genetics.adn.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class DnaRequest {

    @JsonProperty("dna")
    private String[] adn;
}
