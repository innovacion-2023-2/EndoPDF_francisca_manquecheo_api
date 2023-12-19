package com.example.examen_endoPDF.controllers;

import com.example.examen_endoPDF.model.ProfesionalDTO;
import com.example.examen_endoPDF.services.InformeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class InformeController {

    private InformeService informeService;

    //carga pdf
    //editar pdf
    //verpdf

    @PostMapping("/upload")
    public ResponseEntity<String> cargarPDF(@RequestParam("file") MultipartFile multipartFile {
        

    }
}
