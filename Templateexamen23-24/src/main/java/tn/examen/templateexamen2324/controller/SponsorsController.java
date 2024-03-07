package tn.examen.templateexamen2324.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import tn.examen.templateexamen2324.entity.Sponsors;
import tn.examen.templateexamen2324.services.SponsorsService;

import java.util.List;


@RestController
@RequestMapping("/api/sponsors")
@CrossOrigin(origins="http://localhost:4200") // Ajout de @CrossOrigin au niveau de classe

public class SponsorsController {

    @Autowired
    private SponsorsService sponsorsService;



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
}