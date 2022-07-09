package ro.fasttrackit.hotelapi.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Document("cleanups")
@With
@Getter
@Setter
@Builder(toBuilder = true)
@AllArgsConstructor
public class Cleanup {
    @Id
     public String id;
     public LocalDate date;
     public CleaningProcedure cleaningProcedure;

}
