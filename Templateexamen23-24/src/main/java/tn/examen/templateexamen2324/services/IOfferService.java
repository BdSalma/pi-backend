package tn.examen.templateexamen2324.services;

import tn.examen.templateexamen2324.entity.Offer;

import java.util.List;

public interface IOfferService
{
     Offer addOffer(Offer o);
     Offer getOfferById(Long id);
     List<Offer> getOffers();
     Offer updateOffer(Long id);
     void deleteOffer(Long id);

}
