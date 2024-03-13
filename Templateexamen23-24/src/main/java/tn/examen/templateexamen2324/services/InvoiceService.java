package tn.examen.templateexamen2324.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.examen.templateexamen2324.entity.Devis;
import tn.examen.templateexamen2324.entity.Invoice;
import tn.examen.templateexamen2324.entity.RequestSupply;
import tn.examen.templateexamen2324.repository.DevisRepository;
import tn.examen.templateexamen2324.repository.InvoiceRepository;
import tn.examen.templateexamen2324.repository.RequestSupplyRepository;

import java.util.ArrayList;
import java.util.List;

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

        return invoiceRepository.findAll();
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
    public void addAndAssignInvoiceToRequest(Invoice invoice, int requestSupplyId) {
        RequestSupply requestSupply = requestSupplyRepository.findById(requestSupplyId).orElse(null);
        if (requestSupply == null) {
            throw new IllegalArgumentException("RequestSupply with id " + requestSupplyId + " not found!");
        }

        // Check if the RequestSupply already has an invoice assigned
        if (requestSupply.getInvoice() != null) {
            throw new IllegalStateException("RequestSupply already has an invoice assigned!");
        }

        // Set the invoice properties and save it
        this.invoiceRepository.save(invoice);

        // Assign the invoice to the RequestSupply and save it
        requestSupply.setInvoice(invoice);
        requestSupplyRepository.save(requestSupply);
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
            if (requestSupply != null) {
                Invoice invoice = requestSupply.getInvoice();
                if (invoice != null) {
                    invoices.add(invoice);
                }
            }
        }

        return invoices;
    }


}
