package com.example.GameVault.controller;

import com.example.GameVault.model.Usuario;
import com.example.GameVault.service.UsuarioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {
    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/login")
    public String mostrarLogin(){
        return "login";
    }

    @PostMapping("/login")
    public String procesarLogin(@RequestParam("email") String email,
                                @RequestParam("password") String password,
                                HttpSession session,
                               Model model){

        Usuario usuario = usuarioService.autenticaUsuario(email, password);

        if(usuario != null){
            session.setAttribute("usuario_id", usuario.getId());
            return "redirect:/mis-juegos";
        } else {
            model.addAttribute("error", "Credenciales Incorrectas");
            return "login";
        }

    }

    @GetMapping("/registro")
    public String mostrarRegistro(Model model){
        model.addAttribute("usuario", new Usuario());
        return "registro";
    }

    @GetMapping("/logout")
    public String cerrarSesion(HttpSession session){
        session.invalidate();
        return "redirect:/";
    }

    @PostMapping("/registro")
    public String procesarRegistro(Usuario usuario){
        usuarioService.registrarUsuario(usuario);
        return "redirect:/login?registrado=true";
    }
}
