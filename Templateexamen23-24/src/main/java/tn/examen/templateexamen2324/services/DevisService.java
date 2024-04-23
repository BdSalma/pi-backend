package tn.examen.templateexamen2324.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tn.examen.templateexamen2324.entity.*;
import tn.examen.templateexamen2324.repository.DevisRepository;
import tn.examen.templateexamen2324.repository.RequestSupplyRepository;
import tn.examen.templateexamen2324.repository.SocietyRepository;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class DevisService implements DevisIService {
    @Autowired
    DevisRepository devisRepository;
    @Autowired
    RequestSupplyRepository requestSupplyRepository;
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    SocietyRepository societyRepo;
    @Override
    public Devis addDevis(Devis devis) {

        return devisRepository.save(devis);
    }

    @Override
    public List<Devis> retrieveAllDevis() {
        return devisRepository.findAll();
    }

    @Override
    public Devis retrieveDevisById(int idDevis) {
        Devis devis = devisRepository.findById(idDevis).orElse(null);

        if (devis != null) {
            RequestSupply requestSupply = devis.getRequestSupply();
            if (requestSupply != null) {
                // Load additional details of RequestSupply if needed
                requestSupply.getIdRequestSupply();
            }
        }
        return devis;
    }

    @Override
    public void deleteDevis(int idDevis) {
        this.devisRepository.deleteById(idDevis);

    }

    @Override
    public Devis updateDevis(int idDevis) {

        Devis d = this.devisRepository.findById(idDevis).orElse(null);

        return this.devisRepository.save(d);
    }
@Override
    public Devis createDevisAndAssignToRequest(Devis devis,int requestSupplyId,String idS, MultipartFile file)throws IOException {
        RequestSupply requestSupply = requestSupplyRepository.findById(requestSupplyId).orElse(null);
        devis.setRequestSupply(requestSupply);

    if (file != null) {
        String fileName = UUID.randomUUID().toString() + "." + getFileExtension(file);
        String uploadPath = "C:/Users/ASUS/Desktop/PIbackend/Last clone/pi-backend/Templateexamen23-24/src/main/resources/fils";// Replace with your designated upload path
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdir();
        }

        File uploadFile = new File(uploadPath + "/" + fileName);
        file.transferTo(uploadFile); // Save the file to the specified path
        devis.setFile(fileName);// Store the file path in the Candidature object
    }
    Society s = societyRepo.findById(idS).orElse(null);
    if (s instanceof Society) {
        devis.setSocietyDevis(s);
    }else {
        System.out.println("society n'est pas un user");
    }
        return devisRepository.save(devis);
    }
    private String getFileExtension(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }
    public List<Devis> getDevisByRequestSupply(int requestSupplyId) {
        return devisRepository.findByRequestSupplyIdRequestSupply(requestSupplyId);
    }
    @Override
    public List<Devis> getDevisBySociety(String idS) {
        // Retrieve Devis entities associated with the given societyId
        List<Devis> devisList = devisRepository.findBySocietyDevisId(idS);

        // Initialize a list to store Devis with running request
        List<Devis> filteredDevis = new ArrayList<>();

        // Iterate over each Devis and add it to the list if the associated request has status "running"
        for (Devis devis : devisList) {
            RequestSupply requestSupply = devis.getRequestSupply();
            if (requestSupply != null && requestSupply.getStatus() == RequestSupplyStatus.Running) {
                filteredDevis.add(devis);
            }
        }

        return filteredDevis;

    }
    @Override
    public List<Devis> getOldDevisBySociety(String idS) {
        // Retrieve Devis entities associated with the given societyId
        List<Devis> devisList = devisRepository.findBySocietyDevisId(idS);

        // Initialize a list to store Devis with running request
        List<Devis> filteredDevis = new ArrayList<>();

        // Iterate over each Devis and add it to the list if the associated request has status "running"
        for (Devis devis : devisList) {
            RequestSupply requestSupply = devis.getRequestSupply();
            if (requestSupply != null && requestSupply.getStatus() == RequestSupplyStatus.Archived) {
                filteredDevis.add(devis);
            }
        }

        return filteredDevis;
    }
    @Override
    public Devis updateDevisStatus(int idDevis, DevisStatus status) {
        Devis updatedDevis = this.devisRepository.findById(idDevis).orElse(null);
        if (updatedDevis != null) {

            updatedDevis.setStatus(status.Accepted);
            devisRepository.save(updatedDevis);

            if (updatedDevis.getStatus() == DevisStatus.Accepted) {
                List<Devis> otherDevisList = devisRepository.findByRequestSupplyIdRequestSupply(updatedDevis.getRequestSupply().getIdRequestSupply());
                for (Devis otherDevis : otherDevisList) {
                    // Avoid updating the same Devis object again
                    if (otherDevis.getId() != updatedDevis.getId()) {
                        otherDevis.setStatus(DevisStatus.refused);
                        devisRepository.save(otherDevis);
                    }
                }

                String emailContent = prepareEmailContent(updatedDevis);

                sendEmail(updatedDevis.getSocietyDevis().getEmail(), emailContent);
            }
        }

        return updatedDevis;
    }

    private String prepareEmailContent(Devis devis) {
        StringBuilder emailContentBuilder = new StringBuilder();
        emailContentBuilder.append("Dear ").append(devis.getSocietyDevis().getUsername()).append(",\n\n");
        emailContentBuilder.append("We are writing to inform you about the status of your Devis for the request: ")
                .append(devis.getRequestSupply().getDescription()).append("\n\n");
        emailContentBuilder.append("Your Devis (ID: ").append(devis.getId()).append(") has been accepted.\n\n");
        return emailContentBuilder.toString();
    }

    private void sendEmail(String recipientEmail, String emailContent) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("walahamdi0@gmail.com");
            message.setTo(recipientEmail);
            message.setSubject("Devis Status Update");
            message.setText(emailContent);

            mailSender.send(message);
            System.out.println("Email sent successfully!");
        } catch (Exception e) {
            System.err.println("Failed to send email: " + e.getMessage());
        }
    }

        public byte[] getFileBytes(String fileName) throws IOException {
            // Read the file from disk
            Path filePath = Paths.get("C:/Users/ASUS/Desktop/PIbackend/Last clone/pi-backend/Templateexamen23-24/src/main/resources/fils", fileName);
            return Files.readAllBytes(filePath);
        }



}
