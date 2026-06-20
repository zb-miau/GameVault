package com.example.GameVault.service;

import com.example.GameVault.model.Usuario;
import com.example.GameVault.repository.UsuarioRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsuarioService {
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private HttpSession httpSession;

    public Usuario registrarUsuario(Usuario usuario){
        usuario.setContrasenia(passwordEncoder.encode(usuario.getContrasenia()));
        return usuarioRepository.save(usuario);
    }

    public Usuario autenticaUsuario(String email, String contrasenia){
        Optional <Usuario> usuarioopt = usuarioRepository.findByEmail(email);

        if(usuarioopt.isPresent()){
            Usuario usuario = usuarioopt.get();
            if(passwordEncoder.matches(contrasenia, usuario.getContrasenia())){
                return usuario;
            }
        }

        return null;
    }

    public Usuario obtenerUsuarioLogueado(){
        Long usuarioId = (Long) httpSession.getAttribute("usuario_id");
        if (usuarioId != null){
            Optional <Usuario> usuarioopt = usuarioRepository.findById(usuarioId);
            return usuarioopt.get();
        }

        return null;
    }
}
