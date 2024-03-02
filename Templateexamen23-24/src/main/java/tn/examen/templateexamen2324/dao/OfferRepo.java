package tn.examen.templateexamen2324.dao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.examen.templateexamen2324.entity.Category;
import tn.examen.templateexamen2324.entity.Offer;

import java.util.List;

@Repository
public interface OfferRepo extends JpaRepository<Offer, Long> {
    List<Offer> findOffersByOffreCategory(Category OffreCategory);

    List<Offer> findByCandidatProfilContainingIgnoreCase(String candidatProfil);

    List<Offer> findByDureeContainingIgnoreCase(String duree);

    List<Offer> findByDescriptionContainingIgnoreCase(String description);
    List<Offer> findByOfferNameContainingIgnoreCase(String offerName);



    
}
