package tn.examen.templateexamen2324.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tn.examen.templateexamen2324.entity.Candidature;
import tn.examen.templateexamen2324.entity.EtatInterview;
import tn.examen.templateexamen2324.entity.Interview;
import tn.examen.templateexamen2324.entity.Offer;
import tn.examen.templateexamen2324.services.IOfferService;
import tn.examen.templateexamen2324.services.IinterviewService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/interview")
public class InterviewController {
    @Autowired
    IinterviewService service;
    @Autowired
    IOfferService offerService;
    @GetMapping("/{id}")
    public ResponseEntity<Interview> getById(@PathVariable Long id) {
        Interview interview = service.FindInterviewById(id);
        if (interview != null) {
            return ResponseEntity.ok(interview);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/addI/{id}")
    //@PreAuthorize("hasRole('Exposant') Or hasRole('Student') ")
    public ResponseEntity<Interview> ajouterInterview(
            @RequestBody Interview i,
            @PathVariable Long id, // Utilisez @RequestParam pour les paramètres de requête
            @RequestParam(name = "room",required = false)int roomId) {
        Interview nouveauI = service.addInter(i, id, roomId);
        return new ResponseEntity<>(nouveauI, HttpStatus.CREATED);
    }
    @PostMapping("/ajouterI/{id}")
    //@PreAuthorize("hasRole('Exposant') Or hasRole('Student') ")
    public ResponseEntity<Interview> ajouterInter(
            @RequestBody Interview i,
            @PathVariable Long id ) {
        Interview nouveauI = service.addInterview(i, id);
        return new ResponseEntity<>(nouveauI, HttpStatus.CREATED);
    }
    @PostMapping("/addIEnligne/{id}")
    //@PreAuthorize("hasRole('Exposant') Or hasRole('Student') ")
    public ResponseEntity<Interview> ajouterInterviewEnligne(
            @RequestBody Interview i,
            @PathVariable Long id) {
        Interview nouveauI = service.addInterEnligne(i, id);

         return new ResponseEntity<>(nouveauI, HttpStatus.CREATED);
    }

    @DeleteMapping("/deleteI/{id}")
    public void deleteInterview(@PathVariable("id") Long id) {
        service.deleteById(id);
    }
    @GetMapping("/allInterview")
    //@PreAuthorize("hasRole('Exposant') Or hasRole('Student') ")
    public List<Interview>  GetAllInterviews(){
        return service.getInterviews();
    }
    @PutMapping("/updateI/{id}")
    public ResponseEntity<Interview> UpdateInterv(@RequestBody Interview i, @PathVariable  Long id) {
        Interview candidatureMaj = service.updateInterview(id, i);
        if (candidatureMaj != null) {
            return new ResponseEntity<>(candidatureMaj, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @PutMapping("/updateIR/{id}")
    public ResponseEntity<Interview> updateInterview(@RequestBody Interview i, @PathVariable Long id, @RequestParam(name = "room", required = false) Integer roomId) {
        try {
            Interview updatedInterview = service.updateInterviewR(id, i, roomId);
            if (updatedInterview != null) {
                return ResponseEntity.ok(updatedInterview);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/byEtatAndOffer/{etat}/{offerId}")
    public ResponseEntity<List<Interview>> getInterviewsByEtatAndOffer(
            @PathVariable("etat") EtatInterview etat,
            @PathVariable("offerId") Long offerId
    ) {
        try {
            // Récupérer l'offre par son ID
            Offer offer = offerService.getOfferById(offerId);
            if (offer == null) {
                return ResponseEntity.notFound().build();
            }

            // Appeler la méthode du service pour récupérer les interviews par état et offre
            List<Interview> interviews = service.getInterviewsByEtatInterviewetOffer(etat, offer);
            return ResponseEntity.ok(interviews);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    @PutMapping("/valider/{id}")
    public ResponseEntity<Interview> AccepterInterv( Interview candidature, @PathVariable  Long id) throws Exception {
        Interview candidatureMaj = service.AccepterCandidature(id, candidature);
        if (candidatureMaj != null) {
            return new ResponseEntity<>(candidatureMaj, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}