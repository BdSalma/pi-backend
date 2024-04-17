package tn.examen.templateexamen2324.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import tn.examen.templateexamen2324.entity.*;
import tn.examen.templateexamen2324.services.OfferService;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/Offer")
public class OfferController {
    @Autowired
    OfferService offerService;

    @PostMapping("/add-offer")
    @PreAuthorize("hasRole('Exposant')")
    @ResponseBody
    public ResponseEntity<String> affectOffer(@RequestBody Offer o, Authentication authentication) {
        Jwt jwtToken = (Jwt) authentication.getPrincipal();
        String userId = jwtToken.getClaim("sub");
        // Appeler le service pour affecter l'offre à la société
        return offerService.affecetOfferToSociety(o, userId);
    }

    @GetMapping("/allOffersBySociety")
    @PreAuthorize("hasRole('Exposant')")
    @ResponseBody
    public List<Offer> getOfferBySociete(Authentication authentication) {
        Jwt jwtToken = (Jwt) authentication.getPrincipal();
        String userId = jwtToken.getClaim("sub");
        List<Offer> listOffers = offerService.getOfferBySociety(userId);
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

    @GetMapping("/society")
    @ResponseBody
    public User getSociety(Authentication authentication) {
        Jwt jwtToken = (Jwt) authentication.getPrincipal();
        String userId = jwtToken.getClaim("sub");
        return offerService.getSociety(userId);

    }
    @GetMapping("/test-send-offers")
    public String testSendOffers() {
        offerService.sentOffers();
        return "Sent offers task executed.";
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
    @DeleteMapping("/deleteFavoris/{idOffer}")
    @ResponseBody
    public void deleteFavoris(@PathVariable("idOffer") Long idOffer) {
        offerService.deletefavorite(idOffer);
    }
    @PutMapping("/updateOffer/{id}")
    @PreAuthorize("hasRole('Exposant')")
    @ResponseBody
    public Offer updateOffer(@PathVariable("id") Long id, @RequestBody Offer updatedOffer) {
        Offer existingOffer = offerService.getOfferById(id);

        if (existingOffer != null) {
            existingOffer.setOfferName(updatedOffer.getOfferName());
            existingOffer.setOffreCategory(updatedOffer.getOffreCategory());
            existingOffer.setCandidatnumber(updatedOffer.getCandidatnumber());
            existingOffer.setCandidatProfil(updatedOffer.getCandidatProfil());
            existingOffer.setDuree(updatedOffer.getDuree());
            existingOffer.setDescription(updatedOffer.getDescription());




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
        List<Offer> filteredOffers = offerService.filterOffersByInput(criteria);
        return filteredOffers;
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
    @PostMapping("/AddFavoris/{idOffer}")
    public Offer AddFavoris(@PathVariable("idOffer") Long idOffer,Authentication authentication) {
        Jwt jwtToken = (Jwt) authentication.getPrincipal();
        String userId = jwtToken.getClaim("sub");
        return offerService.favoris(userId,idOffer);
    }
    @GetMapping("/getOfferFavoris")
    public List<OfferFavoris> getFavoriteOffersByUserIddd(Authentication authentication) {
        Jwt jwtToken = (Jwt) authentication.getPrincipal();
        String userId = jwtToken.getClaim("sub");
        return offerService.getFavoriteOffersByUserId(userId);
    }
    @GetMapping("/categoryCounts")
    public Map<Category, Long> getOfferCountsByCategory() {
        return offerService.getOfferCountsByCategory();
    }

    @GetMapping("/candidatures-by-offer")
    public Map<String, Long> getCandidaturesByOffer() {
        Map<Offer, Long> candidaturesByOffer = offerService.countCandidaturesByOffer();
        Map<String, Long> candidaturesByOfferNames = new HashMap<>();

        // Convert Offer objects to offer names
        for (Map.Entry<Offer, Long> entry : candidaturesByOffer.entrySet()) {
            candidaturesByOfferNames.put(entry.getKey().getOfferName(), entry.getValue());
        }

        return candidaturesByOfferNames;
    }
    @GetMapping("/candidatByOffer/{idOffer}")
    public boolean getCandidatureByOffer(@PathVariable("idOffer") Long idOffer,Authentication authentication) {
        Jwt jwtToken = (Jwt) authentication.getPrincipal();
        String userId = jwtToken.getClaim("sub");
        return offerService.getCandidatureByOffer(idOffer,userId);
    }

}