package ro.fasttrackit.hotelapi.model;

import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Document("cleanups")
public record Cleanup(LocalDate date, CleaningProcedure cleaningProcedure) {

}
