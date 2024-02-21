package tn.examen.templateexamen2324.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.examen.templateexamen2324.entity.Offer;
import tn.examen.templateexamen2324.entity.Society;

@Repository
public interface SocietyRepo extends JpaRepository<Society, Long> {
}
