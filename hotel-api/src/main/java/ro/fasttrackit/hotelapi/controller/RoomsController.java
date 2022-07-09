package ro.fasttrackit.hotelapi.controller;

import com.github.fge.jsonpatch.JsonPatch;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import ro.fasttrackit.hotelapi.model.Cleanup;
import ro.fasttrackit.hotelapi.model.Room;
import ro.fasttrackit.hotelapi.model.RoomFilter;
import ro.fasttrackit.hotelapi.service.CleanupService;
import ro.fasttrackit.hotelapi.service.RoomService;

import java.util.ArrayList;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("rooms")
@RequiredArgsConstructor
public class RoomsController {

    private final RoomService service;

    @GetMapping
    Page<Room> getAll(RoomFilter filter, Pageable pageable) {
        return service.getAll(filter, pageable);
    }

    @GetMapping("filtered")
    Page<Room> getAllByFilter(RoomFilter filter, Pageable pageable) {
        return service.getAllByFilter(filter, pageable);
    }

    @PatchMapping("{id}")
    Room updateRoom(@PathVariable String id, @RequestBody JsonPatch updatedRoom) {
        return service.updateRoom(id, updatedRoom);
    }

    @DeleteMapping("delete/{id}")
    Room deleteRoom(@PathVariable String id) {
        return service.deleteRoom(id).orElseThrow(() -> new NoSuchElementException("Id:" + id + " does not exist"));
    }


}
