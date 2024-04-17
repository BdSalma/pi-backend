package tn.examen.templateexamen2324.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.examen.templateexamen2324.entity.Offer;
import tn.examen.templateexamen2324.entity.OfferFavoris;
import tn.examen.templateexamen2324.entity.User;

import java.util.List;

@Repository
public interface OfferFavorisRepo  extends JpaRepository<OfferFavoris, Long> {

    OfferFavoris findAllByUserIdAndOffer_IdOffre(String userId,Long offerId);
    List<OfferFavoris> findAllByUserId(String userId);
}
