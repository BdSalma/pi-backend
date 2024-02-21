package tn.examen.templateexamen2324.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.examen.templateexamen2324.entity.Interview;
import tn.examen.templateexamen2324.services.IinterviewService;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/interview")
public class InterviewController {
    @Autowired
    IinterviewService service;
    @PostMapping("/addI/{id}")
    public ResponseEntity<Interview> ajouterCandidat(@RequestBody Interview i,@PathVariable Long id) {
        Interview nouveauI = service.addInter(i,id);
        return new ResponseEntity<>(nouveauI, HttpStatus.CREATED);
    }
}