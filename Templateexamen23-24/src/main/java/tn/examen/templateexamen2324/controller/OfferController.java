package tn.examen.templateexamen2324.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.examen.templateexamen2324.entity.Offer;
import tn.examen.templateexamen2324.services.OfferService;

import java.util.List;

@RestController
@RequestMapping("/Offer")
public class OfferController {
    @Autowired
    OfferService offerService;

    @GetMapping("/allOffers")
    @ResponseBody
    public List<Offer> getOffers() {

        List<Offer> listOffers = offerService.getOffers();

        return listOffers;

    }
    @GetMapping("/offer/{idOffer}")
    @ResponseBody
    public Offer getOfferById(@PathVariable("idOffer") Long id) {
        return offerService.getOfferById(id);

    }
    @PostMapping("/add-offer")
    @ResponseBody
    public Offer addOffer(@RequestBody Offer o){
        return offerService.addOffer(o);
    }
    @DeleteMapping("/deleteOffer/{idOffer}")
    @ResponseBody
    public void deleteOffer(@PathVariable("idOffer") Long idOffer) {
        offerService.deleteOffer(idOffer);
    }
    @PutMapping("/updateOffer/{id}")
    @ResponseBody
    public ResponseEntity<String> updateOffer(@PathVariable("id") Long id, @RequestBody Offer updatedOffer) {
        Offer existingOffer = offerService.getOfferById(id);

        if (existingOffer!=null) {
            existingOffer.setCandidatnumber(updatedOffer.getCandidatnumber());
            existingOffer.setDescription(updatedOffer.getDescription());
            existingOffer.setDuree(updatedOffer.getDuree());
            existingOffer.setOffreCategory(updatedOffer.getOffreCategory());
            existingOffer.setCandidatProfil(updatedOffer.getCandidatProfil());


            offerService.updateOffer(existingOffer.getIdOffre());
            return ResponseEntity.ok("L'offre a été mis à jour avec succès.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("L'offre avec l'ID " + id + " n'existe pas.");
        }
    }}
