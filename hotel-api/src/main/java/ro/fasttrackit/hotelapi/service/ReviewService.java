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
import ro.fasttrackit.hotelapi.model.Review;
import ro.fasttrackit.hotelapi.model.Room;
import ro.fasttrackit.hotelapi.repository.RoomRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final RoomRepository repository;

    private final String MESSAGE = "Resource not found";

    public ArrayList<Review> getReviews(String id) {
        return repository.findById(id).get().getReview();
    }

    public Room createReview(String id, Review review) {
        Room room = getRoom(id);
        room.getReview().add(review);
        return repository.save(room);
    }

    private Room getRoom(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(MESSAGE));
    }


    public Room updateRoomReview(String id, String reviewId, JsonPatch updatedRoomReview) {
        return repository.findById(id)
                .map(dbEntity -> applyPatch(dbEntity, updatedRoomReview))
                .map(dbEntity -> replaceRoomReview(id, reviewId, dbEntity))
                .orElseThrow(() -> new ResourceNotFoundException(MESSAGE));
    }

    public Room replaceRoomReview(String id, String reviewId, Review newReview) {
        Review dbReview = getDbReview(id, reviewId);

        Review updatedReview = dbReview
                .withMessage(newReview.getMessage())
                .withRating(newReview.getRating())
                .withTouristName(newReview.getTouristName());

        Room updateRoomReview = getRoom(id);

        int indexOf = getIndexOfReview(id, reviewId);

        updateRoomReview.getReview().add(indexOf, updatedReview);

        return repository.save(updateRoomReview);
    }

    private Review getDbReview(String id, String reviewId) {
        return repository.findById(id).orElseThrow()
                .getReview()
                .stream()
                .filter(x -> reviewId.equals(x.getReviewId()))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException(MESSAGE));
    }

    private int getIndexOfReview(String roomId, String reviewId) {
        int indexOf = getRoom(roomId).getReview().indexOf(getRoom(roomId).getReview().stream()
                .filter(x->reviewId.equals(x.getReviewId()))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException(MESSAGE)));
        return indexOf;
    }

    private Review applyPatch(Room dbEntity, JsonPatch jsonPatch) {
        try {
            ObjectMapper jsonMapper = new ObjectMapper();
            JsonNode jsonNode = jsonMapper.convertValue(dbEntity, JsonNode.class);
            JsonNode patchedJson = jsonPatch.apply(jsonNode);
            return jsonMapper.treeToValue(patchedJson, Review.class);
        } catch (JsonProcessingException | JsonPatchException e) {
            throw new RuntimeException(e);
        }
    }


    public void deleteReview(String id, String reviewId) {
        repository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException(MESSAGE))
                .getReview().remove(getIndexOfReview(id, reviewId));
    }
}
