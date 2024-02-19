package tn.examen.templateexamen2324.services;
import tn.examen.templateexamen2324.entity.Candidature;

import java.util.List;
import java.util.Optional;
public interface ICandidatureService {
    Candidature addCandidat(Candidature c);
    List<Candidature> findAllCadidature();
    void deleteById(Long id);
    Candidature updateCandidature(Long id, Candidature candidature);
    Candidature FindCandidatById(Long id);
}
