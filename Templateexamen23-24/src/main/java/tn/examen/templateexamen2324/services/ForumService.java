package tn.examen.templateexamen2324.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import tn.examen.templateexamen2324.entity.*;
import tn.examen.templateexamen2324.repository.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

@Service
@Slf4j
public class ForumService implements IForumService{

    @Autowired
    ForumRepo forumRepo;

    @Autowired
    RequestSupplyRepository requestSuplyRepo;

    @Autowired
    DevisRepository devisRepository;
    @Autowired
    PackRepo packRepo;

    @Autowired
    StandRepo standRepo;

    @Autowired
    private JavaMailSender mailSender;

    private static final String uploadPath = "C:/Users/ASUS/Desktop/PIbackend/Last clone/pi-backend/Templateexamen23-24/src/main/resources/fils";

    @Override
    public Forum addForum(Forum forum, MultipartFile  image) {

        //this.addImageToUser(image);
       /* Forum f =this.forumRepo.findForumByForumStatus(ForumStatus.In_Progress);
        if( f ==null){

        }else {
            log.info("Un forum en progress est deja crée");
            return f;
        }*/
        return this.addImageToUser(forum,image);
    }

    @Override
    public List<Forum> retrieveAllForums() {
        return forumRepo.findAll();
    }

    @Override
    public Forum retrieveForumById(long id) {
        return forumRepo.findById(id).get();
    }

    @Override
    public void deleteForum(long id) {
        forumRepo.deleteById(id);
    }

    @Override
    public Forum updateForum(long id, Forum forum) {
        Forum f = forumRepo.findById(id).get();
        f.setId(id);
        f.setForumStatus(forum.getForumStatus());
        f.setDate(forum.getDate());
        f.setDescription(forum.getDescription());
        f.setLocalisation(forum.getLocalisation());
        f.setTheme(forum.getTheme());
        return forumRepo.save(f);
    }



    @Override
    public Forum getCurrentForum() {
        return forumRepo.findForumByForumStatus(ForumStatus.In_Progress);
    }



    @Override
    public Forum getForumByStatus(ForumStatus forumStatus) {
        return null;
    }

    @Override
    public Forum getForumById(long forumId) {
        return this.forumRepo.findById(forumId).get();
    }


    @Override
    public Forum cancelForum(long forumId) {
        Forum f = forumRepo.findById(forumId).orElse(null);
        if (f == null) {
            // Handle case where forum is not found
            return null;
        }

        String mailBody = "Le forum a été annulé";
        f.setForumStatus(ForumStatus.Canceled);

        // Cancel packs and update stands
        List<Pack> packList = new ArrayList<>(f.getPack());
        for (Pack p : packList) {
            if(p.getReservationStatus() == ReservationStatus.Reserved || p.getReservationStatus()==ReservationStatus.On_Hold){
                this.sendSimpleEmail(p.getReserver().getEmail(), "Annulation du forum", mailBody);
                this.standRepo.save(p.getStand());
                this.packRepo.save(p);}
        }

        // Cancel requests and associated devis
        List<RequestSupply> requestSupplyList = new ArrayList<>(this.requestSuplyRepo.findByForum(f));
        for (RequestSupply r: requestSupplyList) {
            r.setStatus(RequestSupplyStatus.Archived);
            Iterator<Devis> iterator = r.getDevis().iterator();
            while (iterator.hasNext()) {
                Devis d = iterator.next();
                this.sendSimpleEmail(d.getSocietyDevis().getEmail(), "Annulation du forum", mailBody);
                this.devisRepository.save(d);
            }
            this.requestSuplyRepo.save(r);
        }


        return this.forumRepo.save(f);
    }

    public void sendSimpleEmail(String toEmail, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("walahamdi0@gmail.com");
        message.setTo(toEmail);
        message.setText(body);
        message.setSubject(subject);
        mailSender.send(message);
        System.out.println("Mail Send...");
    }


