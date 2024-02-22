package tn.examen.templateexamen2324.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tn.examen.templateexamen2324.entity.Forum;
import tn.examen.templateexamen2324.entity.Pack;
import tn.examen.templateexamen2324.services.IForumService;
import tn.examen.templateexamen2324.services.IPackService;

import java.util.List;

@RestController
@RequestMapping("/forum")
@CrossOrigin(origins="http://localhost:4200")
public class ForumController {

    @Autowired
    IForumService forumService;

    @GetMapping("/find-all-forums")
    @ResponseBody
    public List<Forum> getForums() {
        List<Forum> listForum = forumService.retrieveAllForums();
        return listForum;
    }



    @PostMapping("/add-forum")
    @ResponseBody
    public Forum createPack(@RequestBody Forum b) {
        Forum forum = forumService.addForum(b);
        return forum;
    }
    @PutMapping("/update-forum/{id}")
    @ResponseBody
    public Forum updateBloc(@PathVariable("id") int id, @RequestBody Forum forum) {
        return forumService.updateForum(id, forum);

    }

    @PutMapping("/cancel-forum/{id}")
    @ResponseBody
    public Forum updateBloc(@PathVariable("id") Long id) {
        return forumService.cancelForum(id);

    }
   /* @DeleteMapping("/delete-pack/{id}")
    @ResponseBody
    public void deletePack(@PathVariable("id") int packId) {
        packService.deletePack(packId);
    }

    @PutMapping("/update-pack/{id}")
    @ResponseBody
    public Pack updateBloc(@PathVariable("id") int packId, @RequestBody Pack pack) {
        return packService.updatePack(packId, pack);

    }*/
}
