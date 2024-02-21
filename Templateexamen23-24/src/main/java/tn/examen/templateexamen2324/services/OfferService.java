package tn.examen.templateexamen2324.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.examen.templateexamen2324.dao.SocietyRepo;
import tn.examen.templateexamen2324.entity.Offer;
import tn.examen.templateexamen2324.dao.OfferRepo;
import tn.examen.templateexamen2324.entity.Society;

import java.util.ArrayList;
import java.util.List;

@Service
public class OfferService implements IOfferService{
    @Autowired
    OfferRepo offerRepo;
    @Autowired
    SocietyRepo societyRepo;
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
    public void affecetOfferToSociety(Offer o, Long idS) {
        Society s = societyRepo.findById(idS).orElse(null);
        o.setSociety(s);
        offerRepo.save(o);
    }

    @Override
    public List<Offer> getOfferBySociety(Long idS) {
        Society s = societyRepo.findById(idS).orElse(null);
        List<Offer> offers = new ArrayList<>();
        for (Offer o : s.getOffer()){
            offers.add(o);
        }
        return offers;
    }
}
