package tn.examen.templateexamen2324.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tn.examen.templateexamen2324.entity.*;

import java.util.List;

@Repository
public interface PackRepo extends JpaRepository<Pack, Long> {
    List<Pack> findPackByStatut(Boolean statut);
    //List<Pack> findPackByForum(Forum Forum);

    @Query("SELECT c FROM Pack c WHERE c.Forum.id=:forumId")
    List<Pack> findPackByForum(@Param("forumId") Long forumId);

    @Query("SELECT p FROM Pack p WHERE p.reserver=:societyId ")
    Pack findPackSociety(@Param("societyId") Society societyId);
    List<Pack> findPackByTypePackAndReservationStatus(TypePack typePack, ReservationStatus reservationStatus);
    //int countPackByReserverAndForum(User reserver , Forum currentForum);
}