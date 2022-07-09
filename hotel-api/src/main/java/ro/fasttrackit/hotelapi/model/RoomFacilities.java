package ro.fasttrackit.hotelapi.model;

import org.springframework.data.mongodb.core.mapping.Document;

@Document("facilities")
public record RoomFacilities(boolean tv, boolean doubleBed) {
}
