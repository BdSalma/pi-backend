package tn.examen.templateexamen2324.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import tn.examen.templateexamen2324.entity.SponsorView;
import java.util.List;

public interface SponsorViewRepository extends JpaRepository<SponsorView, Long> {

    // Supposons que chaque vue de sponsor stocke une référence au sponsor concerné.
    // Cette requête récupère tous les sponsors avec le nombre total de vues, triés par ce total décroissant.
    // Elle retourne une liste d'objets tableau où chaque tableau contient l'ID du sponsor et le nombre de vues.
    @Query("SELECT sv.sponsor.idSponsor, COUNT(sv) AS viewCount FROM SponsorView sv GROUP BY sv.sponsor.idSponsor ORDER BY viewCount DESC")
    List<Object[]> countViewsBySponsor();
}
