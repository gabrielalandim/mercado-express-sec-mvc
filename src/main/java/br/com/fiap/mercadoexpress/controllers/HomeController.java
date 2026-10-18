package br.com.fiap.mercadoexpress.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    /** Landing page pública (index.html). */
    @GetMapping({"/", "/index"})
    public String index() {
        return "index";
    }

    @GetMapping("/acesso-negado")
    public String acessoNegado() {
        return "acesso-negado";
    }
}
