
-- CREAR SCHEMA (ADN) EN DB

create schema adn;

-- CREAR TABLA (ADN_EVALUADOS) EN DB

create table adn.adn_evaluados(
  id serial primary key,
  adn text not null,
  mutante boolean not null,
  fecha_creacion timestamp default now(),
  unique(adn)
)