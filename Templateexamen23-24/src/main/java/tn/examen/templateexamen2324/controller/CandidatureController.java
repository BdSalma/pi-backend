package tn.examen.templateexamen2324.controller;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tn.examen.templateexamen2324.entity.Candidature;
import tn.examen.templateexamen2324.services.ICandidatureService;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
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

    @GetMapping("/candidatbyuser")
    public List<Candidature> FindCandidatByIdUser(Authentication authentication){
        Jwt jwtToken = (Jwt) authentication.getPrincipal();
        String userId = jwtToken.getClaim("sub");
        return candiService.FindCandidatByIdUser(userId);
    }
    @GetMapping("/download-cv/{id}")
    public ResponseEntity<byte[]> downloadCv(@PathVariable Long id, HttpServletRequest request) throws Exception {
        // Implement authentication and authorization logic (not shown)
        // ...

        // Validate candidate ID
        if (id <= 0) {
            throw new BadRequestException("Invalid candidate ID");
        }

        // Retrieve candidate information and CV file path
        Candidature candidate = candiService.FindCandidatById(id); // Replace with your logic
        if (candidate == null) {
            throw new ClassNotFoundException("Candidate not found");
        }

        String cvFilePath = "C:/Users/Salma/IdeaProjects/pi/pi-backend/Templateexamen23-24/src/main/resources/fils/" + candidate.getCv(); // Adjust path if needed

        // Read CV data as byte array
        File cvFile = new File(cvFilePath);
        if (!cvFile.exists()) {
            throw new FileNotFoundException("CV file not found");
        }
        byte[] cvData = Files.readAllBytes(Paths.get(cvFilePath));

        // Set content type and disposition headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDisposition(ContentDisposition.attachment()
                .filename(candidate.getCv() + ".pdf") // Adjust file extension as needed
                .build());

        return new ResponseEntity<>(cvData, headers, HttpStatus.OK);
    }


    @PostMapping("/addcandidat/{id}")
    public ResponseEntity<Candidature> ajouterCandidat(
            @RequestParam MultipartFile cv, // Change to @RequestParam
            @PathVariable Long id, @RequestParam MultipartFile lettre, Authentication authentication) throws IOException {
        Jwt jwtToken = (Jwt) authentication.getPrincipal();
        String userId = jwtToken.getClaim("sub");
        // Create a new Candidature object
        Candidature candidature = new Candidature();
        // Call the service method to add the candidature
        Candidature savedCandidature = candiService.addCandidat(candidature, id, userId, cv,lettre);

        // Return the saved candidature with CREATED status
        return new ResponseEntity<>(savedCandidature, HttpStatus.CREATED);
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

    @PutMapping("/accepterCandidat/{id}")
    public ResponseEntity<Candidature> AccepterCandidat( Candidature candidature, @PathVariable  Long id) throws Exception {
        Candidature candidatureMaj = candiService.AccepterCandidature(id, candidature);
        if (candidatureMaj != null) {
            return new ResponseEntity<>(candidatureMaj, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
