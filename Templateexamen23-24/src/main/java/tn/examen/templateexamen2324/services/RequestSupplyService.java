package tn.examen.templateexamen2324.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.examen.templateexamen2324.dao.IndividuRepo;
import tn.examen.templateexamen2324.dao.SocietyRepo;
import tn.examen.templateexamen2324.dao.UserRepo;
import tn.examen.templateexamen2324.entity.Individu;
import tn.examen.templateexamen2324.entity.RequestSupply;
import tn.examen.templateexamen2324.entity.User;
import tn.examen.templateexamen2324.repository.RequestSupplyRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
        return requestSupplyRepository.save(requestSupply);
    }

    @Override
    public List<RequestSupply> retrieveAllRequestSupplies() {
        return requestSupplyRepository.findAll();
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
        List<RequestSupply> supplies = new ArrayList<>();
        for (RequestSupply r : i.getRequestSupplies()) {
            supplies.add(r);
        }
        return supplies;
    }

}
