package tn.examen.templateexamen2324.dao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.examen.templateexamen2324.entity.Category;
import tn.examen.templateexamen2324.entity.Offer;

import java.util.List;

@Repository
public interface OfferRepo extends JpaRepository<Offer, Long> {
    List<Offer> findOffersByOffreCategory(Category OffreCategory);
    List<Offer> findOffersBySocietyUsername(String username);
    List<Offer> findOffersByOfferNameContaining(String string);
    List<Offer> findOffersByOffreCategoryAndSocietyUsername(Category OffreCategory,String username);
    List<Offer> findOffersByOffreCategoryAndOfferName(Category OffreCategory,String offer);
    List<Offer> findOffersByOfferNameAndSocietyUsername(String offer,String username);
    List<Offer> findOffersByOffreCategoryAndOfferNameAndSocietyUsername(Category OffreCategory,String offer,String username);

}
