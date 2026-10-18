package br.com.fiap.mercadoexpress.controllers;

import br.com.fiap.mercadoexpress.services.ProdutoService;
import br.com.fiap.mercadoexpress.services.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/** Área restrita do CLIENTE. */
@Controller
@RequestMapping("/cliente")
@RequiredArgsConstructor
public class ClienteController {

    private final UsuarioService usuarioService;
    private final ProdutoService produtoService;

    @GetMapping
    public String area(@RequestParam(required = false) String busca, Authentication auth, Model model) {
        usuarioService.buscarPorUsername(auth.getName())
                .ifPresent(usuario -> model.addAttribute("usuario", usuario));
        model.addAttribute("produtos", produtoService.buscar(busca));
        model.addAttribute("busca", busca);
        return "cliente/area";
    }
}
