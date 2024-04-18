package tn.examen.templateexamen2324.services;

import org.springframework.web.multipart.MultipartFile;
import tn.examen.templateexamen2324.entity.Forum;
import tn.examen.templateexamen2324.entity.ForumStatus;
import tn.examen.templateexamen2324.entity.Pack;
import tn.examen.templateexamen2324.entity.Stand;

import java.util.List;
import java.util.Map;

public interface IForumService {
     Forum addForum(Forum forum, MultipartFile image);
     List<Forum> retrieveAllForums();
     Forum retrieveForumById(long id);
     void deleteForum(long id);
     Forum updateForum(long id,Forum forum);
     Forum getCurrentForum();
     Forum getForumByStatus(ForumStatus forumStatus);
     Forum getForumById(long forumId);
     Map<String, Float> calculateIncomes();
     Forum cancelForum(long forumId);
     public Forum getCurrentForumOrLatest();


}