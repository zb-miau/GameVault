package com.example.GameVault.controller;

import com.example.GameVault.model.Juego;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
public class GameController {
    private static List<Juego> juegosDB = new ArrayList<>();
    private static long idCounter = 1;

    private static final String UPLOAD_DIR = "src/main/resources/static/uploads/";

    @GetMapping("/fragments-demo")
    public String fragmentsDemo(){
        return "fragments-demo";
    }


    @GetMapping({"/", "/juegos"})
    public String listarJuegos(Model model){
        model.addAttribute("juegos", juegosDB);
        return "juegos";
    }

    @GetMapping("/juegos/nuevo")
    public String mostrarFormulario(){
        return "formulario";
    }

    @PostMapping("/juegos")
    public String guardarJuego(@RequestParam("titulo") String titulo,
                               @RequestParam("descripcion") String descripcion,
                               @RequestParam("portada") MultipartFile portada)
    {
        String nombreArchivo = "default.png";
        if (!portada.isEmpty()){
            try {
                Path uploadPath = Paths.get(UPLOAD_DIR);
                if(!Files.exists(uploadPath)){
                    Files.createDirectories(uploadPath);
                }
                nombreArchivo = UUID.randomUUID().toString()+"_"+portada.getOriginalFilename();
                Path filePath = uploadPath.resolve(nombreArchivo);
                Files.copy(portada.getInputStream(), filePath);
            } catch (IOException e){
                e.printStackTrace();
            }
        }
        Juego nuevoJuego = new Juego(idCounter++, titulo, descripcion, nombreArchivo);
        juegosDB.add(nuevoJuego);
        return "redirect:/juegos";
    }

}
