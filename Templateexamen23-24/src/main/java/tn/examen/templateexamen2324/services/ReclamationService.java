package tn.examen.templateexamen2324.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import tn.examen.templateexamen2324.entity.Reclamation;
import tn.examen.templateexamen2324.entity.TypeReclamation;
import tn.examen.templateexamen2324.repository.ReclamationRepository;
import java.util.List;

@Service
public class ReclamationService implements IReclamationService {

    @Autowired
    ReclamationRepository reclamationRepository;

    @Override
    public Reclamation publishReclamation(Reclamation r) {
        return reclamationRepository.save(r);
    }

    @Override
    public Reclamation getReclamationsById(int id) {
        return reclamationRepository.findById(id).get();
    }

    @Override
    public List<Reclamation> getReclamationsByUser(int id) {
        return reclamationRepository.findReclamationUser(id);
    }

    @Override
    public List<Reclamation> getAllReclamation() {
        return reclamationRepository.findAll();
    }

    @Override
    public List<Reclamation> getReclamationType(TypeReclamation typeReclamation) {
        return reclamationRepository.findReclamationType(typeReclamation);
    }

    @Override
    public void DeleteReclamation(int id) {
        reclamationRepository.deleteById(id);
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

}



