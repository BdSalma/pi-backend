package tn.examen.templateexamen2324.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
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

    @PostMapping("/add-offer")
    @PreAuthorize("hasRole('Exposant')")
    @ResponseBody
    public void affectOffer(@RequestBody Offer o, Authentication authentication) {
        Jwt jwtToken = (Jwt) authentication.getPrincipal();
        String userId = jwtToken.getClaim("sub");
        offerService.affecetOfferToSociety(o, userId);
    }

    @GetMapping("/allOffersBySociety")
    @PreAuthorize("hasRole('Exposant')")
    @ResponseBody
    public List<Offer> getOfferBySociete(Authentication authentication) {
        Jwt jwtToken = (Jwt) authentication.getPrincipal();
        String userId = jwtToken.getClaim("sub");
        return offerService.getOfferBySociety(userId);
    }

    @GetMapping("/allOffers")

    @ResponseBody
    public List<Offer> getOffers() {

        return offerService.getOffers();
    }
    @GetMapping("/offer/{idOffer}")
    @PreAuthorize("hasRole('Exposant')")
    @ResponseBody
    public Offer getOfferById(@PathVariable("idOffer") Long id) {
        return offerService.getOfferById(id);

    }

    @GetMapping("/society")
    @ResponseBody
    public User getSociety(Authentication authentication) {
        Jwt jwtToken = (Jwt) authentication.getPrincipal();
        String userId = jwtToken.getClaim("sub");
        return offerService.getSociety(userId);

    }

    @GetMapping("/AcceptedOffer")
    @ResponseBody
    public List<Offer> getAcceptedOffer() {
        return offerService.getAcceptedOffer();

    }

    @DeleteMapping("/deleteOffer/{idOffer}")
    @PreAuthorize("hasRole('Exposant')")
    @ResponseBody
    public void deleteOffer(@PathVariable("idOffer") Long idOffer) {
        offerService.deleteOffer(idOffer);
    }

    @PutMapping("/updateOffer/{id}")
    @PreAuthorize("hasRole('Exposant')")
    @ResponseBody
    public Offer updateOffer(@PathVariable("id") Long id, @RequestBody Offer updatedOffer) {
        Offer existingOffer = offerService.getOfferById(id);

        if (existingOffer != null) {
            existingOffer.setCandidatnumber(updatedOffer.getCandidatnumber());
            existingOffer.setDescription(updatedOffer.getDescription());
            existingOffer.setDuree(updatedOffer.getDuree());
            existingOffer.setOffreCategory(updatedOffer.getOffreCategory());
            existingOffer.setCandidatProfil(updatedOffer.getCandidatProfil());
            existingOffer.setOfferName(updatedOffer.getOfferName());


            offerService.updateOffer(existingOffer.getIdOffre());
        }
        return updatedOffer;

    }

    @GetMapping("/getofferByCategory/{category}")
    @PreAuthorize("hasRole('Exposant') Or hasRole('Admin')  ")
    @ResponseBody
    public List<Offer> getOffer(@PathVariable("category") String categorie,Authentication authentication) {
        Jwt jwtToken = (Jwt) authentication.getPrincipal();
        String userId = jwtToken.getClaim("sub");
        Category category = Category.valueOf(categorie);
        return offerService.getOfferByCategory(category,userId);

    }

    @GetMapping("/Offer/filterByCriteria/{criteria}")
    public List<Offer> filterByCriteria(@PathVariable("criteria") String criteria) {
        // Implement filtering logic based on criteria and return filtered offers
        return offerService.filterOffersByInput(criteria);
    }

    @PostMapping("/Accept/{idOffer}")
    @PreAuthorize("hasRole('Admin')")
    public void Accept(@PathVariable("idOffer") Long idOffer) {
        offerService.changeEtatToApprouvé(idOffer);
    }

    @PostMapping("/Refuse/{idOffer}")
    @PreAuthorize("hasRole('Admin')")
    public void Refuse(@PathVariable("idOffer") Long idOffer) {
        offerService.changeEtatToRefuse(idOffer);
    }

    @GetMapping("/averageOffersPerDay")
    public ResponseEntity<Double> getAverageOffersPerDay() {
        double average = offerService.calculateAverageOffersPerDay();
        return ResponseEntity.ok(average);
    }
    @GetMapping("/ListAcceptedOffer")
    @PreAuthorize("hasRole('Exposant')")
    public List<Offer> getOfferEnAttente() {
        return offerService.getOfferEnAttente();
    }
    @GetMapping("/nbAcceptedOffer")
    @PreAuthorize("hasRole('Admin')")
    public int numberOffersEnAttente() {
        return offerService.numberOffersEnAttente();
    }
}