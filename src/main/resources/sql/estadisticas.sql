
-- QUERY estadisticas ADN mutantes, humanos y ratio mutante

select
  coalesce(sum(case when mutante then 1 end), 0) contador_adn_mutante,
  coalesce(sum(case when not mutante then 1 end), 0) contador_adn_humano,
  coalesce(sum(case when mutante then 1.0 else 0 end) / count(1), 0) ratio
from
  adn.adn_evaluados;