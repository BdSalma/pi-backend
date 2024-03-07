package tn.examen.templateexamen2324.services;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import tn.examen.templateexamen2324.entity.Reclamation;
import tn.examen.templateexamen2324.entity.TypeReclamation;
import tn.examen.templateexamen2324.entity.User;
import tn.examen.templateexamen2324.repository.ReclamationRepository;
import tn.examen.templateexamen2324.repository.UserRepository;
import java.util.List;

@Service
public class ReclamationService implements IReclamationService {

    @Autowired
    ReclamationRepository reclamationRepository;
    @Autowired
    UserRepository userRepository;

    @Override
    public Reclamation publishReclamation(Reclamation r,String userId) {
        r.setTypeReclamation(TypeReclamation.Feed);
        User user = userRepository.findById(userId).get();
        r.setUser(user);
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

    @Override
    public void Review(String id) {

    }


/*
    @Override
    public void Review(String id) {
        User user = userRepository.findById(id).get();
        /*Reclamation reclamation = reclamationRepository.findById(Rid).get();
        reclamation.setReview(true);
        reclamationRepository.save(reclamation);*/
    /*
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("walahamdi0@gmail.com");
        message.setTo(user.email);
        message.setText("Your reclamation has been reviewed ! We are working further to maintain the best user experience for you. Thanks for your feed-back");
        message.setSubject("Reviewed");
        mailSender.send(message);
    }
*/

    @Override
    public List<Reclamation> getFeed(TypeReclamation typeReclamation) {
        TypeReclamation reclamationType = TypeReclamation.Feed;
        return reclamationRepository.getFeed(reclamationType);
    }
/*
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
*/
}



