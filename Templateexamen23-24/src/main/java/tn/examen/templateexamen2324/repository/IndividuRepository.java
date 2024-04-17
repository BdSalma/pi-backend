package tn.examen.templateexamen2324.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tn.examen.templateexamen2324.entity.Individu;
import tn.examen.templateexamen2324.entity.IndividuRole;

import java.util.List;

@Repository
public interface IndividuRepository extends JpaRepository<Individu,String> {

    @Query("SELECT i FROM Individu i WHERE i.role NOT IN :roles")
    public List<Individu> getAllIndividuExcepte(@Param("roles") List<IndividuRole> roles);

    @Query("SELECT i FROM Individu i WHERE lower(i.firstName) LIKE lower(concat('%', :field, '%')) OR " +
            "lower(i.lastName) LIKE lower(concat('%', :field, '%')) OR " +
            "lower(i.identity) LIKE lower(concat('%', :field, '%'))")
    public List<Individu> findAllByFields(@Param("field") String field);

    public List<Individu> findAllByRole(IndividuRole role);

    @Query("SELECT i.role, COUNT(i) FROM Individu i GROUP BY i.role")
    List<Object[]> getIndividuCountByRole();

}
