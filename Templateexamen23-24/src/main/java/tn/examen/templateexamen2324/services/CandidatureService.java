package tn.examen.templateexamen2324.services;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tn.examen.templateexamen2324.dao.CandidatureRepo;
import tn.examen.templateexamen2324.dao.InterviewRepo;
import tn.examen.templateexamen2324.dao.OfferRepo;
import tn.examen.templateexamen2324.dao.UserRepo;
import tn.examen.templateexamen2324.entity.*;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class CandidatureService implements ICandidatureService{
    @Autowired
    CandidatureRepo crepo;
    @Autowired
    OfferRepo offerRepo;
    @Autowired
    InterviewRepo interviewRepo;
    @Autowired
    UserRepo urepo;


    @Override
    public Candidature addCandidat(Candidature c, Long id, Long idUser, MultipartFile cv, MultipartFile lettre) throws IOException {

        // Existing code to retrieve offer and user
        Offer offer = offerRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Offer not found"));
        User user = urepo.findById(idUser).orElseThrow(() -> new IllegalArgumentException("User not found"));

        // File upload logic
        if (cv != null) { // Ensure a file was actually uploaded
            String fileName = UUID.randomUUID().toString() + "." + getFileExtension(cv);
            String uploadPath = "C:/Users/Salma/IdeaProjects/pi/pi-backend/Templateexamen23-24/src/main/resources/fils"; // Replace with your designated upload path
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdir(); // Create the directory if it doesn't exist
            }

            File uploadFile = new File(uploadPath + "/" + fileName);
            cv.transferTo(uploadFile); // Save the file to the specified path

            c.setCv(fileName); // Store the file path in the Candidature object
        }
        // File upload logic
        if (lettre != null) { // Ensure a file was actually uploaded
            String filelettre = UUID.randomUUID().toString() + "." + getFileExtension(lettre);
            String uploadPath = "C:/Users/Salma/IdeaProjects/pi/pi-backend/Templateexamen23-24/src/main/resources/fils"; // Replace with your designated upload path
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdir(); // Create the directory if it doesn't exist
            }

            File uploadFile = new File(uploadPath + "/" + filelettre);
            lettre.transferTo(uploadFile); // Save the file to the specified path

            c.setLettre(filelettre); // Store the file path in the Candidature object
        }

        // Remaining code to set candidature properties
        c.setStatus(Status.In_progress);
        c.setDate(new Date());
        c.setOffer(offer);
        c.setIndividu(user);

        return crepo.save(c);
    }

    // Helper method to get file extension
    private String getFileExtension(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        return fileName.substring(fileName.lastIndexOf(".") + 1);
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
        // Update other fields
        candidature.setStatus(updatedCandidature.getStatus());
        // Save and return the updated Candidature
        return crepo.save(candidature);
    }
    @Override
    public Candidature AccepterCandidature(Long id, Candidature updatedCandidature) {
        // Retrieve existing Candidature
        Candidature candidature = crepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid candidature id: " + id));
        // Update other fields
        candidature.setStatus(Status.Accepted);
        // Save and return the updated Candidature
        return crepo.save(candidature);
    }

    @Override
    public Candidature FindCandidatById(Long id) {
        return crepo.findById(id).get();
    }

    @Override
    public List<Candidature> FindCandidatByIdOffer(Long id) {
        return crepo.findCandidaturesByOffer_IdOffre(id);
    }

    @Override
    public List<Candidature> FindCandidatByIdUser(String idUser) {
        return crepo.findCandidaturesByIndividu_Id(idUser);
    }

}
