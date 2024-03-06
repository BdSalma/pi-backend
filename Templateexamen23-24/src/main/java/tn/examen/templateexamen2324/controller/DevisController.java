package tn.examen.templateexamen2324.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.examen.templateexamen2324.entity.Devis;
import tn.examen.templateexamen2324.entity.Invoice;
import tn.examen.templateexamen2324.services.DevisIService;

import java.util.List;
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
    @PostMapping("/createDevisAndAssignToRequest/{requestId}/{idS}")
    @ResponseBody
    public Devis createDevisAndAssignToRequest(@PathVariable("requestId") int requestSupplyId, @RequestBody Devis devis,@PathVariable("idS") String id){
        return devisIService.createDevisAndAssignToRequest(devis,requestSupplyId,id);
    }
    @GetMapping("/getDevisBySociety/{idS}")
    public List<Devis> getDevisBySociety(@PathVariable("idS") String societyId) {

        return devisIService.getDevisBySociety(societyId);
    }
    @PutMapping("/updateDevisStatus/{id}/{newStatus}")
    @ResponseBody
    public Devis updateDevisStatus(@PathVariable("id") int id, @PathVariable("newStatus") boolean newStatus) {
        return devisIService.updateDevisStatus(id, newStatus);
    }


}
