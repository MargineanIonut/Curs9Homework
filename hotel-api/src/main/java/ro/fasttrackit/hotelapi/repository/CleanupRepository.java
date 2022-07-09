package ro.fasttrackit.hotelapi.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import ro.fasttrackit.hotelapi.model.Cleanup;
import ro.fasttrackit.hotelapi.model.projection.RoomProjections;

import java.util.List;

public interface CleanupRepository extends MongoRepository<Cleanup,String> {
}
