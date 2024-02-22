package tn.examen.templateexamen2324.services;

import tn.examen.templateexamen2324.entity.RequestSupply;

import java.util.List;


public interface RequestSupplyIService {
    RequestSupply addRequestSupply(RequestSupply requestSupply);
    List<RequestSupply> retrieveAllRequestSupplies();
    RequestSupply retrieveRequestSupplyById(int idRequestSupply);
    void deleteRequestSupply(int idRequestSupply);
    RequestSupply updateRequestSupply(int idRequestSupply,RequestSupply requestSupply);

}
