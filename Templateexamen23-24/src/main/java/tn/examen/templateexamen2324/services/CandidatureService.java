package tn.examen.templateexamen2324.services;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tn.examen.templateexamen2324.dao.CandidatureRepo;
import tn.examen.templateexamen2324.dao.InterviewRepo;
import tn.examen.templateexamen2324.dao.OfferRepo;
import tn.examen.templateexamen2324.dao.UserRepo;
import tn.examen.templateexamen2324.entity.*;
import tn.examen.templateexamen2324.repository.SocietyRepository;
import tn.examen.templateexamen2324.repository.UserRepository;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CandidatureService implements ICandidatureService{
    @Autowired
    CandidatureRepo crepo;
    @Autowired
    SocietyRepository societyRepo;
    @Autowired
    OfferRepo offerRepo;
    @Autowired
    InterviewRepo interviewRepo;
    @Autowired
    UserRepository urepo;
   /* private final JavaMailSender mailSender;

    public CandidatureService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }*/

    @Override
    public Candidature addCandidat(Candidature c, Long id, String idUser, MultipartFile cv, MultipartFile lettre) throws IOException {
        Offer offer = offerRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Offer not found"));
        User user = urepo.findById(idUser).orElseThrow(() -> new IllegalArgumentException("User not found"));
        if (cv != null) {
            String fileName = UUID.randomUUID().toString() + "." + getFileExtension(cv);
            String uploadPath = "C:/Users/the cast/Desktop/pi/pi-backend/Templateexamen23-24/src/main/resources/fils"; // Replace with your designated upload path
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }

            File uploadFile = new File(uploadPath + "/" + fileName);
            cv.transferTo(uploadFile); // Save the file to the specified path

            c.setCv(fileName); // Store the file path in the Candidature object
        }
        // File upload logic
        if (lettre != null) { // Ensure a file was actually uploaded
            String filelettre = UUID.randomUUID().toString() + "." + getFileExtension(lettre);
            String uploadPath = "C:/Users/the cast/Desktop/pi/pi-backend/Templateexamen23-24/src/main/resources/fils"; // Replace with your designated upload path
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdir(); // Create the directory if it doesn't exist
            }

            File uploadFile = new File(uploadPath + "/" + filelettre);
            lettre.transferTo(uploadFile); // Save the file to the specified path

            c.setLettre(filelettre); // Store the file path in the Candidature object
        }
        c.setStatus(Status.In_progress);
        c.setDate(new Date());
        c.setOffer(offer);
        c.setIndividu(user);

        return crepo.save(c);
    }
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
    public int getInProgressCandidaturesCountBySocietyId(String societyId) {
        // Récupérer la société à partir de son ID
        Society society = societyRepo.findById(societyId).orElse(null);
        if (society == null) {
            return 0;
        }
        // Récupérer toutes les offres de la société
        List<Offer> offers = offerRepo.findOffersBySociety(society);

        // Initialiser le nombre total de candidatures en cours
        int totalInProgressCandidaturesCount = 0;

        // Parcourir toutes les offres de la société
        for (Offer offer : offers) {
            List<Candidature> inProgressCandidatures = crepo.findCandidaturesByStatusAndOffer(Status.In_progress, offer);
            // Ajouter le nombre de candidatures en cours pour cette offre au total
            totalInProgressCandidaturesCount += inProgressCandidatures.size();
        }
        return totalInProgressCandidaturesCount;
    }


    @Override
    public Candidature RefuserCandidature(Long id, Candidature updatedCandidature) throws Exception {
        // Retrieve existing Candidature
        Candidature candidature = crepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid candidature id: " + id));

        // Update candidature status and send notification email (separate method)
        candidature.setStatus(Status.refused);
        //sendCandidatureAcceptedEmail(candidature);
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("walahamdi0@gmail.com");
        message.setTo(candidature.getIndividu().email);
        message.setText("Your candidature has been refused ! ");
        message.setSubject("Stage");
        mailSender.send(message);
        return crepo.save(candidature);
    }

    @Override
    public Candidature AccepterCandidature(Long id, Candidature updatedCandidature) throws Exception {
        // Retrieve existing Candidature
        Candidature candidature = crepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid candidature id: " + id));

        // Update candidature status and send notification email (separate method)
        candidature.setStatus(Status.Accepted);
        //sendCandidatureAcceptedEmail(candidature);
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("walahamdi0@gmail.com");
        message.setTo(candidature.getIndividu().email);
        message.setText("Your candidature has been accepted ! ");
        message.setSubject("Stage");
        mailSender.send(message);
        return crepo.save(candidature);
    }
    @Autowired
    private JavaMailSender mailSender;

    public void sendSimpleEmail(String toEmail,
                                String subject,
                                String body
    ) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("walahamdi0@gmail.com");
        message.setTo(toEmail);
        message.setText(body);
        message.setSubject(subject);
        mailSender.send(message);
        System.out.println("Mail Send...");
    }
    /*private void sendCandidatureAcceptedEmail(Candidature candidature)  {
        String to = candidature.getIndividu().getEmail();
        String subject = "Candidature Accepted!";
        String body = "Congratulations, your candidature for the offer " +
                candidature.getOffer().getOfferName() + " has been accepted!";

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("beddakhliasalma@gmail.com"); // Replace with your sender email
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);

        this.mailSender.send(message);
    }
*/
    public List<Candidature> getCandidaturesByOfferAndIndividu(Long idOffer, String idIndividu) {
        // Récupérer toutes les candidatures de la base de données
        List<Candidature> allCandidatures = (List<Candidature>) crepo.findAll();

        // Filtrer les candidatures pour celles qui correspondent à l'offre et à l'individu spécifiés
        List<Candidature> filteredCandidatures = allCandidatures.stream()
                .filter(candidature -> candidature.getOffer().getIdOffre().equals(idOffer) && candidature.getIndividu().getId().equals(idIndividu))
                .collect(Collectors.toList());

        return filteredCandidatures;
    }
    @Override
    public boolean getCandidaturesByOfferAndAndIndividu(Long IdOffer,String idIndividu){
       return getCandidaturesByOfferAndIndividu(IdOffer,idIndividu).size() > 0;
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
