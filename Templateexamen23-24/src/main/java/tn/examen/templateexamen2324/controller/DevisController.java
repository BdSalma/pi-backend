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
public class DevisController {
    @Autowired
    DevisIService devisIService;
    @PostMapping("/addInvoice")
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

    @DeleteMapping("/deleteDevis/{id}")
    @ResponseBody
    public void deleteInvoice(@PathVariable("id") int idDevis) {

        devisIService.deleteDevis(idDevis);
    }

    @PutMapping("/updateDevis/{id}")
    @ResponseBody
    public Devis updateInvoice(@PathVariable("id") int idDevis, @RequestBody Devis devis) {
        return devisIService.updateDevis(idDevis, devis);
    }

    @PostMapping("/assignToRequest")
    public Devis assignInvoiceToRequest(
            @RequestBody Devis devis,
            @RequestParam("requestSupplyId") int requestSupplyId
    ) {
        return devisIService.createDevisAndAssignToRequest(devis, requestSupplyId);
    }
}
