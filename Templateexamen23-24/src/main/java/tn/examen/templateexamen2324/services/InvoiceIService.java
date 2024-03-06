package tn.examen.templateexamen2324.services;

import tn.examen.templateexamen2324.entity.Invoice;

import java.util.List;

public interface InvoiceIService {
    Invoice addInvoice(Invoice invoice);

    List<Invoice> retrieveAllInvoices();

    Invoice retrieveInvoiceById(int idInvoice);

    void deleteInvoice(int idInvoice);

    Invoice updateInvoice(int idInvoice);

    void addAndAssignInvoiceToRequest(Invoice invoice, int requestSupplyId);
    List<Invoice> getInvoicesBySocietyId(String societyId);

}
