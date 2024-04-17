package tn.examen.templateexamen2324.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tn.examen.templateexamen2324.entity.Devis;
import tn.examen.templateexamen2324.entity.Invoice;
import tn.examen.templateexamen2324.services.InvoiceIService;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Map;

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
    public List<Invoice> getInvoices(Authentication authentication) {
        Jwt jwtToken = (Jwt) authentication.getPrincipal();
        String userId = jwtToken.getClaim("sub");
        List<Invoice> listInvoices = invoiceIService.retrieveAllInvoices(userId);
        return listInvoices;
    }
    @GetMapping("/retrieveOldInvoices")
    @ResponseBody
    public List<Invoice> getOldInvoices(Authentication authentication) {
        Jwt jwtToken = (Jwt) authentication.getPrincipal();
        String userId = jwtToken.getClaim("sub");
        List<Invoice> listInvoices = invoiceIService.retrieveOldInvoicesByUserId(userId);
        return listInvoices;
    }

    @DeleteMapping("/deleteInvoice/{id}")
    @ResponseBody
    public void deleteInvoice(@PathVariable("id") int idInvoice) {

        invoiceIService.deleteInvoice(idInvoice);
    }
    @GetMapping("/getInvoice/{id}")
    @ResponseBody
    public Invoice getInvoiceById(@PathVariable("id") int idInvoice) {
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
            @RequestParam("invoice") String invoiceJson,
            @PathVariable("requestSupplyId") int requestSupplyId,
            @RequestParam("file") MultipartFile file
            ) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        Invoice invoice = objectMapper.readValue(invoiceJson, Invoice.class);
        invoiceIService.addAndAssignInvoiceToRequest(invoice,requestSupplyId,file);
    }
    @GetMapping("/getInvoicesBySociety")
    @ResponseBody
    public List<Invoice> getInvoicesBySocietyId(Authentication authentication) {
        Jwt jwtToken = (Jwt) authentication.getPrincipal();
        String userId = jwtToken.getClaim("sub");
        List<Invoice> invoices = invoiceIService.getInvoicesBySocietyId(userId);
        return invoices;
    }

    @GetMapping("/getOldInvoicesBySociety")
    @ResponseBody
    public List<Invoice> getOLdInvoicesBySocietyId(Authentication authentication) {
        Jwt jwtToken = (Jwt) authentication.getPrincipal();
        String userId = jwtToken.getClaim("sub");
        List<Invoice> invoices = invoiceIService.getOldInvoicesBySocietyId(userId);
        return invoices;
    }

    @GetMapping("/file/{fileName}")
    public ResponseEntity<byte[]> getFileContent(@PathVariable String fileName) throws IOException {
        byte[] fileBytes = invoiceIService.getFileBytes(fileName);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM); // Set content type for downloads
        headers.setContentDisposition(ContentDisposition.attachment()
                .filename(fileName).build()); // Set filename in response
        return new ResponseEntity<>(fileBytes, headers, HttpStatus.OK);
    }
    @GetMapping("/calculateTotalAmountByIndividu")
    public Map<String, Float> calculateTotalAmountByIndividu(Authentication authentication) {
        Jwt jwtToken = (Jwt) authentication.getPrincipal();
        String userId = jwtToken.getClaim("sub");
        return invoiceIService.calculateTotalAmountByIndividu(userId);
    }



}
