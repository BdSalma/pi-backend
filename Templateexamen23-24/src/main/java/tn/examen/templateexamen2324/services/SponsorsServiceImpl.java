package tn.examen.templateexamen2324.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.examen.templateexamen2324.entity.Sponsors;
import tn.examen.templateexamen2324.entity.User;
import tn.examen.templateexamen2324.repository.SponsorsRepository;
import tn.examen.templateexamen2324.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class SponsorsServiceImpl implements SponsorsService {

    @Autowired
    private SponsorsRepository sponsorsRepository;
    @Autowired
    UserRepository userRepository;

    @Override
    public Sponsors saveSponsor(Sponsors sponsor,String userid)
    {
        User user = userRepository.findById(userid).orElse(null);
        sponsor.setUser(user);
        return sponsorsRepository.save(sponsor);
    }

    @Override
    public List<Sponsors> getAllSponsors() {
        return sponsorsRepository.findAll();
    }

    @Override
    public Sponsors getSponsorById(Long id) {
        Optional<Sponsors> sponsor = sponsorsRepository.findById(id);
        return sponsor.orElse(null);
    }

    @Override
    public Sponsors updateSponsor(Long id, Sponsors sponsorDetails) {
        Sponsors sponsor = getSponsorById(id);
        if(sponsor != null) {
            sponsor.setName(sponsorDetails.getName());
            sponsor.setContactName(sponsorDetails.getContactName());
            // Set other fields similarly
            return sponsorsRepository.save(sponsor);
        }
        return null;
    }

    @Override
    public void deleteSponsor(Long id) {
        sponsorsRepository.deleteById(id);
    }
}