package tn.examen.templateexamen2324.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import tn.examen.templateexamen2324.entity.Sponsors;
import tn.examen.templateexamen2324.services.SponsorViewService;
import tn.examen.templateexamen2324.services.SponsorsService;
import tn.examen.templateexamen2324.services.StripePaymentService;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/sponsors")
@CrossOrigin(origins="http://localhost:4200") // Ajout de @CrossOrigin au niveau de classe

public class SponsorsController {

    @Autowired
    private SponsorsService sponsorsService;

    @Autowired
    private SponsorViewService sponsorViewService; // Injection de SponsorViewService

    @Autowired
    private StripePaymentService stripePaymentService;

    @PostMapping
    // @PreAuthorize("hasRole('Exposant') OR hasRole('Fourniseur')")
    // @PreAuthorize("hasRole('Student') ")

    public Sponsors createSponsor(@RequestBody Sponsors sponsor, Authentication authentication) {
        Jwt jwtToken = (Jwt) authentication.getPrincipal();
        String userId = jwtToken.getClaim("sub");
        return sponsorsService.saveSponsor(sponsor,userId);
    }

    @GetMapping
    // @PreAuthorize("hasRole('Admin')")
    public List<Sponsors> getAllSponsors() {
        return sponsorsService.getAllSponsors();
    }

    @GetMapping("/{id}")
    // @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<Sponsors> getSponsorById(@PathVariable Long id) {
        Sponsors sponsor = sponsorsService.getSponsorById(id);
        return sponsor != null ? ResponseEntity.ok(sponsor) : ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")

    public ResponseEntity<Sponsors> updateSponsor(@PathVariable Long id, @RequestBody Sponsors sponsorDetails) {
        Sponsors updatedSponsor = sponsorsService.updateSponsor(id, sponsorDetails);
        return updatedSponsor != null ? ResponseEntity.ok(updatedSponsor) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    // @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<Void> deleteSponsor(@PathVariable Long id) {
        sponsorsService.deleteSponsor(id);
        return ResponseEntity.ok().build();
    }


    @PostMapping("/intent")
    public ResponseEntity<String> createPaymentIntent(@RequestParam double amount, @RequestParam String currency) {
        String clientSecret = stripePaymentService.createPaymentIntent(amount, currency);
        if (clientSecret != null) {
            return ResponseEntity.ok(clientSecret);
        } else {
            return ResponseEntity.badRequest().body("Erreur lors de la création de l'intention de paiement");
        }
    }


    @GetMapping("/{id}/details")
    public ResponseEntity<Sponsors> getSponsorByIdAndRecordView(@PathVariable Long id) {
        Sponsors sponsor = sponsorsService.getSponsorById(id);
        if (sponsor != null) {
            sponsorViewService.recordView(id); // Enregistrement de la vue
            return ResponseEntity.ok(sponsor);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @GetMapping("/most-viewed")
    public ResponseEntity<Sponsors> getMostViewedSponsor() {
        return sponsorViewService.getMostViewedSponsor()
                .map(ResponseEntity::ok) // Si l'Optional contient une valeur, utilisez-la pour construire une réponse OK.
                .orElseGet(() -> ResponseEntity.notFound().build()); // Sinon, construisez une réponse Not Found.
    }


}