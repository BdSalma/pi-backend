package tn.examen.templateexamen2324.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tn.examen.templateexamen2324.entity.Devis;
import tn.examen.templateexamen2324.entity.DevisStatus;
import tn.examen.templateexamen2324.entity.Invoice;
import tn.examen.templateexamen2324.services.DevisIService;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins="http://localhost:4200")
@RestController
@RequestMapping("/devis")
public class DevisController  {
    @Autowired
    DevisIService devisIService;
    @PostMapping("/addDevis")
    public ResponseEntity<Devis> ajouterDevis(@RequestBody Devis i) {
        Devis devis = devisIService.addDevis(i);
        return new ResponseEntity<>(devis, HttpStatus.CREATED);
    }
    @GetMapping("/retrieveAllDevis")
    @ResponseBody
    public List<Devis> getDevis() {
        List<Devis> listDevis = devisIService.retrieveAllDevis();
        return listDevis;
    }
    @GetMapping("/getDevis/{id}")
    @ResponseBody
    public Devis getDevisById(@PathVariable("id") int idDevis) {
        return devisIService.retrieveDevisById(idDevis);
    }
    @DeleteMapping("/deleteDevis/{id}")
    @ResponseBody
    public void deleteInvoice(@PathVariable("id") int idDevis) {

        devisIService.deleteDevis(idDevis);
    }
    @PutMapping("/updateDevis/{id}")
    @ResponseBody
    public Devis updateDevis(@PathVariable("id") int id, @RequestBody Devis updatedDevis) {
        Devis existingDevis = devisIService.retrieveDevisById(id);

            existingDevis.setFile(updatedDevis.getFile());
            existingDevis.setDescription(updatedDevis.getDescription());
            existingDevis.setPrice(updatedDevis.getPrice());
            existingDevis.setQuantity(updatedDevis.getQuantity());
            return devisIService.updateDevis(existingDevis.getId());

    }

    @GetMapping("/getDevisByRequestSupply/{requestSupplyId}")
    public List<Devis> getDevisByRequestSupply(@PathVariable("requestSupplyId") int requestSupplyId) {
        return devisIService.getDevisByRequestSupply(requestSupplyId);
    }
    @PostMapping("/createDevisAndAssignToRequest/{requestId}")
    @ResponseBody
    public Devis createDevisAndAssignToRequest(
            @PathVariable("requestId") int requestSupplyId,
            @RequestParam("devis") String devisJson,
            @RequestParam("file") MultipartFile file,
            Authentication authentication
    ) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        Devis devis = objectMapper.readValue(devisJson, Devis.class);

        Jwt jwtToken = (Jwt) authentication.getPrincipal();
        String userId = jwtToken.getClaim("sub");
        return devisIService.createDevisAndAssignToRequest(devis, requestSupplyId, userId, file);
    }

    @GetMapping("/getDevisBySociety")
    public List<Devis> getDevisBySociety(Authentication authentication) {
        Jwt jwtToken = (Jwt) authentication.getPrincipal();
        String userId = jwtToken.getClaim("sub");
        return devisIService.getDevisBySociety(userId);
    }
    @GetMapping("/getOldDevisBySociety")
    public List<Devis> getOldDevisBySociety(Authentication authentication) {
        Jwt jwtToken = (Jwt) authentication.getPrincipal();
        String userId = jwtToken.getClaim("sub");
        return devisIService.getOldDevisBySociety(userId);
    }
    @PutMapping("/updateDevisStatus/{id}/{newStatus}")
    @ResponseBody
    public Devis updateDevisStatus(@PathVariable("id") int id, @PathVariable("newStatus") DevisStatus status) {
        return devisIService.updateDevisStatus(id, status);
    }

    @GetMapping("/file/{fileName}")
    public ResponseEntity<byte[]> getFileContent(@PathVariable String fileName) throws IOException {
        byte[] fileBytes = devisIService.getFileBytes(fileName);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM); // Set content type for downloads
        headers.setContentDisposition(ContentDisposition.attachment()
                .filename(fileName).build()); // Set filename in response
        return new ResponseEntity<>(fileBytes, headers, HttpStatus.OK);
    }

    // Calculate the total amount of money for accepted devis by each individu

}
