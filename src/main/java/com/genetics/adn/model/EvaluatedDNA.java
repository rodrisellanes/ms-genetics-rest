package com.genetics.adn.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class EvaluatedDNA implements Serializable {

    private static final long serialVersionUID = 190212L;

    private String[] adn;
    private boolean mutante;
}
