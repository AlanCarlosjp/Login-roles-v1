package com.api.login.service;

import com.api.login.entity.Usuario;
import com.api.login.repository.UsuarioRepository;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;

@Service
public class UsuarioService implements UserDetailsService {

    private final UsuarioRepository repository;
    private final BCryptPasswordEncoder encoder;

    public UsuarioService(UsuarioRepository repository, BCryptPasswordEncoder encoder) {
        this.repository = repository;
        this.encoder = encoder;
    }



    @Transactional(readOnly = true)
    public Usuario getById(Long id) throws Exception {
        Optional<Usuario> entity = repository.findById(id);
        return entity.orElseThrow(() -> new Exception("USUARIO COM ID: " + id));
    }

    @Transactional
    public boolean create(Usuario user) {
       try {
           String password = encoder.encode(user.getPassword());
           user.setPassword(password);
           repository.save(user);
           return true;
       }catch (Exception e){
           e.printStackTrace();
       }
       return false;
    }

    public Usuario update(Usuario user) {

        return user;
    }

    public boolean deleteById(Long id) {
        try{
            repository.deleteById(id);
            return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
       Optional<Usuario> entity = repository.getByName(name);
        entity.orElseThrow(
               () -> new UsernameNotFoundException("Nome não encontrado"));

        String[] roles = entity.get().isAdm() ?
                new String[]{"ADM", "USER"} : new String[]{"USER"};

        return User.builder()
                .username(entity.get().getUsername())
                .password(entity.get().getPassword())
                .roles(roles)
                .build();
    }

    public String ceateToken() {
        String token = JWT.create().withSubject("teste")
                .withExpiresAt(new Date(System.currentTimeMillis() + 60000L)).sign(Algorithm.HMAC512("TESTE"));

        return token;
    }
}
