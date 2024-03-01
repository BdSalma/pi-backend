package tn.examen.templateexamen2324.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.examen.templateexamen2324.entity.Interview;
import tn.examen.templateexamen2324.services.IinterviewService;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/interview")
public class InterviewController {
    @Autowired
    IinterviewService service;
    @PostMapping("/addI/{id}")

    public ResponseEntity<Interview> ajouterInterview(
            @RequestBody Interview i,
            @PathVariable Long id, // Utilisez @RequestParam pour les paramètres de requête
            @RequestParam(name = "room",required = false)int roomId) {
        Interview nouveauI = service.addInter(i, id, roomId);
        return new ResponseEntity<>(nouveauI, HttpStatus.CREATED);
    }

    @DeleteMapping("/deleteI/{id}")
    public void deleteInterview(@PathVariable("id") Long id) {
        service.deleteById(id);
    }
    @GetMapping("/allInterview")
    public List<Interview>  GetAllCandidats(){
        return service.getInterviews();
    }
}