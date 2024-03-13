package tn.examen.templateexamen2324.services;

import tn.examen.templateexamen2324.entity.Devis;

import java.util.List;

public interface DevisIService {
    Devis addDevis(Devis devis);
    List<Devis> retrieveAllDevis();
    Devis retrieveDevisById(int idDevis);
    void deleteDevis(int idDevis);
    Devis updateDevis(int idDevis);
    Devis createDevisAndAssignToRequest(Devis devis,int requestSupplyId,String idS);
    List<Devis> getDevisByRequestSupply(int requestSupplyId);
    List<Devis> getDevisBySociety(String idS);
    Devis updateDevisStatus(int idDevis, boolean newStatus);
}
