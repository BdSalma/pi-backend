package tn.examen.templateexamen2324.services;

import org.springframework.web.multipart.MultipartFile;
import tn.examen.templateexamen2324.entity.Devis;
import tn.examen.templateexamen2324.entity.DevisStatus;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface DevisIService {
    Devis addDevis(Devis devis);
    List<Devis> retrieveAllDevis();
    Devis retrieveDevisById(int idDevis);
    void deleteDevis(int idDevis);
    Devis updateDevis(int idDevis);
    Devis createDevisAndAssignToRequest(Devis devis, int requestSupplyId, String idS, MultipartFile file)throws IOException;
    List<Devis> getDevisByRequestSupply(int requestSupplyId);
    List<Devis> getDevisBySociety(String idS);
    Devis updateDevisStatus(int idDevis, DevisStatus status);
    public byte[] getFileBytes(String fileName) throws IOException ;
    List<Devis> getOldDevisBySociety(String idS);
    // Calculate the total amount of money for accepted devis by each individu


    }
