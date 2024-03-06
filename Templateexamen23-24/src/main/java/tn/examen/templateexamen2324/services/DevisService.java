package tn.examen.templateexamen2324.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.examen.templateexamen2324.dao.SocietyRepo;
import tn.examen.templateexamen2324.entity.*;
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
    @Autowired
    SocietyRepo societyRepo;
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
        Devis devis = devisRepository.findById(idDevis).orElse(null);

        if (devis != null) {
            RequestSupply requestSupply = devis.getRequestSupply();
            if (requestSupply != null) {
                // Load additional details of RequestSupply if needed
                requestSupply.getIdRequestSupply();
            }
        }
        return devis;
    }

    @Override
    public void deleteDevis(int idDevis) {
        this.devisRepository.deleteById(idDevis);

    }

    @Override
    public Devis updateDevis(int idDevis) {

        Devis d = this.devisRepository.findById(idDevis).orElse(null);

        return this.devisRepository.save(d);
    }
@Override
    public Devis createDevisAndAssignToRequest(Devis devis,int requestSupplyId,String idS) {
        RequestSupply requestSupply = requestSupplyRepository.findById(requestSupplyId).orElse(null);
        devis.setRequestSupply(requestSupply);
    Society s = societyRepo.findById(idS).orElse(null);
    if (s instanceof Society) {
        devis.setSociety(s);
    }else {
        System.out.println("society n'est pas un user");
    }
        return devisRepository.save(devis);
    }

    public List<Devis> getDevisByRequestSupply(int requestSupplyId) {
        return devisRepository.findByRequestSupplyIdRequestSupply(requestSupplyId);
    }
   public List<Devis> getDevisBySociety(String idS){
        return devisRepository.findBySocietyId(idS);
   }
    @Override
    public Devis updateDevisStatus(int idDevis, boolean newStatus) {
        Devis updatedDevis = this.devisRepository.findById(idDevis).orElse(null);
        if (updatedDevis != null) {

            updatedDevis.setStatus(newStatus);
            devisRepository.save(updatedDevis);

            // If the new status is true, update the status of other Devis objects to false
            if (newStatus) {
                List<Devis> otherDevisList = devisRepository.findByRequestSupplyIdRequestSupply(updatedDevis.getRequestSupply().getIdRequestSupply());
                for (Devis otherDevis : otherDevisList) {
                    // Avoid updating the status of the same Devis object again
                    if (otherDevis.getId() != updatedDevis.getId()) {
                        otherDevis.setStatus(false);
                        devisRepository.save(otherDevis);
                    }
                }
            }
        }

        return updatedDevis;
    }


}
