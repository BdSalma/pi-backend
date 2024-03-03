package tn.examen.templateexamen2324.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.examen.templateexamen2324.entity.Category;
import tn.examen.templateexamen2324.entity.Offer;
import tn.examen.templateexamen2324.entity.Society;
import tn.examen.templateexamen2324.entity.User;
import tn.examen.templateexamen2324.services.OfferService;

import java.util.List;
@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/Offer")
public class OfferController {
    @Autowired
    OfferService offerService;
    @PostMapping("/add-offer/{idSociety}")
    @ResponseBody
    public void affectOffer(@RequestBody Offer o, @PathVariable("idSociety") String id) {

        offerService.affecetOfferToSociety(o,id);
    }
    @GetMapping("/allOffers/{idSociety}")
    @ResponseBody
    public List<Offer> getOfferBySociete(@PathVariable("idSociety") String id) {

        List<Offer> listOffers = offerService.getOfferBySociety(id);

        return listOffers;
    }
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
    @GetMapping("/society/{idSociety}")
    @ResponseBody
    public User getSociety(@PathVariable("idSociety") String id) {
        return offerService.getSociety(id);

    }
    @GetMapping("/AcceptedOffer")
    @ResponseBody
    public List<Offer> getAcceptedOffer() {
        return offerService.getAcceptedOffer();

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
    public Offer updateOffer(@PathVariable("id") Long id, @RequestBody Offer updatedOffer) {
        Offer existingOffer = offerService.getOfferById(id);

        if (existingOffer!=null) {
            existingOffer.setCandidatnumber(updatedOffer.getCandidatnumber());
            existingOffer.setDescription(updatedOffer.getDescription());
            existingOffer.setDuree(updatedOffer.getDuree());
            existingOffer.setOffreCategory(updatedOffer.getOffreCategory());
            existingOffer.setCandidatProfil(updatedOffer.getCandidatProfil());


            offerService.updateOffer(existingOffer.getIdOffre());
        }
        return updatedOffer;

    }
    @GetMapping("/getofferByCategory/{category}")
    @ResponseBody
    public List<Offer> getOffer(@PathVariable("category") String categorie) {
        Category category = Category.valueOf(categorie);
        return offerService.getOfferByCategory(category);

    }

    @GetMapping("/Offer/filterByCriteria/{criteria}")
    public List<Offer> filterByCriteria(@PathVariable("criteria") String criteria) {
        // Implement filtering logic based on criteria and return filtered offers
        List<Offer> filteredOffers = offerService.filterOffersByInput(criteria);
        return filteredOffers;
    }
    @PostMapping("/Accept/{idOffer}")
    public void Accept(@PathVariable("idOffer") Long idOffer) {
                offerService.changeEtatToApprouvé(idOffer);
    }
    @PostMapping("/Refuse/{idOffer}")
    public void Refuse(@PathVariable("idOffer") Long idOffer) {
        offerService.changeEtatToRefuse(idOffer);
    }
}
