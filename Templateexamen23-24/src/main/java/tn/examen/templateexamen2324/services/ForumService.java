package tn.examen.templateexamen2324.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.examen.templateexamen2324.entity.*;
import tn.examen.templateexamen2324.repository.ForumRepo;
import tn.examen.templateexamen2324.repository.PackRepo;
import tn.examen.templateexamen2324.repository.StandRepo;

import java.util.List;

@Service
public class ForumService implements IForumService{

    @Autowired
    ForumRepo forumRepo;

    @Autowired
    PackRepo packRepo;



    @Autowired
    StandRepo standRepo;

    @Override
    public Forum addForum(Forum forum) {

        return forumRepo.save(forum);
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
    public Forum cancelForum(Long id  ) {
        Forum f = this.forumRepo.findById(id).get();
        f.setForumStatus(ForumStatus.Canceled);
        return forumRepo.save(f);
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
       Forum f = forumRepo.findById(forumId).get();
       f.setForumStatus(ForumStatus.Canceled);
        for (Pack p : f.getPack()) {
            p.setReservationStatus(ReservationStatus.Archived);
            p.getStand().setReserved(false);
            this.notifyUser(p.getReserver().getId());
            this.standRepo.save(p.getStand());
            this.packRepo.save(p);
        }
       return this.forumRepo.save(f);
    }

   public void notifyUser(String idUser){

   }
}
