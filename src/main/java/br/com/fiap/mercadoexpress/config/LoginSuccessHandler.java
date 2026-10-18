package br.com.fiap.mercadoexpress.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Após o login, cada tipo de usuário vai para a sua tela de acesso restrito:
 * ADMIN -> /admin (painel de gestão) | CLIENTE -> /cliente (área do cliente)
 */
@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        boolean isAdmin = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch("ROLE_ADMIN"::equals);

        String destino = isAdmin ? "/admin" : "/cliente";
        response.sendRedirect(request.getContextPath() + destino);
    }
}
