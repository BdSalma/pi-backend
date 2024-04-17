package tn.examen.templateexamen2324.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.examen.templateexamen2324.entity.Candidature;
import tn.examen.templateexamen2324.entity.Stand;

import java.util.List;

@Repository
public interface StandRepo extends JpaRepository<Stand, Long> {
    List<Stand> findStandByReserved(Boolean statut);
}