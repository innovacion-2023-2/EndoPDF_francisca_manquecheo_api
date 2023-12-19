package com.example.examen_endoPDF.services;

import com.example.examen_endoPDF.Repository.ProfesionalRepository;
import com.example.examen_endoPDF.model.Profesional;
import com.example.examen_endoPDF.model.ProfesionalDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

public class ProfesionalService implements UserDetailsService {

    private ProfesionalRepository profesionalRepository;
    private EmailService emailService;


    public Profesional crearUsuarioDesdeDTO(ProfesionalDTO profesionalDTO) {

        Profesional profesional = new Profesional();
        profesional.setNombre(profesionalDTO.getNombre());
        profesional.setEmail(profesionalDTO.getEmail());
        profesional.setClave(profesionalDTO.getClave());

       return profesional;

    }

    public void guardarProfesional(Profesional profesional) {
        profesionalRepository.save(profesional);
    }

    public void enviarCorreoVerificacion(Profesional profesional, String token) {
        String cuerpoMensaje = construirCuerpoMensajeVerificacion(profesional, token);
        emailService.enviarCorreo(profesional.getEmail(), "Verificación de cuenta", cuerpoMensaje);
    }

    private String construirCuerpoMensajeVerificacion(Profesional profesional, String token) {
        return String.format(
                "Estimado, %s,%nGracias por registrarte. Para verificar tu cuenta, haz clic en el siguiente enlace:%n%n%s",
               profesional.getNombre(),
                "http://localhost:8081/verificar?token=" + token
        );
    }
    public Optional<Profesional> obtenerUsuarioPorTokenVerificacion(String tokenVerificacion) {
        return profesionalRepository.findByTokenVerificacion(tokenVerificacion);
    }
    public ResponseEntity<String> verificarCuenta(Profesional profesional) {
        if (profesional != null && !profesional.isVerificado()) {
            if (esValidoTokenVerificacion(profesional)) {
                marcarCuentaComoVerificada(profesional);
                return ResponseEntity.ok("Cuenta verificada exitosamente");
            } else {
                return ResponseEntity.badRequest().body("El token de verificación ha expirado. Solicita un nuevo correo de verificación.");
            }
        } else {
            return ResponseEntity.badRequest().body("Error al verificar la cuenta");
        }
    }
    private boolean esValidoTokenVerificacion(Profesional profesional) {
        LocalDateTime fechaCreacionToken = profesional.getFechaCreacionTokenVerificacion();
        LocalDateTime fechaActual = LocalDateTime.now();
        final int DURACION_TOKEN_VERIFICACION_MINUTOS = 60;
        long minutosTranscurridos = ChronoUnit.MINUTES.between(fechaCreacionToken, fechaActual);

        return minutosTranscurridos <= DURACION_TOKEN_VERIFICACION_MINUTOS;
    }
    private void marcarCuentaComoVerificada(Profesional profesional) {
        profesional.setVerificado(true);
        profesional.setTokenVerificacion(null);
        guardarProfesional(profesional);
    }
    public Profesional obtenerUsuarioSesion(Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) loadUserByUsername(authentication.getName());
        return obtenerUsuarioPorEmail(userDetails.getUsuario().getEmail())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    private Optional<Profesional> obtenerUsuarioPorEmail(String email) {
      return  ProfesionalRepository.findOneByEmail(email);

    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Profesional profesional = ProfesionalRepository
                .findOneByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("El usuario con email " + email + " no existe."));
        return new UserDetailsImpl(profesional);
    }
}
