package tn.examen.templateexamen2324.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import tn.examen.templateexamen2324.entity.Room;
@Repository
public interface RoomRepo  extends CrudRepository<Room,Long> {
    Room findRoomByNum(int roomNum);
}
