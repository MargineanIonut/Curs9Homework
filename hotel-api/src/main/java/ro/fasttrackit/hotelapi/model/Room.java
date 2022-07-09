package ro.fasttrackit.hotelapi.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

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
    private final Cleanup cleanup;
    private final RoomFacilities roomFacilities;

}
