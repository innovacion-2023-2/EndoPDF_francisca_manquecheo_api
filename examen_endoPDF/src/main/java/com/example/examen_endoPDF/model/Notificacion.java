package com.example.examen_endoPDF.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Date;

@Entity
@Table (name = "notificaciones")
public class Notificacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Temporal(TemporalType.DATE)
    private Date fecha;

    @NotBlank
    private String contenido;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Profesional profesional;

    @NotBlank
    private String nameProducto;

    @NotNull
    private boolean leida;

    public Notificacion(Date fecha, String contenido, Profesional profesional, String nameProducto) {
        this.fecha = fecha;
        this.contenido = contenido;
        this.profesional = profesional;
        this.nameProducto = nameProducto;
        this.leida = false;
    }

    public Notificacion() {

    }

    public void leer() {
        this.leida = !this.leida;
    }
}
