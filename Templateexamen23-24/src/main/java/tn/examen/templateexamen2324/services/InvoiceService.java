package tn.examen.templateexamen2324.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tn.examen.templateexamen2324.entity.Devis;
import tn.examen.templateexamen2324.entity.Invoice;
import tn.examen.templateexamen2324.entity.RequestSupply;
import tn.examen.templateexamen2324.entity.RequestSupplyStatus;
import tn.examen.templateexamen2324.repository.DevisRepository;
import tn.examen.templateexamen2324.repository.InvoiceRepository;
import tn.examen.templateexamen2324.repository.RequestSupplyRepository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class InvoiceService implements InvoiceIService {
    @Autowired
    InvoiceRepository invoiceRepository;
    @Autowired
    RequestSupplyRepository requestSupplyRepository;
    @Autowired
    DevisRepository devisRepository;

    @Override
    public Invoice addInvoice(Invoice invoice) {
        return invoiceRepository.save(invoice);
    }

    @Override
    public List<Invoice> retrieveAllInvoices() {
        // Retrieve all invoices
        List<Invoice> allInvoices = invoiceRepository.findAll();

        // Filter invoices where the status of the associated request is "running"
        return allInvoices.stream()
                .filter(invoice -> {
                    RequestSupply requestSupply = invoice.getRequestSupply();
                    return requestSupply != null && requestSupply.getStatus() == RequestSupplyStatus.Running;
                })
                .collect(Collectors.toList());
    }

    @Override
    public Invoice retrieveInvoiceById(int idInvoice) {

        return invoiceRepository.findById(idInvoice).orElse(null);
    }

    @Override
    public void deleteInvoice(int idInvoice) {
        this.invoiceRepository.deleteById(idInvoice);

    }

    @Override
    public Invoice updateInvoice(int idInvoice) {

        Invoice i = this.invoiceRepository.findById(idInvoice).orElse(null);
        return this.invoiceRepository.save(i);
    }


    @Override
    public void addAndAssignInvoiceToRequest(Invoice invoice, int requestSupplyId, MultipartFile file)throws IOException  {
        RequestSupply requestSupply = requestSupplyRepository.findById(requestSupplyId).orElse(null);
        if (requestSupply == null) {
            throw new IllegalArgumentException("RequestSupply with id " + requestSupplyId + " not found!");
        }

        // Check if the RequestSupply already has an invoice assigned
        if (requestSupply.getInvoice() != null) {
            throw new IllegalStateException("RequestSupply already has an invoice assigned!");
        }
        if (file != null) {
            String fileName = UUID.randomUUID().toString() + "." + getFileExtension(file);
            String uploadPath = "C:/Users/ASUS/IdeaProjects/pi-backend/Templateexamen23-24/src/main/resources/fils"; // Replace with your designated upload path
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }

            File uploadFile = new File(uploadPath + "/" + fileName);
            file.transferTo(uploadFile); // Save the file to the specified path
            invoice.setFile(fileName);// Store the file path in the Candidature object
        }
        // Set the invoice properties and save it
        this.invoiceRepository.save(invoice);

        // Assign the invoice to the RequestSupply and save it
        requestSupply.setInvoice(invoice);
        requestSupplyRepository.save(requestSupply);
    }
    private String getFileExtension(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }
    @Override
    public List<Invoice> getInvoicesBySocietyId(String societyId) {
        // Retrieve Devis entities associated with the given societyId
        List<Devis> devisList = devisRepository.findBySocietyDevisId(societyId);

        // Initialize a list to store invoices
        List<Invoice> invoices = new ArrayList<>();

        // Iterate over each Devis and add its associated invoice to the list
        for (Devis devis : devisList) {
            RequestSupply requestSupply = devis.getRequestSupply();
            if (requestSupply != null && requestSupply.getStatus()==RequestSupplyStatus.Running) {
                Invoice invoice = requestSupply.getInvoice();
                if (invoice != null) {
                    invoices.add(invoice);
                }
            }
        }

        return invoices;
    }

    @Override
    public List<Invoice> getOldInvoicesBySocietyId(String societyId) {
        // Retrieve Devis entities associated with the given societyId
        List<Devis> devisList = devisRepository.findBySocietyDevisId(societyId);

        // Initialize a list to store invoices
        List<Invoice> invoices = new ArrayList<>();

        // Iterate over each Devis and add its associated invoice to the list
        for (Devis devis : devisList) {
            RequestSupply requestSupply = devis.getRequestSupply();
            if (requestSupply != null && requestSupply.getStatus()==RequestSupplyStatus.Archived) {
                Invoice invoice = requestSupply.getInvoice();
                if (invoice != null) {
                    invoices.add(invoice);
                }
            }
        }

        return invoices;
    }
    public byte[] getFileBytes(String fileName) throws IOException {
        // Read the file from disk
        Path filePath = Paths.get("C:/Users/ASUS/IdeaProjects/pi-backend/Templateexamen23-24/src/main/resources/fils", fileName);
        return Files.readAllBytes(filePath);
    }
    @Override
    public List<Invoice> retrieveOldInvoices() {
        // Retrieve all invoices
        List<Invoice> allInvoices = invoiceRepository.findAll();

        // Filter invoices where the status of the associated request is "running"
        return allInvoices.stream()
                .filter(invoice -> {
                    RequestSupply requestSupply = invoice.getRequestSupply();
                    return requestSupply != null && requestSupply.getStatus() == RequestSupplyStatus.Archived;
                })
                .collect(Collectors.toList());
    }
}
