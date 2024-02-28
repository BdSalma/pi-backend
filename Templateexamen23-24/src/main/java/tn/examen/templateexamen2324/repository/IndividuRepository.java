package tn.examen.templateexamen2324.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.examen.templateexamen2324.entity.Individu;

@Repository
public interface IndividuRepository extends JpaRepository<Individu,String> {
}
