package tn.examen.templateexamen2324.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    public Sponsors createSponsor(@RequestBody Sponsors sponsor) {
        return sponsorsService.saveSponsor(sponsor);
    }

    @GetMapping
    public List<Sponsors> getAllSponsors() {
        return sponsorsService.getAllSponsors();
    }

    @GetMapping("/{id}")
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
    public ResponseEntity<Void> deleteSponsor(@PathVariable Long id) {
        sponsorsService.deleteSponsor(id);
        return ResponseEntity.ok().build();
    }
}