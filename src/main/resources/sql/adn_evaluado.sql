
-- INSERTAR ADN evaluado en tabla adn_evaluados


insert into adn.adn_evaluados(
  adn,
  mutante,
  fecha_creacion
) values(
  :adn,
  :mutante,
  now()
);


-- INSERT ADN PRUEBA

insert into adn.adn_evaluados(
  adn,
  mutante,
  fecha_creacion
) values(
  '{AAGTTT,CAGTGC,TTATGT,AAAAGG,CACCTA,TCACTG}',
  FALSE,
  now()
);