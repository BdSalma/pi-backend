package tn.examen.templateexamen2324.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.examen.templateexamen2324.entity.Individu;

@Repository
public interface IndividuRepo extends JpaRepository<Individu, String> {
}
