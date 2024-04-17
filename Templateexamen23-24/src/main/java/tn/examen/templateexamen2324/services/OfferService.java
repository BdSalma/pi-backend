package tn.examen.templateexamen2324.services;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import tn.examen.templateexamen2324.dao.CandidatureRepo;
import tn.examen.templateexamen2324.dao.OfferFavorisRepo;
import tn.examen.templateexamen2324.entity.*;
import tn.examen.templateexamen2324.dao.OfferRepo;
import tn.examen.templateexamen2324.repository.ForumRepo;
import tn.examen.templateexamen2324.repository.PackRepo;
import tn.examen.templateexamen2324.repository.SocietyRepository;
import tn.examen.templateexamen2324.repository.UserRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class OfferService implements IOfferService {
    @Autowired
    OfferRepo offerRepo;
    @Autowired
    PackRepo packRepo;
    @Autowired
    SocietyRepository societyRepo;
    @Autowired
    UserRepository userRepo;
    @Autowired
    ForumRepo forumRepo;
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    OfferFavorisRepo offerFavorisRepo;
    @Autowired
    CandidatureRepo candidatureRepo;
    @Override
    public Offer addOffer(Offer o) {
        o.setEtatOffer(EtatOffer.Enattente);
        return offerRepo.save(o);
    }


    @Override
    public Offer getOfferById(Long id) {
        return offerRepo.findById(id).orElse(null);
    }

    @Override
    public List<Offer> getOffers() {
        return offerRepo.findAll();
    }
    @Override
    public Offer updateOffer(Long id) {
        Offer off = offerRepo.findById(id).orElse(null);
        return offerRepo.save(off);
    }

    @Override
    public void deleteOffer(Long id) {
        offerRepo.deleteById(id);
    }


    @Override
    public ResponseEntity<String> affecetOfferToSociety(Offer o, String idU) {
        Society s = societyRepo.findById(idU).orElse(null);
        Pack p = packRepo.findPackSociety(s);
        int numberOffer = offerRepo.countOfferBySociety(s);
        List<Forum> f = forumRepo.findAll();

        // Check if the user is a Society
        if (s instanceof Society) {
            boolean forumInProgressFound = false;

            // Check if there's a forum in progress
            for (Forum fo : f) {
                if (fo.getForumStatus().equals(ForumStatus.In_Progress)) {
                    forumInProgressFound = true;
                    break;
                }
            }

            // If there's no forum in progress
            if (!forumInProgressFound) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Aucun forum en cours trouvé pour affecter l'offre");
            }

            // If there's a forum in progress but the number of offers exceeds the limit
            if (p.getNumberOfOffers() <= numberOffer) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Saturé: vous ne pouvez plus ajouter d'offre");
            }

            // If a suitable forum is found, assign the offer and save it
            for (Forum fo : f) {
                if (fo.getForumStatus().equals(ForumStatus.In_Progress)) {
                    o.setForum(fo);
                    o.setSociety(s);
                    o.setEtatOffer(EtatOffer.Enattente);
                    // Save the offer here since a suitable forum is found
                    offerRepo.save(o);
                    return ResponseEntity.ok().build();
                }
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("La société n'est pas un utilisateur");
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur inconnue");
    }




    private String convertFileToBase64(byte[] fileBytes) {
        // Encoder les octets du fichier en base64
        return Base64.getEncoder().encodeToString(fileBytes);
    }
    @Override
    public List<Offer> getOfferBySociety(String idS) {
        Society s = societyRepo.findById(idS).orElse(null);
        List<Offer> offers = new ArrayList<>();
        for (Offer o : s.getOffer()) {
            offers.add(o);
        }
        return offers;
    }

    @Override
    public List<Offer> getOfferByCategory(Category categoryOffer,String idS) {
        List<Offer> listOffers = offerRepo.findOffersByOffreCategory(categoryOffer);
        Society s = societyRepo.findById(idS).orElse(null);
        List<Offer> offers = new ArrayList<>();
        for (Offer o : listOffers) {
            if (o.getSociety().getUsername().equals(s.getUsername())){
                offers.add(o);
            }
        }
        return offers;

        }

    @Override
    public User getSociety(String id) {

        return societyRepo.findById(id).orElse(null);
    }

    @Override
    public List<Offer> filterOffersByInput(String input) {

        List<Offer> filteredOffers = offerRepo.findAll().stream()
                .filter(offer ->
                        (offer.getOfferName().toLowerCase().contains(input.toLowerCase()) ||
                                offer.getCandidatProfil().toLowerCase().contains(input.toLowerCase()) ||
                                offer.getDuree().toLowerCase().contains(input.toLowerCase()) ||
                                offer.getDescription().toLowerCase().contains(input.toLowerCase()) ||
                                offer.getOffreCategory().toString().contains(input.toLowerCase()) ||
                                offer.getSociety().getUsername().toLowerCase().contains(input.toLowerCase())) &&
                                offer.getEtatOffer().equals(EtatOffer.Approuvé))
                .collect(Collectors.toList());
        for (Offer offer : filteredOffers) {
            // Récupérez les données binaires de l'image
            byte[] imageBytes = offer.getFile().getBytes();
            // Encodez les données binaires en base64
            String imageDataBase64 = Base64.getEncoder().encodeToString(imageBytes);
            // Mettez à jour l'offre avec les données encodées en base64
            offer.setFile(imageDataBase64);
        }

        // You can add more filtering logic here for other attributes

        return filteredOffers; // Return the filtered list
    }

    @Override
    public void changeEtatToApprouvé(Long idOffer) {
        Offer offer = offerRepo.findById(idOffer).orElse(null);
        List<User> users = userRepo.findAll();
        Society s = offer.getSociety();
        offer.setEtatOffer(EtatOffer.Approuvé);
        offerRepo.save(offer);
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("allouchy.ryhem@gmail.com");
        message.setTo(s.email);
        message.setText("Your offer" + offer.getOfferName() + " has been accepted !");
        message.setSubject("Offer Accepted");
        mailSender.send(message);

    }
    @Override
    public void changeEtatToRefuse(Long idOffer) {
        Offer offer= offerRepo.findById(idOffer).orElse(null);
        Society s = offer.getSociety();
        offer.setEtatOffer(EtatOffer.réfusée);
        offerRepo.save(offer);
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("walahamdi0@gmail.com");
        message.setTo(s.email);
        message.setText("Your offer"+offer.getOfferName()+" has been refused !");
        message.setSubject("Offer Refused");
        mailSender.send(message);
    }
    @Override
    public List<Offer> getAcceptedOffer() {
        List<Forum> forumsInProgress = new ArrayList<>();

        // Récupérer tous les forums en cours (In_Progress)
        List<Forum> allForums = forumRepo.findAll();
        for (Forum forum : allForums) {
            if (forum.getForumStatus() == ForumStatus.In_Progress) {
                forumsInProgress.add(forum);
            }
        }

        // Récupérer les offres liées aux forums en cours avec l'état Approuvé
        List<Offer> offers = new ArrayList<>();
        for (Forum forum : forumsInProgress) {
            List<Offer> forumOffers = offerRepo.findAllByForumAndEtatOffer(forum, EtatOffer.Approuvé);
            offers.addAll(forumOffers);
        }

        // Transformer les données binaires de l'image en base64
        for (Offer offer : offers) {
            if (offer.getFile() != null) {
                byte[] imageBytes = offer.getFile().getBytes();
                String imageDataBase64 = Base64.getEncoder().encodeToString(imageBytes);
                offer.setFile(imageDataBase64);
            }
        }

        return offers;
    }

    @Override
    public int numberOffersEnAttente(){
        List<Offer> offers = offerRepo.findAll();
        List<Offer> offreEnattente = new ArrayList<>();
        for (Offer f : offers){
            if (f.getEtatOffer().equals(EtatOffer.Enattente)){
                offreEnattente.add(f);
            }
        }
        return offreEnattente.size();
    }
    @Override
    public List<Offer> getOfferEnAttente(){
        List<Offer> offers = offerRepo.findAll();
        List<Offer> offreEnattente = new ArrayList<>();
        for (Offer f : offers){
            if (f.getEtatOffer().equals(EtatOffer.Enattente)){
                offreEnattente.add(f);
            }
        }
        return offreEnattente;
    }

    @Scheduled(cron = "0 30 14 * * ?") // Execute everyday at 21:33 PM
    public void sentOffers() {
        List<Offer> offers = offerRepo.findAll();
        List<Offer> off = new ArrayList<>();
        for (Offer o : offers) {
            if (o.getEtatOffer().equals(EtatOffer.Enattente)) {
                off.add(o);
            }
        }

        if (!off.isEmpty()) { // Vérifie s'il y a des offres en attente
            StringBuilder messageText = new StringBuilder();
            messageText.append("Les offres en attente sont :").append("\n");
            for (Offer offer : off) {
                messageText.append("nom de l'offre: ").append(offer.getOfferName()).append(", ")
                        .append("Société: ").append(offer.getSociety().getUsername()).append(", ")
                        .append("Description: ").append(offer.getDescription()).append("\n");
            }

            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("walahamdi0@gmail.com");
            message.setTo("allouchy.ryhem@gmail.com");
            message.setText(messageText.toString());
            message.setSubject("Offres en attente");
            mailSender.send(message);
        }
    }
    @Override
    public Offer favoris(String userId, Long offerId) {
        Offer offer = offerRepo.findById(offerId).orElse(null);
        User user = userRepo.findById(userId).orElse(null);
        if (offer != null) {
            OfferFavoris favoriteOffer = offerFavorisRepo.findAllByUserIdAndOffer_IdOffre(userId, offerId);
            if (favoriteOffer == null) {
                favoriteOffer = new OfferFavoris();
                favoriteOffer.setUser(user);
                favoriteOffer.setOffer(offer);
                offerFavorisRepo.save(favoriteOffer);
            }
            return offer;
        }
        return null;
    }
    @Override
    public List<OfferFavoris> getFavoriteOffersByUserId(String userId){
        List<OfferFavoris> favoris = offerFavorisRepo.findAllByUserId(userId);
        return favoris;
    }

    @Override
    public void deletefavorite(Long id) {
        offerFavorisRepo.deleteById(id);
    }
    @Override
    public Map<Category, Long> getOfferCountsByCategory() {
        Map<Category, Long> categoryCounts = new HashMap<>();
        List<Object[]> results = offerRepo.countOffersByCategory();

        for (Object[] result : results) {
            Category category = (Category) result[0];
            Long count = (Long) result[1];
            categoryCounts.put(category, count);
        }

        return categoryCounts;
    }
    @Override
    public Map<Offer, Long> countCandidaturesByOffer() {
        List<Object[]> results = offerRepo.countCandidaturesByOffer();
        Map<Offer, Long> candidaturesByOffer = new HashMap<>();

        for (Object[] result : results) {
            Offer offer = (Offer) result[0];
            Long count = (Long) result[1];
            candidaturesByOffer.put(offer, count);
        }

        return candidaturesByOffer;
    }

    @Override
    public boolean getCandidatureByOffer(Long idOffer, String idUser) {
        List<Candidature> candidatures = candidatureRepo.findCandidaturesByOffer_IdOffre(idOffer);
        System.out.println(candidatures);
        User user = userRepo.findById(idUser).orElse(null);
        System.out.println("User ID: " + idUser);
        System.out.println("User found: " + user); // Check if user is retrieved correctly
        for (Candidature c : candidatures) {
            System.out.println("Candidature User ID: " + c.getIndividu().getId());
            if (c.getIndividu().getId().equals(idUser)) {
                return true;
            }
        }
        return false;
    }
}



