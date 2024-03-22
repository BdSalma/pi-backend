package tn.examen.templateexamen2324.services;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.examen.templateexamen2324.entity.Favorite;
import tn.examen.templateexamen2324.entity.Reclamation;
import tn.examen.templateexamen2324.entity.TypeReclamation;
import tn.examen.templateexamen2324.entity.User;
import tn.examen.templateexamen2324.repository.FavoriteRepository;
import tn.examen.templateexamen2324.repository.ReclamationRepository;
import tn.examen.templateexamen2324.repository.UserRepository;
import java.util.List;
import java.util.Optional;

@Service
public class ReclamationService implements IReclamationService {

    @Autowired
    ReclamationRepository reclamationRepository;
    @Autowired
    UserRepository userRepository;

    @Autowired
    FavoriteRepository favoriteRepository;


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
        User user = userRepository.findById(id).get();
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("walahamdi0@gmail.com");
        message.setTo(user.email);
        message.setText("Your reclamation has been reviewed ! We are working further to maintain the best user experience for you. Thanks for your feed-back");
        message.setSubject("Reviewed");
        mailSender.send(message);
    }

    @Override
    public List<Reclamation> getFeed(TypeReclamation typeReclamation) {
        TypeReclamation reclamationType = TypeReclamation.Feed;
        return reclamationRepository.getFeed(reclamationType);
    }

    /*@Override
    public void addToFavorites(int reclamationId, String userId) {
        // Retrieve the reclamation from the database
        Reclamation reclamation = reclamationRepository.findById( reclamationId)
                .orElseThrow(() -> new EntityNotFoundException("Reclamation not found with id: " + reclamationId));

        // Check if the reclamation is already in the user's favorites
        if (reclamation.getFavorites().stream().anyMatch(favorite -> favorite.getUserId() == userId)) {
            throw new IllegalStateException("Reclamation is already in the user's favorites");
        }

        // Create a new favorite and add it to the reclamation
        Favorite favorite = new Favorite();
        favorite.setUserId((userId));
        favorite.setReclamation(reclamation);

        reclamation.getFavorites().add(favorite);

        // Save the updated reclamation back to the database
        reclamationRepository.save(reclamation);
    }*/
    @Transactional
    @Override
    public void addToFavorites(int reclamationId, String userId) {
        // Retrieve the reclamation from the database
        Reclamation reclamation = reclamationRepository.findById(reclamationId)
                .orElseThrow(() -> new EntityNotFoundException("Reclamation not found with id: " + reclamationId));

        // Find the favorite associated with the reclamation and user
        Favorite favorite = favoriteRepository.findByReclamationUser(reclamation, userId);

        if (favorite != null) {
            // If the favorite exists, remove it
            reclamation.getFavorites().remove(favorite);

            // Save the updated reclamation back to the database
            reclamationRepository.save(reclamation);
        } else {
            // Create a new favorite and add it to the reclamation
            favorite = new Favorite();
            favorite.setUserId(userId);
            favorite.setReclamation(reclamation);

            reclamation.getFavorites().add(favorite);

            // Save the updated reclamation with the new favorite back to the database
            reclamationRepository.save(reclamation);
        }
    }


    @Override
    public List<Reclamation> getFavoriteReclamationsForConnectedUser(String userId) {
        // Retrieve favorite reclamations for the connected user directly using a custom query
        List<Reclamation> favoriteReclamations = reclamationRepository.findFavoritesByUserId(userId);
        return favoriteReclamations;
    }

    /*@Override
    public void removeFavorite(int reclamationId, String userId) {
        Reclamation reclamation = reclamationRepository.findById(reclamationId)
                .orElseThrow(() -> new EntityNotFoundException("Reclamation not found with id: " + reclamationId));


        // Try to find the favorite by its ID
        Favorite favorite = favoriteRepository.findByReclamationUser(reclamation,userId);

        // Delete the favorite
        favoriteRepository.delete(favorite);

    }*/


    @Autowired
    private JavaMailSender mailSender;





}



