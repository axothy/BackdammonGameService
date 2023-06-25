package ru.axothy.backdammon.gameservice.repos;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import ru.axothy.backdammon.gameservice.model.Room;

public interface RoomRepository extends CrudRepository<Room, Integer> {
    Page<Room> findAll(Pageable pageable);
    Page<Room> findByIsGameStartedFalse(Pageable pageable);
    Room findById(int id);
    Room findByName(String name);
}
