package tn.examen.templateexamen2324.services;

import tn.examen.templateexamen2324.entity.Sponsors;

import java.util.List;

public interface SponsorsService {
    Sponsors saveSponsor(Sponsors sponsor,String userid);
    List<Sponsors> getAllSponsors();
    Sponsors getSponsorById(Long id);
    Sponsors updateSponsor(Long id, Sponsors sponsor);
    void deleteSponsor(Long id);
}
