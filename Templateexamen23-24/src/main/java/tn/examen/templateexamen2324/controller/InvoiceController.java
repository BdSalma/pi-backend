package tn.examen.templateexamen2324.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import tn.examen.templateexamen2324.entity.Invoice;
import tn.examen.templateexamen2324.services.InvoiceIService;
import java.util.List;
@CrossOrigin(origins="http://localhost:4200")
@RestController
@RequestMapping("/invoice")
public class InvoiceController {
    @Autowired
    InvoiceIService invoiceIService;
    @PostMapping("/addInvoice")
    public ResponseEntity<Invoice> ajouterInvoice(@RequestBody Invoice i) {
        Invoice invoice = invoiceIService.addInvoice(i);
        return new ResponseEntity<>(invoice, HttpStatus.CREATED);
     }
    @GetMapping("/retrieveAllInvoices")
    @ResponseBody
    public List<Invoice> getInvoices() {
        List<Invoice> listInvoices = invoiceIService.retrieveAllInvoices();
        return listInvoices;
    }

    @DeleteMapping("/deleteInvoice/{id}")
    @ResponseBody
    public void deleteInvoice(@PathVariable("id") int idInvoice) {

        invoiceIService.deleteInvoice(idInvoice);
    }
    @GetMapping("/getInvoice/{id}")
    @ResponseBody
    public Invoice getOfferById(@PathVariable("id") int idInvoice) {
        return invoiceIService.retrieveInvoiceById(idInvoice);

    }

    @PutMapping("/updateInvoice/{idInvoice}")
    @ResponseBody
    public Invoice updateInvoice(@PathVariable("idInvoice") int id, @RequestBody Invoice updatedInvoice) {
        Invoice existingInvoice = invoiceIService.retrieveInvoiceById(id);

        // Update only the status and comment fields
        existingInvoice.setStatus(updatedInvoice.getStatus());
        existingInvoice.setComment(updatedInvoice.getComment());

        return invoiceIService.updateInvoice(existingInvoice.getIdInvoice());
    }

    @PostMapping("/assignToRequest/{requestSupplyId}")
    public void assignInvoiceToRequest(
            @RequestBody Invoice invoice,
            @PathVariable("requestSupplyId") int requestSupplyId
    ) {
        invoiceIService.addAndAssignInvoiceToRequest(invoice, requestSupplyId);
    }
    @GetMapping("/getInvoicesBySociety")
    @ResponseBody
    public List<Invoice> getInvoicesBySocietyId(Authentication authentication) {
        Jwt jwtToken = (Jwt) authentication.getPrincipal();
        String userId = jwtToken.getClaim("sub");
        List<Invoice> invoices = invoiceIService.getInvoicesBySocietyId(userId);
        return invoices;
    }
}
