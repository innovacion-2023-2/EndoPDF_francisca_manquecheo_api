package com.example.examen_endoPDF.Repository;

import com.example.examen_endoPDF.model.Informe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InformeRepository extends JpaRepository<Informe,Integer> {
}
