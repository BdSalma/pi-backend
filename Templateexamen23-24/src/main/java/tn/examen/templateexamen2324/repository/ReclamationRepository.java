package tn.examen.templateexamen2324.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tn.examen.templateexamen2324.entity.Reclamation;
import tn.examen.templateexamen2324.entity.TypeReclamation;

import java.util.List;

@Repository
public interface ReclamationRepository extends JpaRepository<Reclamation,Integer> {
    @Query("SELECT r FROM Reclamation r JOIN r.User u WHERE u.id = :id")
    List<Reclamation> findReclamationUser(@Param("id") int id);

    @Query("SELECT r FROM Reclamation r  WHERE r.typeReclamation = :typeR")
    List<Reclamation> findReclamationType(@Param("typeR") TypeReclamation typeR);

    @Query("SELECT r FROM Reclamation r WHERE r.typeReclamation = :reclamationType")
    List<Reclamation> getFeed(@Param("reclamationType") TypeReclamation reclamationType);

    @Query("SELECT r FROM Reclamation r JOIN r.favorites f WHERE f.userId = :userId")
    List<Reclamation> findFavoritesByUserId(@Param("userId") String userId);

    @Query("SELECT i.typeReclamation, COUNT(i) FROM Reclamation i GROUP BY i.typeReclamation")
    List<Object[]> getReclamationCountByType();
}
