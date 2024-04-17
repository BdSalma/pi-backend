package tn.examen.templateexamen2324.services;

import tn.examen.templateexamen2324.entity.Rating;
import tn.examen.templateexamen2324.entity.Reclamation;
import tn.examen.templateexamen2324.entity.TypeReclamation;

import java.util.List;
import java.util.Map;

public interface IReclamationService {
    Reclamation publishReclamation(Reclamation r,String userId);
    Reclamation getReclamationsById(int id);
    List<Reclamation> getReclamationsByUser(int id);
    List<Reclamation> getAllReclamation();
    List<Reclamation> getReclamationType(TypeReclamation typeReclamation);
    void DeleteReclamation(int id);

    public void Review(String id);
    List<Reclamation> getFeed(TypeReclamation typeReclamation);
    public void addToFavorites(int reclamationId, String userId);

    public List<Reclamation> getFavoriteReclamationsForConnectedUser(String userId);
    public Reclamation rateReclamation(int reclamationId, Rating rating,String userId);
    public Integer getRatingForReclamation(int reclamationId);

    public void Contact(String id, String context);
    public Map<String, Long> listOfReclamationByType();

    //public void removeFavorite(int reclamationId, String userId);


}
