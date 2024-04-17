package tn.examen.templateexamen2324.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.examen.templateexamen2324.entity.Devis;
import tn.examen.templateexamen2324.entity.RequestSupplyStatus;

import java.util.List;

@Repository
public interface DevisRepository extends JpaRepository<Devis, Integer> {

    List<Devis> findByRequestSupplyIdRequestSupply(int idRequestSupply);
    List<Devis> findBySocietyDevisId(String idSociety);

    List<Devis> findByRequestSupplyStatus(RequestSupplyStatus requestSupplyStatus);

    List<Devis> findByRequestSupplyStatusAndAndRequestSupply_Individu_Id(RequestSupplyStatus requestSupplyStatus, String individuId);
}
