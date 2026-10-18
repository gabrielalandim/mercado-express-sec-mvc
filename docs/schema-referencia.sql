-- =====================================================================
-- Mercado Express - CP5 Parte 1
-- Script de REFERÊNCIA das tabelas no PostgreSQL.
-- (O Hibernate cria/atualiza as tabelas automaticamente com ddl-auto=update;
--  este arquivo serve para documentação e para criação manual, se preferir.)
-- =====================================================================

CREATE TABLE IF NOT EXISTS tds_sec_mvc_tb_mercado (
    id           BIGSERIAL PRIMARY KEY,
    nome         VARCHAR(100)   NOT NULL,
    tipo         VARCHAR(60)    NOT NULL,
    setor        VARCHAR(60)    NOT NULL,
    tamanho      VARCHAR(40)    NOT NULL,
    preco        NUMERIC(10, 2) NOT NULL,
    preco_custo  NUMERIC(10, 2) NOT NULL,
    estoque      INTEGER        NOT NULL
);

CREATE TABLE IF NOT EXISTS tds_users_mercado (
    id             BIGSERIAL PRIMARY KEY,
    nome           VARCHAR(100) NOT NULL,
    email          VARCHAR(120) NOT NULL UNIQUE,
    username       VARCHAR(40)  NOT NULL UNIQUE,
    senha          VARCHAR(100) NOT NULL,          -- hash BCrypt
    role           VARCHAR(20)  NOT NULL,          -- ADMIN | CLIENTE
    data_cadastro  TIMESTAMP    NOT NULL
);

-- Consultas úteis
-- SELECT * FROM tds_sec_mvc_tb_mercado ORDER BY nome;
-- SELECT id, nome, username, email, role, data_cadastro FROM tds_users_mercado;
