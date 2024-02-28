package tn.examen.templateexamen2324.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.examen.templateexamen2324.dao.SocietyRepo;
import tn.examen.templateexamen2324.dao.UserRepo;
import tn.examen.templateexamen2324.entity.Category;
import tn.examen.templateexamen2324.entity.Offer;
import tn.examen.templateexamen2324.dao.OfferRepo;
import tn.examen.templateexamen2324.entity.Society;
import tn.examen.templateexamen2324.entity.User;

import java.util.ArrayList;
import java.util.List;

@Service
public class OfferService implements IOfferService{
    @Autowired
    OfferRepo offerRepo;
    @Autowired
    SocietyRepo societyRepo;
    @Autowired
    UserRepo userRepo;
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
    public void affecetOfferToSociety(Offer o, String idU) {
        Society s = societyRepo.findById(idU).orElse(null);
        if (s instanceof Society) {
            o.setSociety(s);
            offerRepo.save(o);
        }
        else {
            System.out.println("societe n'est pas un user");
        }
    }

    @Override
    public List<Offer> getOfferBySociety(String idS) {
        Society s = societyRepo.findById(idS).orElse(null);
        List<Offer> offers = new ArrayList<>();
        for (Offer o : s.getOffers()){
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
        return userRepo.findById(id).orElse(null);    }

    @Override
    public List<Offer> filterOffersByParams(String category, String society, String offerName) {
        if (category != null && society != null && offerName != null) {
            Category categoryEnum = Category.valueOf(category);
            return offerRepo.findOffersByOffreCategoryAndOfferNameAndSocietyUsername(categoryEnum, offerName, society);
        } else if (category != null && society != null) {
            Category categoryEnum = Category.valueOf(category);
            return offerRepo.findOffersByOffreCategoryAndSocietyUsername(categoryEnum, society);
        } else if (category != null && offerName != null) {
            Category categoryEnum = Category.valueOf(category);
            return offerRepo.findOffersByOffreCategoryAndOfferName(categoryEnum, offerName);
        } else if (society != null && offerName != null) {
            return offerRepo.findOffersByOfferNameAndSocietyUsername(offerName, society);
        } else if (category != null) {
            Category categoryEnum = Category.valueOf(category);
            return offerRepo.findOffersByOffreCategory(categoryEnum);
        } else if (society != null) {
            return offerRepo.findOffersBySocietyUsername(society);
        } else if (offerName != null) {
            return offerRepo.findOffersByOfferNameContaining(offerName);
        } else {
            return offerRepo.findAll();
        }
    }



}
