package br.com.fiap.mercadoexpress.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Perfil de usuário salvo no Sign Up.
 * Tabela no PostgreSQL: TDS_Users_Mercado
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "TDS_Users_Mercado")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(nullable = false, unique = true, length = 120)
    private String email;

    @Column(nullable = false, unique = true, length = 40)
    private String username;

    /** Senha criptografada com BCrypt (nunca é salva em texto puro). */
    @Column(nullable = false, length = 100)
    private String senha;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Role role;

    @Column(name = "data_cadastro", nullable = false)
    private LocalDateTime dataCadastro;

    @PrePersist
    void prePersist() {
        if (dataCadastro == null) {
            dataCadastro = LocalDateTime.now();
        }
    }
}
