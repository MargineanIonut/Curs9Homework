package ro.fasttrackit.hotelapi.model;

import org.springframework.data.mongodb.core.mapping.Document;

@Document("procedures")
public record CleaningProcedure(String name, Integer duration) {
}
