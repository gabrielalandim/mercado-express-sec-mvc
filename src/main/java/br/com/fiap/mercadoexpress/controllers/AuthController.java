package br.com.fiap.mercadoexpress.controllers;

import br.com.fiap.mercadoexpress.dtos.CadastroUsuarioDTO;
import br.com.fiap.mercadoexpress.services.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final UsuarioService usuarioService;

    /** Tela de login customizada (não usa a tela padrão do Spring Security). */
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    /** Tela de Sign Up. Recebe ?username=... quando o login falhou por falta de cadastro. */
    @GetMapping("/cadastro")
    public String cadastro(@RequestParam(required = false) String username, Model model) {
        CadastroUsuarioDTO dto = new CadastroUsuarioDTO();
        if (username != null) {
            dto.setUsername(username);
        }
        model.addAttribute("usuario", dto);
        return "cadastro";
    }

    @PostMapping("/cadastro")
    public String cadastrar(@Valid @ModelAttribute("usuario") CadastroUsuarioDTO dto,
                            BindingResult result,
                            RedirectAttributes redirect) {

        if (dto.getSenha() != null && !dto.getSenha().equals(dto.getConfirmacaoSenha())) {
            result.rejectValue("confirmacaoSenha", "senha.diferente", "As senhas não conferem");
        }
        if (usuarioService.usernameEmUso(dto.getUsername())) {
            result.rejectValue("username", "username.duplicado", "Este nome de usuário já está em uso");
        }
        if (usuarioService.emailEmUso(dto.getEmail())) {
            result.rejectValue("email", "email.duplicado", "Este e-mail já está cadastrado");
        }
        if (result.hasErrors()) {
            return "cadastro";
        }

        usuarioService.cadastrarCliente(dto);
        redirect.addFlashAttribute("mensagemSucesso",
                "Cadastro realizado! Agora é só entrar com o usuário " + dto.getUsername().trim().toLowerCase() + ".");
        return "redirect:/login";
    }
}
