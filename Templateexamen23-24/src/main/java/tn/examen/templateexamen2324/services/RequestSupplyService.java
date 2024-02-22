package tn.examen.templateexamen2324.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.examen.templateexamen2324.entity.RequestSupply;
import tn.examen.templateexamen2324.repository.RequestSupplyRepository;

import java.util.List;

@Service
public class RequestSupplyService implements RequestSupplyIService{
    @Autowired
    RequestSupplyRepository requestSupplyRepository;
    @Override
    public RequestSupply addRequestSupply(RequestSupply requestSupply) {
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
    public RequestSupply updateRequestSupply(int idRequestSupply, RequestSupply requestSupply) {

        RequestSupply r = this.requestSupplyRepository.findById(idRequestSupply).orElse(null);

        return this.requestSupplyRepository.save(r);
        }
}
