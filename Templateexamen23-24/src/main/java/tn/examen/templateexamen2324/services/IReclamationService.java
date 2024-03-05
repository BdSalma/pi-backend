package tn.examen.templateexamen2324.services;

import tn.examen.templateexamen2324.entity.Reclamation;
import tn.examen.templateexamen2324.entity.TypeReclamation;

import java.util.List;

public interface IReclamationService {
    Reclamation publishReclamation(Reclamation r);
    Reclamation getReclamationsById(int id);
    List<Reclamation> getReclamationsByUser(int id);
    List<Reclamation> getAllReclamation();
    List<Reclamation> getReclamationType(TypeReclamation typeReclamation);
    void DeleteReclamation(int id);

    public void Review(String id);


}
