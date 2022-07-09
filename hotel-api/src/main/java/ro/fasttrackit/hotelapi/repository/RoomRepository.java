package ro.fasttrackit.hotelapi.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import ro.fasttrackit.hotelapi.model.CleaningProcedure;
import ro.fasttrackit.hotelapi.model.Room;
import ro.fasttrackit.hotelapi.model.projection.RoomProjections;

import java.time.LocalDate;
import java.util.List;

public interface RoomRepository extends MongoRepository<Room, String> {

    List<RoomProjections> findByCleanup(LocalDate date, CleaningProcedure cleaningProcedure);
}
