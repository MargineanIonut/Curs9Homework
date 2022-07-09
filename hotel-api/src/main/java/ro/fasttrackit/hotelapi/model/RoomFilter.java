package ro.fasttrackit.hotelapi.model;

public record RoomFilter(
        String id,
        String number,
        Integer floor,
        String hotelName,
        Cleanup cleanup,
        Boolean tv,
        Boolean doubleBed) {

}
