package tn.examen.templateexamen2324.services;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.store.ByteBuffersDirectory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tn.examen.templateexamen2324.dao.CandidatureRepo;
import tn.examen.templateexamen2324.dao.OfferFavorisRepo;
import tn.examen.templateexamen2324.dao.UserRepo;
import tn.examen.templateexamen2324.entity.*;
import tn.examen.templateexamen2324.dao.OfferRepo;
import tn.examen.templateexamen2324.repository.ForumRepo;
import tn.examen.templateexamen2324.repository.SocietyRepository;
import tn.examen.templateexamen2324.repository.UserRepository;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class OfferService implements IOfferService {
    @Autowired
    OfferRepo offerRepo;
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
    public void affecetOfferToSociety(Offer o, String idU) {
        Society s = societyRepo.findById(idU).orElse(null);
        List<Forum> f = forumRepo.findAll();
        if (s instanceof Society) {
            // Vérifier si le fichier est présent et non vide
            // Définir la société et l'état de l'offre
            for (Forum fo:f) {
                if (fo.getForumStatus().equals(ForumStatus.In_Progress)) {

                    o.setForum(fo);
                    o.setSociety(s);
                    o.setEtatOffer(EtatOffer.Enattente);
                }
            }
            // Enregistrer l'offre dans la base de données
            offerRepo.save(o);
        } else {
            System.out.println("La société n'est pas un utilisateur");
        }
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
        Offer offer= offerRepo.findById(idOffer).orElse(null);
        Society s = offer.getSociety();
        offer.setEtatOffer(EtatOffer.Approuvé);
        offerRepo.save(offer);
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("allouchy.ryhem@gmail.com");
        message.setTo(s.email);
        message.setText("Your offer"+offer.getOfferName()+" has been accepted !");
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

    @Scheduled(cron = "0 30 15 * * ?") // Execute everyday at 21:33 PM
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
    /*@Override
    public Offer addFavorite(Long id) {
        Offer offer = offerRepo.findById(id).orElse(null);
            Integer favoris = offer.getFavoris();
            if (favoris == null) {
                favoris = 0; // Initialisation à zéro si favoris est null
            }
            offer.setFavoris(favoris + 1);
            return offerRepo.save(offer);
    }*/

    /*@Override
    public List<Offer> getSuggestedOffers(User user, int numberOfSuggestions) {
        List<Offer> allOffers = offerRepo.findAll();
        List<Offer> suggestedOffers = new ArrayList<>();

        try {
            // Create an in-memory Lucene index
            ByteBuffersDirectory directory = new ByteBuffersDirectory();
            Analyzer analyzer = new StandardAnalyzer();

            // Index the offers' descriptions
            IndexWriterConfig config = new IndexWriterConfig(analyzer);
            IndexWriter writer = new IndexWriter(directory, config);
            for (Offer offer : allOffers) {
                Document doc = new Document();
                doc.add(new org.apache.lucene.document.TextField("description", offer.getDescription(), org.apache.lucene.document.Field.Store.YES));
                writer.addDocument(doc);
            }
            writer.close();

            // Search for offers similar to user's competence
            IndexReader reader = DirectoryReader.open(directory);
            IndexSearcher searcher = new IndexSearcher(reader);

            Query query = new TermQuery(new Term("description", user.getCompetence()));
            ScoreDoc[] hits = searcher.search(query, numberOfSuggestions).scoreDocs;

            // Retrieve and add the suggested offers
            for (ScoreDoc hit : hits) {
                int docId = hit.doc;
                Document d = searcher.doc(docId);
                String description = d.get("description");
                Offer offer = allOffers.stream().filter(o -> o.getDescription().equals(description)).findFirst().orElse(null);
                if (offer != null) {
                    suggestedOffers.add(offer);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return suggestedOffers;
    }*/
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



