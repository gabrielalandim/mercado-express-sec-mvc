package br.com.fiap.mercadoexpress.config;

import br.com.fiap.mercadoexpress.models.Produto;
import br.com.fiap.mercadoexpress.models.Role;
import br.com.fiap.mercadoexpress.repositories.ProdutoRepository;
import br.com.fiap.mercadoexpress.services.UsuarioService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UsuarioService usuarioService;
    private final ProdutoRepository produtoRepository;

    @Value("${app.admin.username}")
    private String adminUsername;

    @Value("${app.admin.password}")
    private String adminPassword;

    @Value("${app.seed-demo-data:true}")
    private boolean seedDemoData;

    @Override
    public void run(String... args) {
        if (!usuarioService.usernameEmUso(adminUsername)) {
            usuarioService.criar("Administrador", "admin@mercadoexpress.com.br", adminUsername, adminPassword, Role.ADMIN);
            log.info("Usuário ADMIN '{}' criado.", adminUsername);
        }

        if (!seedDemoData) {
            return;
        }

        if (!usuarioService.usernameEmUso("cliente")) {
            usuarioService.criar("Cliente Demonstração", "cliente@mercadoexpress.com.br", "cliente", "cliente123", Role.CLIENTE);
            log.info("Usuário CLIENTE 'cliente' criado.");
        }

        if (produtoRepository.count() == 0) {
            produtoRepository.saveAll(List.of(
                    new Produto("Maçã Gala", "Fruta", "Hortifruti", "1 kg", bd("9.90"), bd("5.20"), 80),
                    new Produto("Banana Prata", "Fruta", "Hortifruti", "1 kg", bd("6.49"), bd("3.10"), 120),
                    new Produto("Detergente Neutro", "Limpeza", "Limpeza", "500 ml", bd("2.79"), bd("1.35"), 60),
                    new Produto("Água Sanitária", "Limpeza", "Limpeza", "2 L", bd("8.90"), bd("4.40"), 8),
                    new Produto("Meia Esportiva", "Vestuário", "Meias", "Par - Tam. 39/43", bd("19.90"), bd("8.00"), 35),
                    new Produto("Sabonete Hidratante", "Higiene", "Higiene Pessoal", "90 g", bd("3.49"), bd("1.60"), 5),
                    new Produto("Café Torrado", "Mercearia", "Mercearia", "500 g", bd("24.90"), bd("15.80"), 40),
                    new Produto("Leite Integral", "Laticínio", "Frios e Laticínios", "1 L", bd("5.99"), bd("3.90"), 90)
            ));
            log.info("Produtos de demonstração cadastrados.");
        }
    }

    private static BigDecimal bd(String valor) {
        return new BigDecimal(valor);
    }
}
