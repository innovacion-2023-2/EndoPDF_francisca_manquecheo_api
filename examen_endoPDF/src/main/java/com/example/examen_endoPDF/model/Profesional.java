package com.example.examen_endoPDF.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Setter
@Getter
@Table(name = "profesionales")
public class Profesional {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(name = "nombre_usuario")
    private String nombre;

    @NotBlank
    @Column(name = "apellido_usuario")
    private String apellido;

    @NotBlank
    @Email
    @Column(unique = true)
    private String email;

    @NotBlank
    // @Size(min = 8, max = 20)
    // @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$")
    @JsonIgnore
    private String clave;

    private boolean verificado;
    @JsonIgnore
    private String tokenVerificacion;

    @JsonIgnore
    private LocalDateTime fechaCreacionTokenVerificacion;

    @JsonIgnore
    private String tokenSesion;

}
