package tn.examen.templateexamen2324.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.examen.templateexamen2324.entity.Sponsors;

@Repository
public interface SponsorsRepository extends JpaRepository<Sponsors, Long> {
    // Vous pouvez ajouter des méthodes de requête personnalisées ici si nécessaire
}