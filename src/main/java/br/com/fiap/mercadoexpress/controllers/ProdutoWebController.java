package br.com.fiap.mercadoexpress.controllers;

import br.com.fiap.mercadoexpress.models.Produto;
import br.com.fiap.mercadoexpress.services.ProdutoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Endpoints MVC do CRUD de produtos (tabela TDS_Sec_MVC_TB_Mercado).
 *
 * READ   GET  /produtos               lista (com busca ?busca=)   ADMIN e CLIENTE
 * READ   GET  /produtos/{id}          consulta de um produto      ADMIN e CLIENTE
 * CREATE GET  /produtos/novo  + POST /produtos                    ADMIN
 * UPDATE GET  /produtos/{id}/editar + POST /produtos              ADMIN
 * DELETE POST /produtos/{id}/excluir                              ADMIN
 */
@Controller
@RequestMapping("/produtos")
@RequiredArgsConstructor
public class ProdutoWebController {

    private final ProdutoService service;

    @GetMapping
    public String listar(@RequestParam(required = false) String busca, Model model) {
        model.addAttribute("produtos", service.buscar(busca));
        model.addAttribute("busca", busca);
        return "produtos/lista";
    }

    @GetMapping("/{id}")
    public String detalhe(@PathVariable Long id, Model model, RedirectAttributes redirect) {
        return service.buscarPorId(id)
                .map(produto -> {
                    model.addAttribute("produto", produto);
                    return "produtos/detalhe";
                })
                .orElseGet(() -> {
                    redirect.addFlashAttribute("mensagemErro", "Produto #" + id + " não encontrado.");
                    return "redirect:/produtos";
                });
    }

    @GetMapping("/novo")
    public String novo(Model model) {
        model.addAttribute("produto", new Produto());
        return "produtos/form";
    }

    @PostMapping
    public String salvar(@Valid @ModelAttribute("produto") Produto produto,
                         BindingResult result,
                         RedirectAttributes redirect) {
        if (result.hasErrors()) {
            return "produtos/form";
        }
        boolean novo = produto.getId() == null;
        Produto salvo = service.salvar(produto);
        redirect.addFlashAttribute("mensagemSucesso",
                novo ? "Produto \"" + salvo.getNome() + "\" cadastrado com sucesso!"
                     : "Produto \"" + salvo.getNome() + "\" atualizado com sucesso!");
        return "redirect:/produtos/" + salvo.getId();
    }

    @GetMapping("/{id}/editar")
    public String editar(@PathVariable Long id, Model model, RedirectAttributes redirect) {
        return service.buscarPorId(id)
                .map(produto -> {
                    model.addAttribute("produto", produto);
                    return "produtos/form";
                })
                .orElseGet(() -> {
                    redirect.addFlashAttribute("mensagemErro", "Produto #" + id + " não encontrado.");
                    return "redirect:/produtos";
                });
    }

    @PostMapping("/{id}/excluir")
    public String excluir(@PathVariable Long id, RedirectAttributes redirect) {
        service.excluir(id);
        redirect.addFlashAttribute("mensagemSucesso", "Produto #" + id + " excluído.");
        return "redirect:/produtos";
    }
}
