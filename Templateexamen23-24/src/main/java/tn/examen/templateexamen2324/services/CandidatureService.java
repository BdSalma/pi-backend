package tn.examen.templateexamen2324.services;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.examen.templateexamen2324.dao.CandidatureRepo;
import tn.examen.templateexamen2324.dao.InterviewRepo;
import tn.examen.templateexamen2324.dao.OfferRepo;
import tn.examen.templateexamen2324.entity.Candidature;
import tn.examen.templateexamen2324.entity.Interview;
import tn.examen.templateexamen2324.entity.Offer;

import java.util.Date;
import java.util.List;

@Service
public class CandidatureService implements ICandidatureService{
    @Autowired
    CandidatureRepo crepo;
    @Autowired
    OfferRepo offerRepo;
    @Autowired
    InterviewRepo interviewRepo;
    @Override
    public Candidature addCandidat(Candidature c, Long id) {
        Offer offer = offerRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Offer not found"));
        c.setDate(new Date());
        c.setOffer(offer);
        return crepo.save(c);
    }

    @Override
    public List<Candidature> findAllCadidature() {
        return (List<Candidature>) crepo.findAll();
    }

    @Override
    public void deleteById(Long id) {
        crepo.deleteById(id);
    }

    @Override
    public Candidature updateCandidature(Long id, Candidature updatedCandidature) {
        // Retrieve existing Candidature
        Candidature candidature = crepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid candidature id: " + id));

        // Check if Interview ID is provided in the updatedCandidature
        if (updatedCandidature.getInterview() != null && updatedCandidature.getInterview().getIdInterview() != null) {
            // Retrieve existing Interview by its ID
            Interview existingInterview = interviewRepo.findById(updatedCandidature.getInterview().getIdInterview())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid interview id: " + updatedCandidature.getInterview().getIdInterview()));

            // Update Candidature with the existing Interview
            candidature.setInterview(existingInterview);
        }

        // Update other fields
        candidature.setStatus(updatedCandidature.getStatus());

        // Save and return the updated Candidature
        return crepo.save(candidature);
    }

    @Override
    public Candidature FindCandidatById(Long id) {
        return crepo.findById(id).get();
    }
}
