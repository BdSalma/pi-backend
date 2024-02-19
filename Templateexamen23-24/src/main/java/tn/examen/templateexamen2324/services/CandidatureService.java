package tn.examen.templateexamen2324.services;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.examen.templateexamen2324.dao.CandidatureRepo;
import tn.examen.templateexamen2324.entity.Candidature;

import java.util.Date;
import java.util.List;

@Service
public class CandidatureService implements ICandidatureService{
    @Autowired
    CandidatureRepo crepo;
    @Override
    public Candidature addCandidat(Candidature c) {
        c.setDate(new Date());
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
    public Candidature updateCandidature(Long id, Candidature candidature) {
        return null;
    }

    @Override
    public Candidature FindCandidatById(Long id) {
        return crepo.findById(id).get();
    }
}
