package br.com.fiap.mercadoexpress.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Regras de acesso do Mercado Express.
 *
 * Público ......... /, /login, /cadastro, /acesso-negado, arquivos estáticos
 * ADMIN ........... /admin/**, criar/editar/excluir produtos
 * CLIENTE ......... /cliente/**
 * ADMIN e CLIENTE . /produtos (lista e consulta de um produto)
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final LoginSuccessHandler loginSuccessHandler;
    private final LoginFailureHandler loginFailureHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        // Rotas públicas
                        .requestMatchers("/", "/index", "/login", "/cadastro", "/acesso-negado", "/error").permitAll()
                        .requestMatchers("/css/**", "/js/**", "/img/**", "/favicon.ico").permitAll()

                        // Somente ADMIN
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/produtos/novo", "/produtos/*/editar").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/produtos", "/produtos/*/excluir").hasRole("ADMIN")

                        // Somente CLIENTE
                        .requestMatchers("/cliente/**").hasRole("CLIENTE")

                        // ADMIN e CLIENTE (consulta)
                        .requestMatchers(HttpMethod.GET, "/produtos", "/produtos/*").hasAnyRole("ADMIN", "CLIENTE")

                        .anyRequest().authenticated()
                )
                // Tela de login própria (substitui a tela padrão do Spring Security)
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .successHandler(loginSuccessHandler)   // redireciona conforme o perfil
                        .failureHandler(loginFailureHandler)   // sem conta -> tela de Sign Up
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                )
                .exceptionHandling(ex -> ex.accessDeniedPage("/acesso-negado"));
        // CSRF permanece habilitado: os formulários Thymeleaf (th:action) enviam o token automaticamente.

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
