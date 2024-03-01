package tn.examen.templateexamen2324.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tn.examen.templateexamen2324.entity.Candidature;

import java.util.List;

@Repository
public interface CandidatureRepo extends CrudRepository<Candidature, Long> {
    List<Candidature> findCandidaturesByOffer_IdOffre(Long id);
    List<Candidature> findCandidaturesByIndividu_Id(String individu_id);


}