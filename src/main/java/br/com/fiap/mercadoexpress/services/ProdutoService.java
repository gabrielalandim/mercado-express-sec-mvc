package br.com.fiap.mercadoexpress.services;

import br.com.fiap.mercadoexpress.models.Produto;
import br.com.fiap.mercadoexpress.repositories.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProdutoService {

    public static final int LIMITE_ESTOQUE_BAIXO = 10;

    private final ProdutoRepository repository;

    @Transactional(readOnly = true)
    public List<Produto> listarTodos() {
        return repository.findAllByOrderByNomeAsc();
    }

    @Transactional(readOnly = true)
    public List<Produto> buscar(String termo) {
        if (termo == null || termo.isBlank()) {
            return listarTodos();
        }
        String t = termo.trim();
        return repository.findByNomeContainingIgnoreCaseOrTipoContainingIgnoreCaseOrderByNomeAsc(t, t);
    }

    @Transactional(readOnly = true)
    public Optional<Produto> buscarPorId(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public Produto salvar(Produto produto) {
        return repository.save(produto);
    }

    @Transactional
    public void excluir(Long id) {
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public long contar() {
        return repository.count();
    }

    @Transactional(readOnly = true)
    public List<Produto> estoqueBaixo() {
        return repository.findByEstoqueLessThanEqualOrderByEstoqueAsc(LIMITE_ESTOQUE_BAIXO);
    }

    @Transactional(readOnly = true)
    public BigDecimal valorTotalEstoque() {
        return repository.findAll().stream()
                .map(p -> p.getPreco().multiply(BigDecimal.valueOf(p.getEstoque())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Transactional(readOnly = true)
    public BigDecimal lucroPotencialEstoque() {
        return repository.findAll().stream()
                .map(p -> p.getPreco().subtract(p.getPrecoCusto()).multiply(BigDecimal.valueOf(p.getEstoque())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
