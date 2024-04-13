package tn.examen.templateexamen2324.dao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.examen.templateexamen2324.entity.Category;
import tn.examen.templateexamen2324.entity.EtatOffer;
import tn.examen.templateexamen2324.entity.Offer;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OfferRepo extends JpaRepository<Offer, Long> {
    List<Offer> findOffersByOffreCategory(Category OffreCategory);

}