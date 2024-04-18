package tn.examen.templateexamen2324.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tn.examen.templateexamen2324.entity.Forum;

import tn.examen.templateexamen2324.services.IForumService;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @GetMapping("/forum_incomes")
    @ResponseBody
    public Map<String,Float> getIncomes() {
        return  forumService.calculateIncomes();
    }

    @GetMapping("/find-current-forum")
    @ResponseBody
    public Forum getCurrentForum() {
        return  forumService.getCurrentForum();
    }


    @GetMapping("/find-forum/{forumId}")
    @ResponseBody
    public Forum getForumById(@PathVariable("forumId") long forumId) {
        return  forumService.getForumById(forumId);
    }

    @PostMapping("/add-forum")
    @ResponseBody
    public Forum createForum(@RequestParam MultipartFile image,@RequestParam String theme, @RequestParam LocalDate date , @RequestParam String localisation ,  @RequestParam String description  ) {
        Forum forum = new Forum();
        forum.setLocalisation(localisation);
        forum.setDescription(description);
        forum.setTheme(theme);
        forum.setDate(date);
        return forumService.addForum(forum,image);
    }
    @PutMapping("/update-forum/{id}")
    @ResponseBody
    public Forum updateForum(@PathVariable("id") int id, @RequestBody Forum forum) {
        return forumService.updateForum(id, forum);

    }

    @PutMapping("/cancel-forum/{id}")
    @ResponseBody
    public Forum updateForum(@PathVariable("id") Long id) {
        return forumService.cancelForum(id);

    }
    @DeleteMapping("/delete-forum/{id}")
    @ResponseBody
    public void deletePack(@PathVariable("id") long forumID) {
        forumService.deleteForum(forumID);
    }


    @GetMapping("/find-current-forum-or_latest")
    @ResponseBody
    public Forum getCurrentForumOrLatest() {
        return  forumService.getCurrentForumOrLatest();
    }


}