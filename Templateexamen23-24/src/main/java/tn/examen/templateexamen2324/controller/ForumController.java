package tn.examen.templateexamen2324.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tn.examen.templateexamen2324.entity.Forum;

import tn.examen.templateexamen2324.services.IForumService;

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
        return  forumService.retrieveAllForums();
    }

    @GetMapping("/find-forum/{forumId}")
    @ResponseBody
    public Forum getForumById(@PathVariable("forumId") long forumId) {
        return  forumService.getForumById(forumId);

    }

    @PostMapping("/add-forum")
    @ResponseBody
    public Forum createPack(@RequestBody Forum b) {

        return forumService.addForum(b);
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
