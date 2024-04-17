package tn.examen.templateexamen2324.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.examen.templateexamen2324.dao.RoomRepo;
import tn.examen.templateexamen2324.entity.Room;
import tn.examen.templateexamen2324.entity.roomStatus;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoomService implements IRoomService{
    @Autowired
    RoomRepo roomRepo;
    @Override
    public List<Room> getRooms() {
        return roomRepo.findByStatus(roomStatus.AVAILABLE);
    }
}
