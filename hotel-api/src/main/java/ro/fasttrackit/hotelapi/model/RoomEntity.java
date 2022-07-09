package ro.fasttrackit.hotelapi.model;

import java.util.List;

public record RoomEntity(String id,
                         String number,
                         Integer floor,
                         String hotelName,
                         Review review,
                         List<Cleanup> cleanup,
                         RoomFacilities roomFacilities) {
}
