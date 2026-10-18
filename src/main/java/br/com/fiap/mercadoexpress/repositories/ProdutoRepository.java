package br.com.fiap.mercadoexpress.repositories;

import br.com.fiap.mercadoexpress.models.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    List<Produto> findAllByOrderByNomeAsc();

    List<Produto> findByNomeContainingIgnoreCaseOrTipoContainingIgnoreCaseOrderByNomeAsc(String nome, String tipo);

    List<Produto> findByEstoqueLessThanEqualOrderByEstoqueAsc(Integer limite);
}
