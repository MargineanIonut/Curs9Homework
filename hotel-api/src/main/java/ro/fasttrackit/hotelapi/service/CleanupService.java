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

    private final String MESSAGE = "Resource not found";

    public ArrayList<Cleanup> getCleanup(String roomId) {
        return repository.findById(roomId).get().getCleanup();
    }

    public Room createCleanup(String roomId, Cleanup cleanup) {
        Room dbRoom = getRoom(roomId);
        dbRoom.getCleanup().add(cleanup);
        return repository.save(dbRoom);
    }


    public Room updateCleanup(String roomId, String cleanupId, JsonPatch updatedCleanup) {

        return repository.findById(roomId).get()
                .getCleanup()
                .stream()
                .filter(x -> x.getId().equals(cleanupId))
                .findFirst()
                .map(dbEntity -> applyCleanupPatch(dbEntity, updatedCleanup))
                .map(dbEntity -> replaceCleanup(roomId, cleanupId, dbEntity))
                .orElseThrow(() -> new ResourceNotFoundException(MESSAGE));

    }

    public Room replaceCleanup(String roomId, String cleanupId, Cleanup newCleanup) {
        Cleanup dbCleanup = getRoom(roomId).getCleanup()
                .stream()
                .filter(x -> cleanupId.equals(x.getId()))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException(MESSAGE));

        Cleanup updatedCleanup = dbCleanup
                .withDate(newCleanup.getDate())
                .withCleaningProcedure(newCleanup.getCleaningProcedure());

        Room updateRoomCleanup = getRoom(roomId);
        int indexOf = getIndexOfCleanup(roomId, cleanupId);
        updateRoomCleanup.getCleanup().add(indexOf, updatedCleanup);

        return repository.save(updateRoomCleanup);
    }

    private int getIndexOfCleanup(String roomId, String cleanupId) {
        int indexOf = getRoom(roomId).getCleanup().indexOf(getRoom(roomId).getCleanup()
                .stream()
                .filter(x -> cleanupId.equals(x.getId()))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException(MESSAGE)));
        return indexOf;
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

    public void deleteCleanup(String id,String cleanupId) {
      repository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Resource not found"))
                .getCleanup().remove(getIndexOfCleanup(id,cleanupId));
    }

    private Room getRoom(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(MESSAGE));
    }
}
