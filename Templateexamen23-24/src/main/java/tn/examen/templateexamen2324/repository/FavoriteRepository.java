package tn.examen.templateexamen2324.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tn.examen.templateexamen2324.entity.Favorite;
import tn.examen.templateexamen2324.entity.Individu;
import tn.examen.templateexamen2324.entity.Reclamation;

import java.util.List;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite,Integer> {

    @Query("SELECT f FROM Favorite f  WHERE f.reclamation = :reclamation and f.userId = :userId")
    Favorite findByReclamationUser(@Param("reclamation") Reclamation reclamation,@Param("userId") String userId);
}
