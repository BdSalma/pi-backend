package tn.examen.templateexamen2324.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.examen.templateexamen2324.entity.Invoice;
import tn.examen.templateexamen2324.entity.RequestSupply;
import tn.examen.templateexamen2324.repository.InvoiceRepository;
import tn.examen.templateexamen2324.repository.RequestSupplyRepository;

import java.util.List;

@Service
public class InvoiceService implements InvoiceIService {
    @Autowired
    InvoiceRepository invoiceRepository;
    @Autowired
    RequestSupplyRepository requestSupplyRepository;
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
    public Invoice updateInvoice(int idInvoice, Invoice invoice) {

        Invoice i = this.invoiceRepository.findById(idInvoice).orElse(null);
        return this.invoiceRepository.save(i);
    }

@Override
    public void assignInvoiceToRequest(Invoice invoice, int requestSupplyId) {
        RequestSupply requestSupply = requestSupplyRepository.findById(requestSupplyId).orElse(null);
        if (invoice.getRequestSupply() != null) {
            throw new IllegalStateException("Invoice already assigned to another Request Supply!");
        }

        if (requestSupply.getInvoice() != null) {
            throw new IllegalStateException("Request Supply already has an invoice assigned!");
        }
        this.invoiceRepository.save(invoice);
        requestSupply.setInvoice(invoice);
        requestSupplyRepository.save(requestSupply);
    }


}
