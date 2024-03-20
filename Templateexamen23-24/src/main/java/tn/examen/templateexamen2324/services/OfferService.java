package tn.examen.templateexamen2324.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import tn.examen.templateexamen2324.dao.UserRepo;
import tn.examen.templateexamen2324.entity.*;
import tn.examen.templateexamen2324.dao.OfferRepo;
import tn.examen.templateexamen2324.repository.SocietyRepository;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class OfferService implements IOfferService {
    @Autowired
    OfferRepo offerRepo;
    @Autowired
    SocietyRepository societyRepo;
    @Autowired
    UserRepo userRepo;
    @Autowired
    private JavaMailSender mailSender;
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
        if (s instanceof Society) {
            o.setSociety(s);
            o.setEtatOffer(EtatOffer.Enattente);
            offerRepo.save(o);
        } else {
            System.out.println("societe n'est pas un user");
        }
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
        // Perform filtering based on the provided input
        // You can define your filter logic here
        // For example, filtering based on offer name, category, or other attributes

        // Filtering by offer name containing the specified input (case-insensitive)
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
    public  List<Offer> getAcceptedOffer(){
        return offerRepo.findAcceptedOffersOrderByFavorisDesc(EtatOffer.Approuvé);
    }

    @Override
    public double calculateAverageOffersPerDay() {
        List<Offer> offers = offerRepo.findAll();
        Set<LocalDate> uniqueDates = new HashSet<>();

        for (Offer offer : offers) {
            // Convert java.sql.Date to java.util.Date
            java.util.Date utilDate = new java.util.Date(offer.getDateEmission().getTime());
            // Convert java.util.Date to LocalDate
            LocalDate localDate = utilDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            // Add the date to the uniqueDates set
            uniqueDates.add(localDate);
        }
        int totalDays = uniqueDates.size();
        int totalOffers = offers.size();

        // Calculate the average percentage by dividing the total days by the total offers and multiplying by 100
        double averagePercentage = totalOffers / (double) totalDays;
        System.out.println(averagePercentage);
        return averagePercentage;
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
    @Override
    public Offer addFavorite(Long id) {
        Offer offer = offerRepo.findById(id).orElse(null);
            Integer favoris = offer.getFavoris();
            if (favoris == null) {
                favoris = 0; // Initialisation à zéro si favoris est null
            }
            offer.setFavoris(favoris + 1);
            return offerRepo.save(offer);
    }

}



