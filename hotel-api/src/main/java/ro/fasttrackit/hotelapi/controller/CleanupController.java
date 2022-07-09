package ro.fasttrackit.hotelapi.controller;

import com.github.fge.jsonpatch.JsonPatch;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ro.fasttrackit.hotelapi.model.Cleanup;
import ro.fasttrackit.hotelapi.model.Room;
import ro.fasttrackit.hotelapi.service.CleanupService;

import java.util.ArrayList;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("cleanup")
@RequiredArgsConstructor
public class CleanupController {

    private final CleanupService cleanupService;


    @GetMapping("{id}")
    ArrayList<Cleanup> getCleanupForRoom(@PathVariable String id) {
        return cleanupService.getCleanup(id);
    }

    @PostMapping("{id}")
    Room createCleanup(@PathVariable String roomId, @RequestBody Cleanup cleanup) {
        return cleanupService.createCleanup(roomId, cleanup);
    }

    @PatchMapping("{id}")
    Room patchCleanup(@PathVariable String id, String cleanupId, @RequestBody JsonPatch cleanup) {
        return cleanupService.updateCleanup(id, cleanupId, cleanup);
    }

    @DeleteMapping("/{id}")
    void deleteCleanup(@PathVariable String id, String cleanupId) {
        cleanupService.deleteCleanup(id, cleanupId);
    }
}
