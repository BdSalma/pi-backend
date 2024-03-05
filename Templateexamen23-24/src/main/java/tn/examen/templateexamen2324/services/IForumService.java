package tn.examen.templateexamen2324.services;

import tn.examen.templateexamen2324.entity.Forum;
import tn.examen.templateexamen2324.entity.ForumStatus;
import tn.examen.templateexamen2324.entity.Pack;
import tn.examen.templateexamen2324.entity.Stand;

import java.util.List;

public interface IForumService {
     Forum addForum(Forum stand);
     List<Forum> retrieveAllForums();
     Forum retrieveForumById(long id);
     void deleteForum(long id);
     Forum updateForum(long id,Forum forum);
     Forum getCurrentForum();
     Forum cancelForum(Long id  );
     Forum getForumByStatus(ForumStatus forumStatus);

     Forum getForumById(long forumId);
}
