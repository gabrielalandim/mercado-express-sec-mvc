package br.com.fiap.mercadoexpress.config;

import br.com.fiap.mercadoexpress.repositories.UsuarioRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * Falha no login:
 *  - se o usuário NÃO possui cadastro -> redireciona para a tela de Sign Up (/cadastro)
 *  - se o usuário existe, mas a senha está errada -> volta para /login?error
 */
@Component
@RequiredArgsConstructor
public class LoginFailureHandler implements AuthenticationFailureHandler {

    private final UsuarioRepository usuarioRepository;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception) throws IOException {
        String username = request.getParameter("username");

        if (username != null && !username.isBlank()
                && !usuarioRepository.existsByUsernameIgnoreCase(username.trim())) {
            String param = URLEncoder.encode(username.trim(), StandardCharsets.UTF_8);
            response.sendRedirect(request.getContextPath() + "/cadastro?semConta&username=" + param);
            return;
        }

        response.sendRedirect(request.getContextPath() + "/login?error");
    }
}
