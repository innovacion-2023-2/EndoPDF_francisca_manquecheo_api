package com.example.examen_endoPDF.security;


import com.example.examen_endoPDF.model.Profesional;
import com.example.examen_endoPDF.services.ProfesionalService;
import com.example.examen_endoPDF.model.Profesional;
import com.example.examen_endoPDF.services.UserDetailsImpl;

import com.example.examen_endoPDF.utils.TokenUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Collections;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final ProfesionalService profesionalService;

    public JWTAuthenticationFilter(ProfesionalService profesionalService) {
        this.profesionalService = profesionalService;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response){
        AuthCredenciales authCredenciales;

        try{
            authCredenciales = new ObjectMapper().readValue(request.getReader(), AuthCredenciales.class);
        }catch(IOException e){
            return null;
        }

        UsernamePasswordAuthenticationToken usernamePAT = new UsernamePasswordAuthenticationToken(
                authCredenciales.getEmail(),
                authCredenciales.getPassword(),
                Collections.emptyList()
        );

        return getAuthenticationManager().authenticate(usernamePAT);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException {
        UserDetailsImpl userDetails = (UserDetailsImpl) authResult.getPrincipal();
        String token;

        Profesional profesional = userDetails.getUsuario();

        //aqui validamos para asegurar que cada usuario tenga su propio token, pasado el periodo de expiracion
        //sera invalido y se generara uno nuevo
        if(profesional.isVerificado()){
        if(profesional.getTokenSesion()==null){
            token = TokenUtils.createToken(userDetails.getNombre(), userDetails.getUsername());
            profesional.setTokenSesion(token);
            profesionalService.guardarProfesional(profesional);
        }else {
            //llamamos a la funcion para verificar que el token aun sea valido
            if (TokenUtils.validarToken(profesional.getTokenSesion())) {
                token =profesional.getTokenSesion();
                //  ---agregar funcion para extender la duracion del token---
            } else {
                //si el token no es valido se genera el token nuevamente
                token = TokenUtils.createToken(userDetails.getNombre(), userDetails.getUsername());
                profesional.setTokenSesion(token);
                profesionalService.guardarProfesional(profesional);
            }
        }
            response.addHeader("Authorization","Bearer " + token);
            response.getWriter().flush();
        }else{
            //si el usuario no esta verificado enviara codigo 401 y un mensaje en el body
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // Establece el código de estado 401 (Unauthorized)
            response.getWriter().write("Usuario no verificado"); // Agrega el mensaje de respuesta
            response.getWriter().flush();
        }

    }
}
