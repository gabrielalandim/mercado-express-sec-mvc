package br.com.fiap.mercadoexpress.services;

import br.com.fiap.mercadoexpress.dtos.CadastroUsuarioDTO;
import br.com.fiap.mercadoexpress.models.Role;
import br.com.fiap.mercadoexpress.models.Usuario;
import br.com.fiap.mercadoexpress.repositories.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository repository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Sign Up: todo usuário que se cadastra pela tela pública recebe o perfil CLIENTE.
     * A senha é criptografada com BCrypt antes de ser salva em TDS_Users_Mercado.
     */
    @Transactional
    public Usuario cadastrarCliente(CadastroUsuarioDTO dto) {
        return criar(dto.getNome(), dto.getEmail(), dto.getUsername(), dto.getSenha(), Role.CLIENTE);
    }

    @Transactional
    public Usuario criar(String nome, String email, String username, String senhaPura, Role role) {
        Usuario usuario = new Usuario();
        usuario.setNome(nome.trim());
        usuario.setEmail(email.trim().toLowerCase());
        usuario.setUsername(username.trim().toLowerCase());
        usuario.setSenha(passwordEncoder.encode(senhaPura));
        usuario.setRole(role);
        return repository.save(usuario);
    }

    @Transactional(readOnly = true)
    public boolean usernameEmUso(String username) {
        return username != null && repository.existsByUsernameIgnoreCase(username.trim());
    }

    @Transactional(readOnly = true)
    public boolean emailEmUso(String email) {
        return email != null && repository.existsByEmailIgnoreCase(email.trim());
    }

    @Transactional(readOnly = true)
    public Optional<Usuario> buscarPorUsername(String username) {
        return repository.findByUsernameIgnoreCase(username);
    }

    @Transactional(readOnly = true)
    public List<Usuario> listarTodos() {
        return repository.findAllByOrderByDataCadastroDesc();
    }

    @Transactional(readOnly = true)
    public long contar() {
        return repository.count();
    }

    @Transactional(readOnly = true)
    public long contarPorRole(Role role) {
        return repository.countByRole(role);
    }

    /** ADMIN pode promover um CLIENTE a ADMIN (ou o inverso), exceto a si mesmo. */
    @Transactional
    public void alternarPerfil(Long id, String usernameLogado) {
        Usuario usuario = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));
        if (usuario.getUsername().equalsIgnoreCase(usernameLogado)) {
            throw new IllegalArgumentException("Você não pode alterar o seu próprio perfil.");
        }
        usuario.setRole(usuario.getRole() == Role.ADMIN ? Role.CLIENTE : Role.ADMIN);
    }
}
