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
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/candidat")
public class CandidatureController {
    @Autowired
    ICandidatureService candiService;

    @GetMapping("/allCandidat")
    public List<Candidature> GetAllCandidats(){
        return candiService.findAllCadidature();
    }
    @GetMapping("/candidatbyoffer/{id}")
    public List<Candidature> FindCandidatByIdOffer(@PathVariable Long id){
        return candiService.FindCandidatByIdOffer(id);
    }
    @GetMapping("/candidatbyuser/{idUser}")
    public List<Candidature> FindCandidatByIdUser(@PathVariable String idUser){
        return candiService.FindCandidatByIdUser(idUser);
    }
    @PostMapping("/addcandidat/{id}/{idUser}")
    public ResponseEntity<Candidature> ajouterCandidat(@RequestBody Candidature candidature, @PathVariable  Long id,@PathVariable  Long idUser) {
        Candidature nouveauC = candiService.addCandidat(candidature, id,idUser);
        return new ResponseEntity <>(nouveauC, HttpStatus.CREATED);
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
    @PutMapping("/updateCandidat/{id}")
    public ResponseEntity<Candidature> UpdateCandidat(@RequestBody Candidature candidature, @PathVariable  Long id) {
        Candidature candidatureMaj = candiService.updateCandidature(id, candidature);
        if (candidatureMaj != null) {
            return new ResponseEntity<>(candidatureMaj, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


}
