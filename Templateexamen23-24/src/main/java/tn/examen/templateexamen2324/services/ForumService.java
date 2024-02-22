package tn.examen.templateexamen2324.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.examen.templateexamen2324.entity.Forum;
import tn.examen.templateexamen2324.entity.ForumStatus;
import tn.examen.templateexamen2324.entity.Pack;
import tn.examen.templateexamen2324.entity.Stand;
import tn.examen.templateexamen2324.repository.ForumRepo;
import tn.examen.templateexamen2324.repository.PackRepo;
import tn.examen.templateexamen2324.repository.StandRepo;

import java.util.List;

@Service
public class ForumService implements IForumService{

    @Autowired
    ForumRepo forumRepo;

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
        return forumRepo.save(forum);
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


}
