package tn.examen.templateexamen2324.services;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.examen.templateexamen2324.entity.*;
import tn.examen.templateexamen2324.repository.FavoriteRepository;
import tn.examen.templateexamen2324.repository.RatingRepository;
import tn.examen.templateexamen2324.repository.ReclamationRepository;
import tn.examen.templateexamen2324.repository.UserRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReclamationService implements IReclamationService {

    @Autowired
    ReclamationRepository reclamationRepository;
    @Autowired
    UserRepository userRepository;

    @Autowired
    FavoriteRepository favoriteRepository;
    @Autowired
    RatingRepository ratingRepository;


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
    @Override
    public void addToFavorites(int reclamationId, String userId) {
        // Retrieve the reclamation from the database
        Reclamation reclamation = reclamationRepository.findById(reclamationId)
                .orElseThrow(() -> new EntityNotFoundException("Reclamation not found with id: " + reclamationId));

        // Find the favorite associated with the reclamation and user
        Favorite favorite = favoriteRepository.findByReclamationUser(reclamation, userId);

        if (favorite != null) {
            // If the favorite exists, remove it
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

    @Override
    public Reclamation rateReclamation(int reclamationId, Rating rating, String userId) {
        Reclamation reclamation = reclamationRepository.findById(reclamationId).orElse(null);
        if (reclamation != null) {
            // Check if the reclamation already has ratings
            Set<Rating> ratings = reclamation.getRatings();
            if (ratings.isEmpty()) {
                // If there are no existing ratings, add the new rating
                rating.setReclamation(reclamation);
                rating.setUserId(userId);
                ratings.add(rating);
            } else {
                // Iterate through existing ratings and find the one by the same user (if exists)
                for (Rating existingRating : ratings) {
                    if (existingRating.getUserId().equals(userId)) {
                        // Update the existing rating
                        existingRating.setRating(rating.getRating());
                        // Save changes to the rating entity
                        ratingRepository.save(existingRating);
                        return reclamationRepository.save(reclamation);
                    }
                }
                // If no existing rating found for the user, add the new rating
                rating.setReclamation(reclamation);
                rating.setUserId(userId);
                ratings.add(rating);
            }
            return reclamationRepository.save(reclamation);
        } else {
            // Handle reclamation not found
            return null;
        }
    }

    @Override
    public Integer getRatingForReclamation(int reclamationId) {
        Rating rating = ratingRepository.findByReclamationId(reclamationId);
        return rating != null ? rating.getRating() : null;
    }

    @Override
    public void Contact(String email,String context) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(email);
        message.setTo("walahamdi0@gmail.com");
        message.setText(context);
        message.setSubject("Contact");
        mailSender.send(message);

    }

    @Override
    public Map<String, Long> listOfReclamationByType() {
        Map<String, Long> ReclamationCount = new HashMap<>();

        convertListToObjectMap(reclamationRepository.getReclamationCountByType())
                .forEach((type, count) -> ReclamationCount.merge(type, count, Long::sum));


        return ReclamationCount;

    }
    private Map<String, Long> convertListToObjectMap(List<Object[]> counts) {
        return counts.stream()
                .collect(Collectors.toMap(
                        count -> ((TypeReclamation) count[0]).toString(),
                        count -> (Long) count[1],
                        Long::sum));
    }


    @Autowired
    private JavaMailSender mailSender;





}



