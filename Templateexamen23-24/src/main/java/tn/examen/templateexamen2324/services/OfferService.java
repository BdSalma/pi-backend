package tn.examen.templateexamen2324.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.examen.templateexamen2324.dao.OfferRepo;
import tn.examen.templateexamen2324.entity.Offer;

import java.util.List;

@Service
public class OfferService implements IOfferService{
    @Autowired
    OfferRepo offerRepo;
    @Override
    public Offer addOffer(Offer o) {
        return offerRepo.save(o);
    }

    @Override
    public Offer getOfferById(Long id) {
        return offerRepo.findById(id).orElse(null);
    }

    @Override
    public List<Offer> getOffers() {
        return (List<Offer>) offerRepo.findAll();
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
}