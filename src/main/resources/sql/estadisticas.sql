
-- QUERY estadisticas ADN mutantes, humanos y ratio mutante

select
  sum(case when mutante then 1 end) contador_adn_mutante,
  sum(case when not mutante then 1 end) contador_adn_humano,
  sum(case when mutante then 1.0 else 0 end) / count(1) ratio
from
  adn.adn_evaluados;