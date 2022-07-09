package ro.fasttrackit.hotelapi.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document("rooms")
@With
@Getter
@Setter
@Builder(toBuilder = true)
@AllArgsConstructor
public class Room {

    @Id
    private final String id;
    private final String number;
    private final Integer floor;
    private final String hotelName;
    private final Review review;
    ArrayList<Cleanup> cleanup;
    private final RoomFacilities roomFacilities;

}
