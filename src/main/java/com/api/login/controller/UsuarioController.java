package com.api.login.controller;

import com.api.login.entity.Usuario;
import com.api.login.service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/user")
public class UsuarioController {

    private final UsuarioService service;

    public UsuarioController(UsuarioService service) {
        this.service = service;
    }

    @GetMapping(value = "/name/{name}")
    public ResponseEntity getByName(@PathVariable String name) {
        User user = (User) service.loadUserByUsername(name);
        return ResponseEntity.ok().body(user);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity findById(@PathVariable(value = "id") Long id) throws Exception {
        try {
            Usuario usuario = service.getById(id);
            return ResponseEntity.ok().body(usuario);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.noContent().build();
    }

    @PostMapping(value = "/create")
    public ResponseEntity createUser(@RequestBody Usuario user) {
        boolean created = service.create(user);
        if (created == true) {
            return ResponseEntity.ok().body(created);
        }
        return ResponseEntity.badRequest().build();
    }

    @PutMapping(value = "/put")
    public ResponseEntity updateUser(@RequestBody Usuario user) {
        service.update(user);
        return ResponseEntity.ok().body(user);
    }

    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity deleteById(@PathVariable Long id) {
        boolean deleted = service.deleteById(id);
        if (deleted == true) {
            return ResponseEntity.ok().body(deleted);
        }
        return ResponseEntity.badRequest().body(deleted);
    }

    @GetMapping(value = "/token")
    public ResponseEntity getToken() {

       String token = service.ceateToken();
        return ResponseEntity.ok().body(token);
    }

}
