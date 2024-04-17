package tn.examen.templateexamen2324.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.examen.templateexamen2324.entity.Forum;
import tn.examen.templateexamen2324.entity.ForumStatus;
import tn.examen.templateexamen2324.entity.Stand;

import java.util.ArrayList;

@Repository
public interface ForumRepo extends JpaRepository<Forum, Long> {
    Forum findForumByForumStatus(ForumStatus forumStatus);


}