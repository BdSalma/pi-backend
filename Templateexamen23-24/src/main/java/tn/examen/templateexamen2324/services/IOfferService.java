package tn.examen.templateexamen2324.services;

import tn.examen.templateexamen2324.entity.Category;
import tn.examen.templateexamen2324.entity.Offer;
import tn.examen.templateexamen2324.entity.Society;
import tn.examen.templateexamen2324.entity.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface IOfferService {
     Offer addOffer(Offer o);
     List<LocalDateTime> getDates(Offer o);
     Offer getOfferById(Long id);

     List<Offer> getOffers();

     Offer updateOffer(Long id);

     void deleteOffer(Long id);

     void affecetOfferToSociety(Offer o, String idS);

     List<Offer> getOfferBySociety(String idS);

     List<Offer> getOfferByCategory(Category categoryOffer,String idS);

     User getSociety(String id);

     public List<Offer> filterOffersByInput(String input);

     void changeEtatToApprouvé(Long idOffer);

     void changeEtatToRefuse(Long idOffer);

     List<Offer> getAcceptedOffer();

     double calculateAverageOffersPerDay() ;
     int numberOffersEnAttente();
     List<Offer> getOfferEnAttente();
}