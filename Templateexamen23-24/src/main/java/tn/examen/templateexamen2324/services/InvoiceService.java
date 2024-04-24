package tn.examen.templateexamen2324.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tn.examen.templateexamen2324.entity.*;
import tn.examen.templateexamen2324.repository.DevisRepository;
import tn.examen.templateexamen2324.repository.InvoiceRepository;
import tn.examen.templateexamen2324.repository.RequestSupplyRepository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
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
    public List<Invoice> retrieveAllInvoices(String userId) {
        // Retrieve all invoices
        List<Invoice> allInvoices = invoiceRepository.findAll();

        // Filter invoices where the status of the associated request is "running"
        return allInvoices.stream()
                .filter(invoice -> {
                    RequestSupply requestSupply = invoice.getRequestSupply();
                    return requestSupply != null && requestSupply.getStatus() == RequestSupplyStatus.Running&&
                            requestSupply.getIndividu().getId().equals(userId);
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
            String uploadPath = "C:/Users/ASUS/Desktop/PIbackend/Last clone/pi-backend/Templateexamen23-24/src/main/resources/fils"; // Replace with your designated upload path
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
        Path filePath = Paths.get("C:/Users/ASUS/Desktop/PIbackend/Last clone/pi-backend/Templateexamen23-24/src/main/resources/fils", fileName);
        return Files.readAllBytes(filePath);
    }
    @Override
    public List<Invoice> retrieveOldInvoicesByUserId(String userId) {
        // Retrieve all invoices
        List<Invoice> allInvoices = invoiceRepository.findAll();

        // Filter invoices by the user's ID and where the status of the associated request is "Archived"
        return allInvoices.stream()
                .filter(invoice -> {
                    RequestSupply requestSupply = invoice.getRequestSupply();
                    return requestSupply != null &&
                            requestSupply.getStatus() == RequestSupplyStatus.Archived &&
                            requestSupply.getIndividu().getId().equals(userId);
                })
                .collect(Collectors.toList());
    }

    // Calculate the total amount of money for accepted devis by each individu
    @Override
    public Map<String, Float> calculateTotalAmountByIndividu(String individuId) {
        // Retrieve all devis with running requests
        List<Devis> allDevis = devisRepository.findByRequestSupplyStatusAndAndRequestSupply_Individu_Id(RequestSupplyStatus.Running, individuId);
        // Initialize a map to store the total amount by individu
        Map<String, Float> totalAmountByIndividu = new HashMap<>();

        // Iterate over all devis
        for (Devis devis : allDevis) {
            // Check if the devis is accepted
            if (devis.getStatus() == DevisStatus.Accepted) {
                // Get the current total amount for the individu, or 0 if it doesn't exist yet
                float currentTotalAmount = totalAmountByIndividu.getOrDefault(individuId, 0f);
                // Add the price of the current devis to the total amount for the individu
                currentTotalAmount += devis.getPrice();
                // Update the total amount for the individu in the map
                totalAmountByIndividu.put(individuId, currentTotalAmount);

            }
        }

        return totalAmountByIndividu;
    }



}
