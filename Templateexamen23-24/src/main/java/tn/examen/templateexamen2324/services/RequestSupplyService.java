package tn.examen.templateexamen2324.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.examen.templateexamen2324.dao.IndividuRepo;
import tn.examen.templateexamen2324.dao.SocietyRepo;
import tn.examen.templateexamen2324.dao.UserRepo;
import tn.examen.templateexamen2324.entity.*;
import tn.examen.templateexamen2324.repository.DevisRepository;
import tn.examen.templateexamen2324.repository.RequestSupplyRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class RequestSupplyService implements RequestSupplyIService{
    @Autowired
    RequestSupplyRepository requestSupplyRepository;
    @Autowired
    SocietyRepo societyRepo;
    @Autowired
    UserRepo userRepo;
    @Autowired
    IndividuRepo individuRepo;
    @Autowired
    DevisRepository devisRepository;
    @Autowired
    ForumService fs;
    @Override
    public RequestSupply addRequestSupply(RequestSupply requestSupply,String idU) {
        if (LocalDate.now().plusDays(4).isAfter(requestSupply.getDate())) {
            throw new IllegalArgumentException("Request date must be at least 4 days in the future.");
        }
        Individu i = individuRepo.findById(idU).orElse(null);
        if (i instanceof Individu) {
                    requestSupply.setIndividu(i);
        }else {
            System.out.println("individus n'est pas un user");
        }
        requestSupply.setForum(fs.getCurrentForum());
        return requestSupplyRepository.save(requestSupply);
    }

    @Override
    public List<RequestSupply> retrieveAllRequestSupplies() {
        List<RequestSupply> allSupplies = requestSupplyRepository.findAll();
        return allSupplies.stream()
                .filter(r -> r.getStatus() == RequestSupplyStatus.Running)
                .collect(Collectors.toList());
    }

    @Override
    public RequestSupply retrieveRequestSupplyById(int idRequestSupply) {
        return requestSupplyRepository.findById(idRequestSupply).orElse(null);
    }

    @Override
    public void deleteRequestSupply(int idRequestSupply) {

        this.requestSupplyRepository.deleteById(idRequestSupply);
    }

    @Override
    public RequestSupply updateRequestSupply(int idRequestSupply) {
        RequestSupply r = this.requestSupplyRepository.findById(idRequestSupply).orElse(null);
        return this.requestSupplyRepository.save(r);
    }
    @Override
    public List<RequestSupply> getRequestSupplyByIndividus(String idU) {
        Individu i = individuRepo.findById(idU).orElse(null);
        if (i == null) {
            return Collections.emptyList(); // Return empty list if Individu not found
        }

        return i.getRequestSupplies().stream()
                .filter(r -> r.getStatus() == RequestSupplyStatus.Running)
                .collect(Collectors.toList());
    }
    @Override
    public List<RequestSupply> getOLdRequestSupplyByIndividus(String idU) {
        Individu i = individuRepo.findById(idU).orElse(null);
        if (i == null) {
            return Collections.emptyList();
        }

        return i.getRequestSupplies().stream()
                .filter(r -> r.getStatus() == RequestSupplyStatus.Archived)
                .collect(Collectors.toList());
    }
    @Override
    public List<RequestSupply> recommendNewRequestsForSociety(String societyId) {
        // Step 1: Retrieve all the accepted devis for the specified society
        List<Devis> acceptedDevis = devisRepository.findBySocietyDevisId(societyId);

        // Step 2: Retrieve all the requests of the individus that have old devis accepted for the specified society
        List<RequestSupply> newRequestsFromIndividus = acceptedDevis.stream()
                .map(Devis::getRequestSupply)
                .filter(Objects::nonNull)
                .map(RequestSupply::getIndividu)
                .filter(Objects::nonNull)
                .flatMap(individu -> requestSupplyRepository.findAllByIndividuIdAndDevisIsNull(individu.getId()).stream())
                .distinct()
                .collect(Collectors.toList());

        return newRequestsFromIndividus.stream()
                .filter(r -> r.getStatus() == RequestSupplyStatus.Running)
                .collect(Collectors.toList());
    }





}
