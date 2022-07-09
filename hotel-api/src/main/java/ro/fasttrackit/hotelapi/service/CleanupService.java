package ro.fasttrackit.hotelapi.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ro.fasttrackit.hotelapi.exception.ResourceNotFoundException;
import ro.fasttrackit.hotelapi.model.Cleanup;
import ro.fasttrackit.hotelapi.model.Room;
import ro.fasttrackit.hotelapi.repository.RoomRepository;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class CleanupService {

    private final RoomRepository repository;


    public ArrayList<Cleanup> getCleanup(String roomId) {
        return repository.findById(roomId).get().getCleanup();
    }

    public Room createCleanup(String roomId, Cleanup cleanup) {
        Room dbRoom = getDbRoom(roomId);
        dbRoom.getCleanup().add(cleanup);
        return repository.save(dbRoom);
    }


    private Room getDbRoom(String roomId) {
        Room dbRoom = repository.findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Could not find person with id %s".formatted(roomId)));
        return dbRoom;
    }

    public Room updateCleanup(String roomId, String cleanupId, JsonPatch updatedCleanup) {

        return repository.findById(roomId).get()
                .getCleanup()
                .stream()
                .filter(x -> x.getId().equals(cleanupId))
                .findFirst()
                .map(dbEntity -> applyCleanupPatch(dbEntity, updatedCleanup))
                .map(dbEntity -> replaceCleanup(roomId, cleanupId, dbEntity))
                .orElseThrow(() -> new ResourceNotFoundException("Could not find cleanup with id %s" + cleanupId));

    }

    public Room replaceCleanup(String roomId, String cleanupId, Cleanup newCleanup) {
        Cleanup dbCleanup = getDbRoom(roomId).getCleanup()
                .stream()
                .filter(x -> cleanupId.equals(x.getId()))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Resource not found"));

        Cleanup updatedCleanup = dbCleanup
                .withDate(newCleanup.getDate())
                .withCleaningProcedure(newCleanup.getCleaningProcedure());

        Room updateRoomCleanup = getDbRoom(roomId);
        int indexOf = updateRoomCleanup.getCleanup().indexOf(getDbRoom(roomId).getCleanup()
                .stream()
                .filter(x -> cleanupId.equals(x.getId()))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Resource not found")));
        updateRoomCleanup.getCleanup().add(indexOf, updatedCleanup);

        return repository.save(updateRoomCleanup);
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
}
