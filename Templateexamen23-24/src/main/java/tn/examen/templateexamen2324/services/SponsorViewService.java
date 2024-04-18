package tn.examen.templateexamen2324.services;

import tn.examen.templateexamen2324.entity.SponsorView;
import tn.examen.templateexamen2324.entity.Sponsors;

import java.util.List;
import java.util.Optional;

public interface SponsorViewService {
    void recordView(Long sponsorId);
    List<SponsorView> getAllViews();
    Optional<Sponsors> getMostViewedSponsor();

    // Vous pouvez ajouter d'autres méthodes ici selon vos besoins, par exemple pour récupérer le nombre de vues par sponsor, etc.
}
