package tn.examen.templateexamen2324.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import tn.examen.templateexamen2324.entity.Reclamation;
import tn.examen.templateexamen2324.entity.TypeReclamation;
import tn.examen.templateexamen2324.services.IReclamationService;

import java.util.List;

@RestController
@CrossOrigin(origins="http://localhost:4200")
@RequestMapping("/reclamation")
public class ReclamationController {
    @Autowired
    IReclamationService iReclamationService;

    @GetMapping("/retrieve")
    @ResponseBody
    public ResponseEntity<List<Reclamation>> getReclamations() {
        List<Reclamation> reclamationList = iReclamationService.getAllReclamation();
        return ResponseEntity.ok().body(reclamationList);
    }
    @GetMapping("/feed")
    @ResponseBody
    public ResponseEntity<List<Reclamation>> getFeed() {
        TypeReclamation reclamationType = TypeReclamation.Feed;
        List<Reclamation> reclamationList = iReclamationService.getFeed(reclamationType);
        return ResponseEntity.ok().body(reclamationList);
    }
    @PostMapping("/create")
    @ResponseBody
    public Reclamation publishReclamation(@RequestBody Reclamation r, Authentication authentication) {
        Jwt jwtToken = (Jwt) authentication.getPrincipal();
        String userId = jwtToken.getClaim("sub");
        Reclamation reclamation = iReclamationService.publishReclamation(r,userId);
        return reclamation;
    }
    @GetMapping("/find-type/{typeR}")
    @ResponseBody
    public List<Reclamation> getReclamationType(@PathVariable("typeR")TypeReclamation typeR) {
        List<Reclamation> reclamationList = iReclamationService.getReclamationType(typeR);
        return reclamationList;
    }
    @GetMapping("/find-user/{id}")
    @ResponseBody
    public List<Reclamation> getReclamationUser(@PathVariable("id") int id) {
        List<Reclamation> reclamationList = iReclamationService.getReclamationsByUser(id);
        return reclamationList;
    }
    @GetMapping("/find-reclamation/{id}")
    @ResponseBody
    public Reclamation getReclamationById(@PathVariable("id") int id) {
        Reclamation reclamation = iReclamationService.getReclamationsById(id);
        return reclamation;
    }
    @DeleteMapping("/delete/{id}")
    public void DeleteReclamation(@PathVariable("id") int id){
        iReclamationService.DeleteReclamation(id
        );
    }

    @PostMapping("/review/{id}")
    @ResponseBody
    public void Review(@PathVariable("id") String id){
        iReclamationService.Review(id);
    }
}
