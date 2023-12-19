package com.example.examen_endoPDF.controllers;

import com.example.examen_endoPDF.model.Profesional;
import com.example.examen_endoPDF.model.ProfesionalDTO;
import com.example.examen_endoPDF.services.ProfesionalService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
public class ProfesionalController {

    private ProfesionalService profesionalService;
    private Profesional profesional;
    private PasswordEncoder passwordEncoder;


    @PostMapping("/registro")
    public ResponseEntity<String> registrarUsuario(@RequestBody ProfesionalDTO profesionalDTO) {

        Profesional profesional = profesionalService.crearUsuarioDesdeDTO(profesionalDTO);
        String token = UUID.randomUUID().toString();
       profesional.setTokenVerificacion(token);
        profesional.setFechaCreacionTokenVerificacion(LocalDateTime.now());
        // Encriptar la contraseña
        String claveEncriptada = passwordEncoder.encode(profesional.getClave());
        profesional.setClave(claveEncriptada);
        // Guardar el usuario en la base de datos
        profesionalService.guardarProfesional(profesional);
        profesionalService.enviarCorreoVerificacion(profesional, token);
        // La línea de abajo es para probar las notificaciones
        return ResponseEntity.ok("Usuario registrado correctamente. Verifica tu cuenta a través del correo electrónico.");
    }
}
