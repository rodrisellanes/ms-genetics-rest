
-- CREAR SCHEMA (ADN) EN DB

create schema adn;

-- CREAR TABLA (ADN_EVALUADOS) EN DB

create table adn.adn_evaluados(
  id serial primary key,
  adn varchar(400) not null,
  mutante boolean not null,
  fecha_creacion timestamp default now()
);