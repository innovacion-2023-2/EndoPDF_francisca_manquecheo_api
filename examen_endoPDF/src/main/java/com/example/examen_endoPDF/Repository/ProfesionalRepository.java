package com.example.examen_endoPDF.Repository;

import com.example.examen_endoPDF.model.Profesional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProfesionalRepository extends JpaRepository<Profesional,Integer> {
    Optional<Profesional> findByTokenVerificacion(String tokenVerificacion);


    static Optional<Profesional> findOneByEmail(String email);
}
