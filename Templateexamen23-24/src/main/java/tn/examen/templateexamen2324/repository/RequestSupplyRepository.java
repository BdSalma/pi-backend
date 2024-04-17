package tn.examen.templateexamen2324.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tn.examen.templateexamen2324.entity.Forum;
import tn.examen.templateexamen2324.entity.InvoiceStatus;
import tn.examen.templateexamen2324.entity.RequestSupply;
import tn.examen.templateexamen2324.entity.RequestSupplyStatus;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface RequestSupplyRepository extends JpaRepository<RequestSupply, Integer> {
    @Query("SELECT rs FROM RequestSupply rs WHERE rs.individu.id = :individuId AND  rs.devis IS EMPTY")
    List<RequestSupply> findAllByIndividuIdAndDevisIsNull(String individuId);

    List<RequestSupply> findByDateBeforeAndStatus(LocalDate date, RequestSupplyStatus status);

    @Query("SELECT rs FROM RequestSupply rs LEFT JOIN FETCH rs.devis d WHERE d IS NULL AND rs.date <= :expirationDate")
    List<RequestSupply> findRequestsWithoutDevisAndCloseToExpiration(LocalDate expirationDate);

    List<RequestSupply> findByForum(Forum f);
}

