package tn.examen.templateexamen2324.services;
import org.springframework.web.multipart.MultipartFile;
import tn.examen.templateexamen2324.entity.Candidature;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
public interface ICandidatureService {
    Candidature addCandidat(Candidature c, Long id,String idUser, MultipartFile cvFile, MultipartFile lettre) throws IOException;
    List<Candidature> findAllCadidature();
    void deleteById(Long id);
    Candidature updateCandidature(Long id, Candidature updatedCandidature);
    Candidature AccepterCandidature(Long id, Candidature updatedCandidature) throws Exception;
    Candidature FindCandidatById(Long id);
    List<Candidature> FindCandidatByIdOffer(Long id);
    List<Candidature> FindCandidatByIdUser(String idUser);
}
