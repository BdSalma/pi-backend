package tn.examen.templateexamen2324.services;

import tn.examen.templateexamen2324.entity.RequestSupply;

import java.util.List;
import java.util.Map;


public interface RequestSupplyIService {
    RequestSupply addRequestSupply(RequestSupply requestSupply,String idU);
    List<RequestSupply> retrieveAllRequestSupplies();
    RequestSupply retrieveRequestSupplyById(int idRequestSupply);
    void deleteRequestSupply(int idRequestSupply);
    RequestSupply updateRequestSupply(int idRequestSupply);
    List<RequestSupply> getRequestSupplyByIndividus(String idU);
    List<RequestSupply> recommendNewRequestsForSociety(String societyId);
    List<RequestSupply> getOLdRequestSupplyByIndividus(String idU);
    List<RequestSupply> getRequestSupplyByForumTheme(String theme);
    Map<String, Integer> getIndividuStatistics(String idU);
    Map<String, Integer> getSocietyStatistics(String societyId);
}
