package tn.examen.templateexamen2324.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tn.examen.templateexamen2324.entity.Individu;
import tn.examen.templateexamen2324.entity.IndividuRole;
import tn.examen.templateexamen2324.entity.Society;
import tn.examen.templateexamen2324.entity.SocietyRole;

import java.util.List;

@Repository
public interface SocietyRepository extends JpaRepository<Society,String> {

    @Query("SELECT s FROM Society s WHERE CAST(s.matricule AS string) LIKE lower(concat('%', :field, '%')) OR " +
            "lower(s.representative) LIKE lower(concat('%', :field, '%')) OR " +
            "lower(s.sector) LIKE lower(concat('%', :field, '%'))")
    public List<Society> findAllByFields(@Param("field") String field);

    public List<Society> findAllByRole(SocietyRole role);

    @Query("SELECT s.role, COUNT(s) FROM Society s GROUP BY s.role")
    List<Object[]> getSocietyCountByRole();
}
