package br.com.fiap.mercadoexpress.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CadastroUsuarioDTO {

    @NotBlank(message = "Informe seu nome")
    @Size(max = 100)
    private String nome;

    @NotBlank(message = "Informe seu e-mail")
    @Email(message = "E-mail inválido")
    @Size(max = 120)
    private String email;

    @NotBlank(message = "Escolha um nome de usuário")
    @Size(min = 3, max = 40, message = "O usuário deve ter entre 3 e 40 caracteres")
    @Pattern(regexp = "^[a-zA-Z0-9._-]+$", message = "Use apenas letras, números, ponto, hífen ou underline")
    private String username;

    @NotBlank(message = "Crie uma senha")
    @Size(min = 6, max = 60, message = "A senha deve ter pelo menos 6 caracteres")
    private String senha;

    @NotBlank(message = "Confirme sua senha")
    private String confirmacaoSenha;
}
