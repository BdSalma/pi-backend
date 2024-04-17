package tn.examen.templateexamen2324.services;

import org.springframework.web.multipart.MultipartFile;
import tn.examen.templateexamen2324.entity.Invoice;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface InvoiceIService {
    Invoice addInvoice(Invoice invoice);

    List<Invoice> retrieveAllInvoices(String userId);

    Invoice retrieveInvoiceById(int idInvoice);

    void deleteInvoice(int idInvoice);

    Invoice updateInvoice(int idInvoice);

    void addAndAssignInvoiceToRequest(Invoice invoice, int requestSupplyId ,MultipartFile file)throws IOException;
    List<Invoice> getInvoicesBySocietyId(String societyId);
    public byte[] getFileBytes(String fileName) throws IOException ;
    List<Invoice> retrieveOldInvoicesByUserId(String userId);
    List<Invoice> getOldInvoicesBySocietyId(String societyId);
    Map<String, Float> calculateTotalAmountByIndividu(String individuId) ;


}
