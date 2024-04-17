package tn.examen.templateexamen2324.services;

import org.springframework.http.ResponseEntity;
import tn.examen.templateexamen2324.entity.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface IOfferService {
     Offer addOffer(Offer o);

     Offer getOfferById(Long id);

     List<Offer> getOffers();

     Offer updateOffer(Long id);

     void deleteOffer(Long id);

     public ResponseEntity<String> affecetOfferToSociety(Offer o, String idU) ;
     List<Offer> getOfferBySociety(String idS);

     List<Offer> getOfferByCategory(Category categoryOffer,String idS);

     User getSociety(String id);

     public List<Offer> filterOffersByInput(String input);

     void changeEtatToApprouvé(Long idOffer);

     void changeEtatToRefuse(Long idOffer);

     List<Offer> getAcceptedOffer();

     int numberOffersEnAttente();
     List<Offer> getOfferEnAttente();
     void sentOffers();
     //public Offer addFavorite(Long id);
     //public List<Offer> getSuggestedOffers(User user, int numberOfSuggestions) ;
     public Offer favoris(String userId, Long offerId) ;
     public List<OfferFavoris> getFavoriteOffersByUserId(String userId) ;

     public void deletefavorite(Long id);
     public Map<Category, Long> getOfferCountsByCategory() ;
     public Map<Offer, Long> countCandidaturesByOffer() ;

     public boolean getCandidatureByOffer(Long idOffer,String idUser);
     }