package com.genetics.adn.queue;

import com.genetics.adn.model.EvaluatedDNA;

public interface MessagePublisher {

    void publish(EvaluatedDNA mensaje);
}
