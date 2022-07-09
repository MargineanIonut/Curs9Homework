package ro.fasttrackit.hotelapi.model;

import org.springframework.data.mongodb.core.mapping.Document;

@Document("reviews")
public record Review(String message, Integer rating, String touristName) {
}
