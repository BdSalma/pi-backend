package tn.examen.templateexamen2324.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @PutMapping("/updateInvoice/{id}")
    @ResponseBody
    public Invoice updateInvoice(@PathVariable("id") int idInvoice, @RequestBody Invoice invoice) {
        return invoiceIService.updateInvoice(idInvoice, invoice);
    }
    @PostMapping("/assignToRequest")
    public void assignInvoiceToRequest(
            @RequestBody Invoice invoice,
            @RequestParam("requestSupplyId") int requestSupplyId
    ) {
        invoiceIService.assignInvoiceToRequest(invoice, requestSupplyId);
    }
}
