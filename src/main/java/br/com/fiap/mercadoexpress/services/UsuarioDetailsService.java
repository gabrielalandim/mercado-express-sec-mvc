package br.com.fiap.mercadoexpress.services;

import br.com.fiap.mercadoexpress.models.Usuario;
import br.com.fiap.mercadoexpress.repositories.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Faz a ponte entre a tabela TDS_Users_Mercado e o Spring Security.
 * O perfil (ADMIN / CLIENTE) vira a authority ROLE_ADMIN / ROLE_CLIENTE.
 */
@Service
@RequiredArgsConstructor
public class UsuarioDetailsService implements UserDetailsService {

    private final UsuarioRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = repository.findByUsernameIgnoreCase(username.trim())
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + username));

        return User.withUsername(usuario.getUsername())
                .password(usuario.getSenha())
                .roles(usuario.getRole().name())
                .build();
    }
}
