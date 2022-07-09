package ro.fasttrackit.hotelapi.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@With
@Getter
@Setter
@Builder(toBuilder = true)
@AllArgsConstructor
public class Review {
    @Id
    private final String reviewId;
    private final String message;
    private final Integer rating;
    private final String touristName;
}