    public Forum addImageToUser( Forum forum , MultipartFile image) {
        ResponseMessage message = new ResponseMessage();
        try {
            if (image != null) {
                //Forum forum = forumRepo.findById(1L).get();
                String newPhotoName = nameFile(image);
                forum.setAffiche(newPhotoName);
                File uploadDir = new File(uploadPath);
                if (!uploadDir.exists()) {
                    uploadDir.mkdir();
                }
                saveFile(image,newPhotoName);
                return forumRepo.save(forum);
            }else {
                message.setMessage("There is no image");
                return null;
            }
        } catch (IOException e) {
            message.setMessage(e.getMessage());
            return null;
        }
    }

    public void saveFile(MultipartFile multipartFile,String fileName) throws IOException{
        Path upload = Paths.get(uploadPath);
        if(!Files.exists(upload)){
            Files.createDirectories(upload);
        }
        try (InputStream inputStream = multipartFile.getInputStream()){
            Path filePath = upload.resolve(fileName);
            Files.copy(inputStream,filePath, StandardCopyOption.REPLACE_EXISTING);
        }catch (IOException e){
            throw new IOException("Could not save file");
        }
    }

    public String nameFile(MultipartFile multipartFile){
        String originalFileName = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        Integer fileDotIndex = originalFileName.lastIndexOf('.');
        String fileExtension = originalFileName.substring(fileDotIndex);
        return UUID.randomUUID().toString() + fileExtension;
    }

    @Override
    public Map<String, Float> calculateIncomes() {
        Map<String, Float> incomes = new HashMap<>();
        Forum f = this.forumRepo.findForumByForumStatus(ForumStatus.In_Progress);
        if (f == null) {
            f = this.getLatestForum();
        }
        float sumPrixPacksLatestForum = 0;
        float numberPacksLatestForum = 0;
        float sumPrixPacksPreviousForum = 0;
        for (Pack p : f.getPack()) {
            if (p.getReservationStatus() == ReservationStatus.Reserved) {
                sumPrixPacksLatestForum += p.getPrix();
                numberPacksLatestForum++;
            }
        }
        Forum previousForum = this.getPreviousForum();
        if (previousForum != null) {
            for (Pack p : previousForum.getPack()) {
                if (p.getReservationStatus() == ReservationStatus.Reserved) {
                    sumPrixPacksPreviousForum += p.getPrix();
                }
            }
            incomes.put("SumPrixPrcks", sumPrixPacksLatestForum);
            incomes.put("NumberPacks", numberPacksLatestForum);

            // Calculate the percentage increase
            float percentageIncrease = 0; // Valeur par défaut
            if (sumPrixPacksPreviousForum != 0) {
                percentageIncrease = ((sumPrixPacksLatestForum - sumPrixPacksPreviousForum) / sumPrixPacksPreviousForum) * 100;
                percentageIncrease = (float) (Math.round(percentageIncrease * 100.0) / 100.0); // Arrondir à deux chiffres après la virgule
            }
            incomes.put("TauxAugmentation", percentageIncrease);
        }
        return incomes;
    }

    public Forum getLatestForum() {
        List<Forum> forums = this.forumRepo.findAll();
        // Check if the list is empty
        if (forums.isEmpty()) {
            return null;
        }
        Forum latestForum = forums.get(0);
        for (Forum f : forums) {
            if (f.getDate().isAfter(latestForum.getDate())) {
                latestForum = f;
            }
        }
        return latestForum;
    }

    public Forum getPreviousForum() {
        List<Forum> forums = this.forumRepo.findAll();
        if (forums.isEmpty() || forums.size() == 1) {
            return null;
        }
        Collections.sort(forums, (f1, f2) -> f2.getDate().compareTo(f1.getDate()));
        return forums.get(1);
    }

    @Override
    public Forum getCurrentForumOrLatest() {
        if( forumRepo.findForumByForumStatus(ForumStatus.In_Progress)==null){
            return this.getLatestForum();
        }else {
            return forumRepo.findForumByForumStatus(ForumStatus.In_Progress);}
    }


}