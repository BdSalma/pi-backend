package tn.examen.templateexamen2324.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.examen.templateexamen2324.entity.Devis;
import tn.examen.templateexamen2324.entity.Invoice;
import tn.examen.templateexamen2324.entity.RequestSupply;
import tn.examen.templateexamen2324.repository.DevisRepository;
import tn.examen.templateexamen2324.repository.InvoiceRepository;
import tn.examen.templateexamen2324.repository.RequestSupplyRepository;

import java.util.List;

@Service
public class DevisService implements DevisIService {
    @Autowired
    DevisRepository devisRepository;
    @Autowired
    RequestSupplyRepository requestSupplyRepository;
    @Override
    public Devis addDevis(Devis devis) {
        return devisRepository.save(devis);
    }

    @Override
    public List<Devis> retrieveAllDevis() {
        return devisRepository.findAll();
    }

    @Override
    public Devis retrieveDevisById(int idDevis) {
        return devisRepository.findById(idDevis).orElse(null);
    }

    @Override
    public void deleteDevis(int idDevis) {
        this.devisRepository.deleteById(idDevis);

    }

    @Override
    public Devis updateDevis(int idDevis, Devis devis) {

        Devis d = this.devisRepository.findById(idDevis).orElse(null);

        return this.devisRepository.save(d);
    }
@Override
    public Devis createDevisAndAssignToRequest(Devis devis,int requestSupplyId) {
        RequestSupply requestSupply = requestSupplyRepository.findById(requestSupplyId).orElse(null);
        devis.setRequestSupply(requestSupply);
        return devisRepository.save(devis);
    }

}
