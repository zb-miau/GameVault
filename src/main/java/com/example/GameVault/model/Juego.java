package com.example.GameVault.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // Genera Getters, Setters, toString, etc. (Gracias a Lombok)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "juegos")
public class Juego {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titulo;

    @Column(nullable = false)
    private String descripcion;

    // Almacenaremos el nombre del archivo o la URL de la imagen
    @Column(nullable = false)
    private String portadaUrl;

    @Column
    private Double precio;

    @Column
    private String categoria;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

}