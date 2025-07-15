package com.gft.BTGPactual.service.impl;

import com.gft.BTGPactual.model.Usuario;
import com.gft.BTGPactual.service.IDynamoDbService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class DynamoUserDetailsServiceImpl implements UserDetailsService {
    private final IDynamoDbService dynamoDbService;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Usuario> usuarioOpt = dynamoDbService.obtenerUsuarioPorUsername(username);
        if (usuarioOpt.isEmpty()) {
            throw new UsernameNotFoundException("Usuario no encontrado: " + username);
        }
        Usuario usuario = usuarioOpt.get();

        List<GrantedAuthority> authorities = usuario.getRoles().stream()
                .map(Usuario.Rol::name)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        return new User(
                usuario.getUsername(),
                usuario.getPassword(),
                authorities
        );
    }
}
