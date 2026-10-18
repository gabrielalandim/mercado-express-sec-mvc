package br.com.fiap.mercadoexpress.repositories;

import br.com.fiap.mercadoexpress.models.Role;
import br.com.fiap.mercadoexpress.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByUsernameIgnoreCase(String username);

    boolean existsByUsernameIgnoreCase(String username);

    boolean existsByEmailIgnoreCase(String email);

    long countByRole(Role role);

    List<Usuario> findAllByOrderByDataCadastroDesc();
}
