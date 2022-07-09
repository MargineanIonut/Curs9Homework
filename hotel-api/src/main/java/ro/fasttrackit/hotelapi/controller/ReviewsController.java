package ro.fasttrackit.hotelapi.controller;

import com.github.fge.jsonpatch.JsonPatch;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ro.fasttrackit.hotelapi.model.Cleanup;
import ro.fasttrackit.hotelapi.model.Review;
import ro.fasttrackit.hotelapi.model.Room;
import ro.fasttrackit.hotelapi.service.ReviewService;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("review")
@RequiredArgsConstructor
public class ReviewsController {

    private ReviewService reviewService;


    @GetMapping("{id}")
    ArrayList<Review> getReviewForRoom(@PathVariable String id) {
        return reviewService.getReviews(id);
    }

    @PostMapping("{id}")
    Room createReview(@PathVariable String id, @RequestBody Review review) {
        return reviewService.createReview(id, review);
    }

    @PatchMapping("{id}")
    Room patchCleanup(@PathVariable String id, String reviewId, @RequestBody JsonPatch cleanup) {
        return reviewService.updateRoomReview(id, reviewId, cleanup);
    }

    @DeleteMapping("/{id}")
    void deleteCleanup(@PathVariable String id, String cleanupId) {
        reviewService.deleteReview(id, cleanupId);
    }
}
