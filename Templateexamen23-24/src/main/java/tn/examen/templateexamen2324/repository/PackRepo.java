package tn.examen.templateexamen2324.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.examen.templateexamen2324.entity.Pack;
import tn.examen.templateexamen2324.entity.Stand;

@Repository
public interface PackRepo extends JpaRepository<Pack, Long> {

}
