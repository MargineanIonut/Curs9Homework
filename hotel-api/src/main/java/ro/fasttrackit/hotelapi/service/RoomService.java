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

    private final String MESSAGE = "Resource not found";


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
                .orElseThrow(() -> new ResourceNotFoundException(MESSAGE));
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



    public Optional<Room> deleteRoom(String id) {
        var room = repository.findById(id);
        room.ifPresent(repository::delete);
        return room;
    }


    private Room getDbRoom(String roomId) {
        Room dbRoom = repository.findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException(MESSAGE));
        return dbRoom;
    }

}
