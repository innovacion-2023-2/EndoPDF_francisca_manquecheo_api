package com.example.examen_endoPDF.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "informes")
public class Informe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(name = "nombre_informe")
    private String nombreInforme;

    @Column(name = "descripcion")
    private String descripcion;

    private String ruta;


}
