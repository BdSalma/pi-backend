package tn.examen.templateexamen2324.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.examen.templateexamen2324.entity.Candidature;
import tn.examen.templateexamen2324.services.ICandidatureService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/candidat")
public class CandidatureController {
    @Autowired
    ICandidatureService candiService;

    @GetMapping("/allCandidat")
    public List<Candidature> GetAllCandidats(){
        return candiService.findAllCadidature();
    }

    @PostMapping("/addcandidat")
    public ResponseEntity<Candidature> ajouterCandidat(@RequestBody Candidature candidature) {
        Candidature nouveauC = candiService.addCandidat(candidature);
        return new ResponseEntity<>(nouveauC, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Candidature> getById(@PathVariable Long id) {
        Candidature c= candiService.FindCandidatById(id);
        return ResponseEntity.ok(c);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteCandidat(@PathVariable("id") Long id) {
        candiService.deleteById(id);
    }

}
