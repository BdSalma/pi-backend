package tn.examen.templateexamen2324.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.examen.templateexamen2324.entity.Candidature;
import tn.examen.templateexamen2324.entity.Offer;
import tn.examen.templateexamen2324.entity.Status;

import java.util.List;

@Repository
public interface CandidatureRepo extends JpaRepository<Candidature, Long> {
    List<Candidature> findCandidaturesByOffer_IdOffre(Long id);
    List<Candidature> findCandidaturesByIndividu_Id(String individu_id);
    List<Candidature> findCandidaturesByStatusAndOffer(Status s, Offer o);

    // List<Candidature> getCandidaturesByOfferAndIndividu(Long idOffer, String idIndividu);

}