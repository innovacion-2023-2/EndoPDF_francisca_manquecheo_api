package com.example.examen_endoPDF.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TokenUtils {
    private final static String TOKEN_BASE_SECRETO = "1532ADCkdjdieocsklxko56djuehd";
    private final static Long DURACION_TOKEN_SEGUNDOS = 7200L; // 2 horas

    public static String createToken(String nombre, String email){
        long tiempoExpiracion = DURACION_TOKEN_SEGUNDOS * 1000;
        Date fechaExpiracion = new Date(System.currentTimeMillis() + tiempoExpiracion);

        Map<String, Object> datosExtra = new HashMap<>();
        datosExtra.put("nombre", nombre);

        //esto produce el token que se retorna al cliente y en extra se ponen los datos que quieres devolver con el
        return Jwts.builder()
                .setSubject(email)
                .setExpiration(fechaExpiracion)
                .addClaims(datosExtra)
                .signWith(Keys.hmacShaKeyFor(TOKEN_BASE_SECRETO.getBytes()))
                .compact();
    }

    public static UsernamePasswordAuthenticationToken getAuthentication(String token){
        //proceso inverso a lo que hacemos arriba
        try{
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(TOKEN_BASE_SECRETO.getBytes())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            String email = claims.getSubject();
            return new UsernamePasswordAuthenticationToken(email, null, Collections.emptyList());
        } catch (JwtException e){
            return null;
        }
    }

    public static boolean validarToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(TOKEN_BASE_SECRETO.getBytes())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }
}
