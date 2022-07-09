package ro.fasttrackit.hotelapi.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Document("cleanups")
public record Cleanup(
        @Id
        String id,
        LocalDate date,
        CleaningProcedure cleaningProcedure) {

}
