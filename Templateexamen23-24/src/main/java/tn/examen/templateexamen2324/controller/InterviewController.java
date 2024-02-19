package tn.examen.templateexamen2324.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tn.examen.templateexamen2324.entity.Interview;
import tn.examen.templateexamen2324.services.IinterviewService;

@RestController
@RequestMapping("/interview")
public class InterviewController {
    @Autowired
    IinterviewService service;
    @PostMapping("/addI")
    public ResponseEntity<Interview> ajouterCandidat(@RequestBody Interview i) {
        Interview nouveauI = service.addInter(i);
        return new ResponseEntity<>(nouveauI, HttpStatus.CREATED);
    }
}
