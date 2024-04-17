package tn.examen.templateexamen2324.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.examen.templateexamen2324.entity.SponsorView;
import tn.examen.templateexamen2324.entity.Sponsors;
import tn.examen.templateexamen2324.repository.SponsorViewRepository;
import tn.examen.templateexamen2324.services.SponsorViewService;
import tn.examen.templateexamen2324.services.SponsorsService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class SponsorViewServiceImpl implements SponsorViewService {

    @Autowired
    private SponsorViewRepository sponsorViewRepository;

    @Autowired
    private SponsorsService sponsorsService;

    public Optional<Sponsors> getMostViewedSponsor() {
        List<Object[]> results = sponsorViewRepository.countViewsBySponsor();
        if (!results.isEmpty()) {
            Object[] mostViewed = results.get(0);
            Long sponsorId = (Long) mostViewed[0];
            return Optional.ofNullable(sponsorsService.getSponsorById(sponsorId));
        }
        return Optional.empty();
    }

    @Override
    public void recordView(Long sponsorId) {
        Sponsors sponsor = sponsorsService.getSponsorById(sponsorId);
        if (sponsor != null) {
            SponsorView view = new SponsorView();
            view.setSponsor(sponsor);
            view.setTimestamp(LocalDateTime.now());
            sponsorViewRepository.save(view);
        }
    }




    @Override
    public List<SponsorView> getAllViews() {
        return sponsorViewRepository.findAll();
    }

    // Implémentez d'autres méthodes si nécessaire
}
