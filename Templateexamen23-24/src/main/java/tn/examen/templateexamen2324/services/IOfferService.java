package tn.examen.templateexamen2324.services;

import tn.examen.templateexamen2324.entity.Category;
import tn.examen.templateexamen2324.entity.Offer;
import tn.examen.templateexamen2324.entity.Society;
import tn.examen.templateexamen2324.entity.User;

import java.util.List;

public interface IOfferService {
    Offer addOffer(Offer o);

    Offer getOfferById(Long id);

    List<Offer> getOffers();

    Offer updateOffer(Long id);

    void deleteOffer(Long id);

    void affecetOfferToSociety(Offer o, String idS);

    List<Offer> getOfferBySociety(String idS);

    List<Offer> getOfferByCategory(Category categoryOffer);

    User getSociety(String id);

    public List<Offer> filterOffersByParams(String category, String society, String offerName);
}