package io.bootify.my_gate_visitor_management_project.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder();
    }

//    public static void main(String[] args) {
//        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//        System.out.println(passwordEncoder.encode("test@123"));
//    }

    // A bean of security filter chain is required for authorization
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception
    {
        httpSecurity
                .csrf((httpSecurityCsrfConfigurer -> httpSecurityCsrfConfigurer.disable()))
                .authorizeHttpRequests((auth) -> {
                    auth.requestMatchers("/api/admin/**").hasAuthority("admin")
                            .requestMatchers("/api/gatekeeper/**").hasAuthority("gatekeeper")
                            //.requestMatchers("/api/user/**").hasAuthority("resident")  // This is to check with single authority
                            .requestMatchers("/api/user/**").hasAnyAuthority("resident", "admin") // since admin can also have flat
                            .requestMatchers("/public/**").permitAll()
                            .requestMatchers("/v3/api-docs/**").permitAll()
                            .requestMatchers("/swagger-ui/**").permitAll()
                            .anyRequest().authenticated();
                })
                .formLogin(Customizer.withDefaults())
                .httpBasic(Customizer.withDefaults());
        return httpSecurity.build();
    }


}
