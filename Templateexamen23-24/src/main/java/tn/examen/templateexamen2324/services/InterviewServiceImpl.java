package tn.examen.templateexamen2324.services;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import tn.examen.templateexamen2324.dao.CandidatureRepo;
import tn.examen.templateexamen2324.dao.InterviewRepo;
import tn.examen.templateexamen2324.dao.OfferRepo;
import tn.examen.templateexamen2324.dao.RoomRepo;
import tn.examen.templateexamen2324.entity.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
@Slf4j
@Service
@Transactional
public class InterviewServiceImpl implements IinterviewService {
    @Autowired
    InterviewRepo irepo;
    @Autowired
    CandidatureRepo crepo;

    @Autowired
    OfferRepo orepo;
    @Autowired
    RoomRepo roomRepo;
    @PersistenceContext
    private EntityManager entityManager;
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    @Override
    public Interview FindInterviewById(Long id) {
        return irepo.findById(id).get();
    }
    @Override
    public List<Interview> getInterviewsByEtatInterviewetOffer(EtatInterview etatInterview, Offer offer) {
        List<Interview> interviewsByOfferAndEtat = new ArrayList<>();

        // Récupérer toutes les candidatures pour l'offre spécifiée
        Set<Candidature> candidatures = offer.getCandidatures();

        // Parcourir les candidatures et récupérer les interviews associées
        for (Candidature candidature : candidatures) {
            Interview interview = candidature.getInterview();

            // Vérifier si l'interview est non nulle avant d'accéder à son état
            if (interview != null && interview.getEtatInterview() == etatInterview) {
                interviewsByOfferAndEtat.add(interview);
            }
        }

        return interviewsByOfferAndEtat;
    }

    @Override
    @Transactional
    public Interview addInterview(Interview i, Long candidatureId) {
        Candidature candidature = crepo.findById(candidatureId)
                .orElseThrow(() -> new IllegalArgumentException("Candidature not found"));
        candidature.setStatus(Status.Finish);
        i.setCandidature(candidature);
        if (candidature.getInterview() == null) {
            candidature.setInterview(i);
        }
        i.setEtatInterview(EtatInterview.Non_Valider);
        return irepo.save(i);
    }
    @Override
    @Transactional
    public Interview addInter(Interview i, Long candidatureId, int roomNum) {
        Candidature candidature = crepo.findById(candidatureId)
                .orElseThrow(() -> new IllegalArgumentException("Candidature not found"));
        i.setCandidature(candidature);
        if (candidature.getInterview() == null) {
            candidature.setInterview(i);
        }

            Room room = roomRepo.findRoomByNum(roomNum);
            i.getRoom().add(room);
            room.setStatus(roomStatus.RESERVED);
            room.setInterview(i);
        i.setEtatInterview(EtatInterview.Non_Valider);
        Interview savedInterview = irepo.save(i);
            scheduleRoomAvailabilityChange(i);

        return savedInterview;
    }// 1 hour in milliseconds
    private void scheduleRoomAvailabilityChange(Interview interview) {
        LocalDateTime interviewDate = interview.getDate();
        LocalDateTime changeTime = interviewDate.plusMinutes(30);

        // Create a copy of the room collection
        List<Room> rooms = new ArrayList<>(interview.getRoom());

        TimerTask task = new TimerTask() {
            public void run() {
                // Change the status of each room in the copied collection
                for (Room room : rooms) {
                    room.setStatus(roomStatus.AVAILABLE);
                    roomRepo.save(room);
                }
            }
        };

        Timer timer = new Timer("RoomAvailabilityTimer");
        timer.schedule(task, Date.from(changeTime.atZone(ZoneId.systemDefault()).toInstant()));
    }

    @Override
    @Transactional
    public Interview addInterEnligne(Interview i, Long candidatureId) {
        Candidature candidature = crepo.findById(candidatureId)
                .orElseThrow(() -> new IllegalArgumentException("Candidature not found"));
        candidature.setStatus(Status.Finish);
       
        i.setCandidature(candidature);
        if (candidature.getInterview() == null) {
            candidature.setInterview(i);
        }

        // Générer le lien Meet et l'ajouter à l'interview
        String meetRoomName = "Interview_Room_" + i.getIdInterview(); // You can adjust the room name as needed
        String meetLink = "https://meet.jit.si/" + meetRoomName;
        i.setLien(meetLink);
        i.setEtatInterview(EtatInterview.Non_Valider);
        return irepo.save(i);
    }
    public List<LocalDateTime> getDates(Long id) {
        Offer offer=orepo.findById(id).orElse(null);
        // Assuming Offer has fields date1, date2, and date3 for the three dates
        return Arrays.asList(offer.getDate1(), offer.getDate2(), offer.getDate3());
    }
    public boolean comparerLesDates(Interview i,Candidature c){
        Offer o=c.getOffer();
        List<LocalDateTime>dates =getDates(o.getIdOffre());

        return false;
    }
    private String generateMeetLink() {
        // Logique de génération du lien Meet
        // Vous pouvez utiliser des bibliothèques ou des algorithmes spécifiques pour générer un lien unique.

        // Exemple simple : concaténer une partie fixe avec un identifiant unique (comme l'ID de l'interview)
        return "https://meet.google.com/" + generateUniqueIdentifier();
    }

    private String generateUniqueIdentifier() {
        // Logique pour générer un identifiant unique (peut-être l'ID de l'interview ou autre)
        // Vous pouvez utiliser UUID.randomUUID() ou toute autre méthode d'identification unique.
        return UUID.randomUUID().toString();
    }
    @Override
    public List<Interview> getInterviews() {
        return (List<Interview>)irepo.findAll();
    }


    @Override
    public void deleteById(Long id) {
        irepo.deleteById(id);
    }
    @Override
    @Transactional
    public Interview updateInterview(Long id, Interview updatedInterv) {
        // Retrieve existing Candidature
        Interview interview = irepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid candidature id: " + id));
        // Update other fields
        Candidature c =interview.getCandidature();
        c.setStatus(Status.Finish);
        interview.setRoom(updatedInterv.getRoom());
        interview.setTitre(updatedInterv.getTitre());
        // Save the interview
        Interview updatedInterview = irepo.save(interview);
        // Save and return the updated Candidature
        return updatedInterview;
    }
    @Override
    @Transactional
    public Interview updateInterviewR(Long id, Interview updatedInterv, int roomNum) {
        // Retrieve existing Interview
        Interview interview = irepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid interview id: " + id));

        // Update other fields of the interview
        interview.setTitre(updatedInterv.getTitre());

        // Retrieve the room by its number
        Room room = roomRepo.findRoomByNum(roomNum);
        if (room == null) {
            throw new IllegalArgumentException("Room not found with number: " + roomNum);
        }

        // Update the room's status and associate it with the interview
        room.setStatus(roomStatus.RESERVED);
        room.setInterview(interview);

        // Add the room to the interview's room list
        interview.getRoom().add(room);
        // Save and return the updated Interview
        Interview updatedInterview = irepo.save(interview);

        // Schedule room availability change
        scheduleRoomAvailabilityChange(interview);

         // Save and return the updated Candidature
        return updatedInterview;
    }

    @Override
    public Interview AccepterCandidature(Long id, Interview updatedCandidature)  {
        // Retrieve existing Candidature
        Interview candidature = irepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid interview id: " + id));

        // Update candidature status and send notification email (separate method)
        candidature.setEtatInterview(EtatInterview.Valider);

        return irepo.save(candidature);
    }
}