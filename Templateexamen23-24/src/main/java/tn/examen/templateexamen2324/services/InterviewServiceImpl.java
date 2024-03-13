package tn.examen.templateexamen2324.services;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import tn.examen.templateexamen2324.dao.CandidatureRepo;
import tn.examen.templateexamen2324.dao.InterviewRepo;
import tn.examen.templateexamen2324.dao.RoomRepo;
import tn.examen.templateexamen2324.entity.Candidature;
import tn.examen.templateexamen2324.entity.Interview;
import tn.examen.templateexamen2324.entity.Room;
import tn.examen.templateexamen2324.entity.roomStatus;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
@Slf4j
@Service
public class InterviewServiceImpl implements IinterviewService {
    @Autowired
    InterviewRepo irepo;
    @Autowired
    CandidatureRepo crepo;
    @Autowired
    RoomRepo roomRepo;
    @PersistenceContext
    private EntityManager entityManager;
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    @Override
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
            scheduleRoomAvailabilityChange(i);
        return irepo.save(i);
    }// 1 hour in milliseconds
    private void scheduleRoomAvailabilityChange(Interview interview) {
        LocalDateTime interviewDate = interview.getDate();
        LocalDateTime changeTime = interviewDate.plusMinutes(15);

        TimerTask task = new TimerTask() {
            public void run() {
                // Changer le statut de la salle en "available"
                for (Room room : interview.getRoom()) {
                    room.setStatus(roomStatus.AVAILABLE);
                    roomRepo.save(room);
                }
            }
        };

        Timer timer = new Timer("RoomAvailabilityTimer");
        timer.schedule(task, Date.from(changeTime.atZone(ZoneId.systemDefault()).toInstant()));
    }

    @Override
    public Interview addInterEnligne(Interview i, Long candidatureId) {
        Candidature candidature = crepo.findById(candidatureId)
                .orElseThrow(() -> new IllegalArgumentException("Candidature not found"));
        i.setCandidature(candidature);
        if (candidature.getInterview() == null) {
            candidature.setInterview(i);
        }

        // Générer le lien Meet et l'ajouter à l'interview
        String meetLink = generateMeetLink();
        i.setLien(meetLink);

        return irepo.save(i);
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

}