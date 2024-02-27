package tn.examen.templateexamen2324.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tn.examen.templateexamen2324.entity.Candidature;
import tn.examen.templateexamen2324.entity.Room;
import tn.examen.templateexamen2324.services.IRoomService;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/room")
public class RoomController {
    @Autowired
    IRoomService roomService;
    @GetMapping("/allrooms")
    public List<Room> GetAllRooms(){
        return roomService.getRooms();
    }
}
