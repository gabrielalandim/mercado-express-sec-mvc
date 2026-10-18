package br.com.fiap.mercadoexpress.controllers;

import br.com.fiap.mercadoexpress.models.Role;
import br.com.fiap.mercadoexpress.services.ProdutoService;
import br.com.fiap.mercadoexpress.services.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/** Área restrita do ADMIN. */
@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final ProdutoService produtoService;
    private final UsuarioService usuarioService;

    @GetMapping
    public String painel(Model model) {
        model.addAttribute("totalProdutos", produtoService.contar());
        model.addAttribute("totalUsuarios", usuarioService.contar());
        model.addAttribute("totalClientes", usuarioService.contarPorRole(Role.CLIENTE));
        model.addAttribute("totalAdmins", usuarioService.contarPorRole(Role.ADMIN));
        model.addAttribute("valorEstoque", produtoService.valorTotalEstoque());
        model.addAttribute("lucroPotencial", produtoService.lucroPotencialEstoque());
        model.addAttribute("estoqueBaixo", produtoService.estoqueBaixo());
        model.addAttribute("limiteEstoque", ProdutoService.LIMITE_ESTOQUE_BAIXO);
        return "admin/painel";
    }

    @GetMapping("/usuarios")
    public String usuarios(Model model) {
        model.addAttribute("usuarios", usuarioService.listarTodos());
        return "admin/usuarios";
    }

    @PostMapping("/usuarios/{id}/perfil")
    public String alternarPerfil(@PathVariable Long id, Authentication auth, RedirectAttributes redirect) {
        try {
            usuarioService.alternarPerfil(id, auth.getName());
            redirect.addFlashAttribute("mensagemSucesso", "Perfil do usuário atualizado.");
        } catch (IllegalArgumentException e) {
            redirect.addFlashAttribute("mensagemErro", e.getMessage());
        }
        return "redirect:/admin/usuarios";
    }
}
