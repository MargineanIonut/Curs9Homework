package ro.fasttrackit.hotelapi.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ro.fasttrackit.hotelapi.exception.ResourceNotFoundException;
import ro.fasttrackit.hotelapi.model.Cleanup;
import ro.fasttrackit.hotelapi.model.RoomFilter;
import ro.fasttrackit.hotelapi.model.Room;
import ro.fasttrackit.hotelapi.repository.RoomDAO;
import ro.fasttrackit.hotelapi.repository.RoomRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoomService {
    private final RoomRepository repository;
    private final RoomDAO dao;

    public Page<Room> getAll(RoomFilter filter, Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Page<Room> getAllByFilter(RoomFilter filter, Pageable pageable) {
        return dao.findBy(filter, pageable);
    }

    public Room updateRoom(String id, JsonPatch updatedRoom) {
        return repository.findById(id)
                .map(dbEntity -> applyPatch(dbEntity, updatedRoom))
                .map(dbEntity -> replaceRoom(id, dbEntity))
                .orElseThrow(() -> new ResourceNotFoundException("Could not find person with id %s" + id));
    }

    public Room replaceRoom(String id, Room newRoom) {
        Room dbRoom = getDbRoom(id);
        return repository.save(dbRoom
                .withNumber(newRoom.getNumber())
                .withFloor(newRoom.getFloor())
                .withHotelName(newRoom.getHotelName())
                .withReview(newRoom.getReview()))
                .withCleanup(newRoom.getCleanup())
                .withRoomFacilities(newRoom.getRoomFacilities());
    }


    private Room applyPatch(Room dbEntity, JsonPatch jsonPatch) {
        try {
            ObjectMapper jsonMapper = new ObjectMapper();
            JsonNode jsonNode = jsonMapper.convertValue(dbEntity, JsonNode.class);
            JsonNode patchedJson = jsonPatch.apply(jsonNode);
            return jsonMapper.treeToValue(patchedJson, Room.class);
        } catch (JsonProcessingException | JsonPatchException e) {
            throw new RuntimeException(e);
        }
    }

    private Cleanup applyCleanupPatch(Cleanup dbEntity, JsonPatch jsonPatch) {
        try {
            ObjectMapper jsonMapper = new ObjectMapper();
            JsonNode jsonNode = jsonMapper.convertValue(dbEntity, JsonNode.class);
            JsonNode patchedJson = jsonPatch.apply(jsonNode);
            return jsonMapper.treeToValue(patchedJson, Cleanup.class);
        } catch (JsonProcessingException | JsonPatchException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<Room> deleteRoom(String id) {
        var room = repository.findById(id);
        room.ifPresent(repository::delete);
        return room;
    }


    private Room getDbRoom(String roomId) {
        Room dbRoom = repository.findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Could not find person with id %s".formatted(roomId)));
        return dbRoom;
    }

    public ArrayList<Cleanup> getCleanup(String id) {
        return repository.findById(id).get().getCleanup();
    }

    public Room createCleanup(String roomId, Cleanup cleanup) {
        Room dbRoom = getDbRoom(roomId);
        dbRoom.getCleanup().add(cleanup);
        return repository.save(dbRoom);
    }


    public Room replaceCleanup(String roomId,String cleanupId, Cleanup newCleanup) {
        Cleanup dbCleanup = getDbRoom(roomId).getCleanup()
                .stream()
                .filter(x->cleanupId.equals(x.getId()))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Resource not found"));

        Cleanup updatedCleanup = dbCleanup
                .withDate(newCleanup.getDate())
                .withCleaningProcedure(newCleanup.getCleaningProcedure());

        Room updateRoomCleanup = getDbRoom(roomId);
        int indexOf = updateRoomCleanup.getCleanup().indexOf(dbCleanup);
        updateRoomCleanup.getCleanup(updateRoomCleanup.getCleanup().set(indexOf, updatedCleanup))

    return repository.save();
    }

    public Room updateCleanup(String roomId, String cleanupId, JsonPatch updatedCleanup) {

        return repository.findById(roomId).get()
                .getCleanup()
                .stream()
                .filter(x->x.getId().equals(cleanupId))
                .findFirst()
                .map(dbEntity -> applyCleanupPatch(dbEntity, updatedCleanup))
                .map(dbEntity -> replaceCleanup(roomId,cleanupId, dbEntity))
                .orElseThrow(() -> new ResourceNotFoundException("Could not find cleanup with id %s" + cleanupId));
        ;
    }
}
