package com.example.GameVault.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.GameVault.model.Juego;
import com.example.GameVault.repository.JuegosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class JuegoService {
    private static final String UPLOAD_DIR = "src/main/resources/static/uploads/";

    @Autowired
    private JuegosRepository juegosRepository;

    @Autowired
    private Cloudinary cloudinary;

    public List<Juego> listarTodos(){
        return juegosRepository.findAll();
    }

    public void guardarJuego(Juego juego, MultipartFile file){
        String nombreArchivo = "default.png";

        if (!file.isEmpty()){
           nombreArchivo = guardarImagenCloudinary(file);
        }

        juego.setPortadaUrl(nombreArchivo);
        juegosRepository.save(juego);
    }

    private String guardarImagenEnLocal (MultipartFile file){
        try {
            Path uploadPath = Paths.get(UPLOAD_DIR);
            if(!Files.exists(uploadPath)){
                Files.createDirectories(uploadPath);
            }
            String nombreArchivo = UUID.randomUUID().toString()+"_"+file.getOriginalFilename();
            Path filePath = uploadPath.resolve(nombreArchivo);
            Files.copy(file.getInputStream(), filePath);

            return nombreArchivo;
        } catch (IOException e){
            e.printStackTrace();
            return "default.png";
        }
    }

    private String guardarImagenCloudinary(MultipartFile file){
        try {
            Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
            return  uploadResult.get("secure_url").toString();
        } catch (Exception e){
            e.printStackTrace();
            return "default.png";
        }
    }

    public List<Juego> buscarPorTitulo(String titulo){
        if(titulo == null || titulo.trim().isEmpty()){
            return listarTodos();
        }

        return juegosRepository.findByTituloContainingIgnoreCase(titulo);
    }

    public Juego obtenerJuegoPorId(Long id){
        return juegosRepository.findById(id).orElse(null);
    }

    public void eliminarJuego(Long id){
        juegosRepository.deleteById(id);
    }

    public List<Juego> obtenerJuegosPorUsuario(Long usuarioId){
        return juegosRepository.findByUsuarioId(usuarioId);
    }
}

