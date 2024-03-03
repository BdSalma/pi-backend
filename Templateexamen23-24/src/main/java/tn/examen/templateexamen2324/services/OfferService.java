package tn.examen.templateexamen2324.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.examen.templateexamen2324.dao.SocietyRepo;
import tn.examen.templateexamen2324.dao.UserRepo;
import tn.examen.templateexamen2324.entity.*;
import tn.examen.templateexamen2324.dao.OfferRepo;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OfferService implements IOfferService {
    @Autowired
    OfferRepo offerRepo;
    @Autowired
    SocietyRepo societyRepo;
    @Autowired
    UserRepo userRepo;

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
        for (Offer o : s.getOffers()) {
            offers.add(o);
        }
        return offers;
    }

    @Override
    public List<Offer> getOfferByCategory(Category categoryOffer) {
        return offerRepo.findOffersByOffreCategory(categoryOffer);
    }

    @Override
    public User getSociety(String id) {
        return userRepo.findById(id).orElse(null);
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
        offer.setEtatOffer(EtatOffer.Approuvé);
        offerRepo.save(offer);
    }

    @Override
    public void changeEtatToRefuse(Long idOffer) {
        Offer offer= offerRepo.findById(idOffer).orElse(null);
        offer.setEtatOffer(EtatOffer.réfusée);
        offerRepo.save(offer);
    }
    @Override
    public  List<Offer> getAcceptedOffer(){
        List<Offer> offers = offerRepo.findAll();
        List<Offer> off = new ArrayList<>();
        for (Offer f: offers){
            if (f.getEtatOffer().equals(EtatOffer.Approuvé)){
                off.add(f);
            }
        }
        return off;
    }
}




