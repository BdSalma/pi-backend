package tn.examen.templateexamen2324.dao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tn.examen.templateexamen2324.entity.Category;
import tn.examen.templateexamen2324.entity.EtatOffer;
import tn.examen.templateexamen2324.entity.Offer;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
@Repository
public interface OfferRepo extends JpaRepository<Offer, Long> {
    List<Offer> findOffersByOffreCategory(Category OffreCategory);
    @Query("SELECT o FROM Offer o WHERE o.etatOffer = :etatOffer ORDER BY o.favoris DESC")
    List<Offer> findAcceptedOffersOrderByFavorisDesc(@Param("etatOffer") EtatOffer etatOffer);


}