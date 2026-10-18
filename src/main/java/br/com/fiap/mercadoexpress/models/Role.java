package br.com.fiap.mercadoexpress.models;

/**
 * Tipos de usuário do sistema.
 * ADMIN   -> gerencia produtos (CRUD completo) e usuários.
 * CLIENTE -> consulta o catálogo e acessa sua área restrita.
 */
public enum Role {
    ADMIN,
    CLIENTE
}
