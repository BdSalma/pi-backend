package tn.examen.templateexamen2324.dao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tn.examen.templateexamen2324.entity.*;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.Query;
@Repository
public interface OfferRepo extends JpaRepository<Offer, Long> {
    List<Offer> findOffersByOffreCategory(Category OffreCategory);
    @Query("SELECT o FROM Offer o WHERE o.etatOffer = :etatOffer ORDER BY o.favoris DESC")
    List<Offer> findAcceptedOffersOrderByFavorisDesc(@Param("etatOffer") EtatOffer etatOffer);
    List<Offer> findAllByForumAndEtatOffer(Forum f,EtatOffer etat);
    @Query("SELECT o.offreCategory, COUNT(o) FROM Offer o GROUP BY o.offreCategory")
    List<Object[]> countOffersByCategory();
    @Query("SELECT c.offer, COUNT(c) FROM Candidature c GROUP BY c.offer")
    List<Object[]> countCandidaturesByOffer();

    public int countOfferBySociety(Society idS);
    List<Offer> findOffersBySociety(Society s);
}