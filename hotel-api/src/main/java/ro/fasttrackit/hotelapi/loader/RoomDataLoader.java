package ro.fasttrackit.hotelapi.loader;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ro.fasttrackit.hotelapi.model.*;
import ro.fasttrackit.hotelapi.repository.RoomRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class RoomDataLoader implements CommandLineRunner {
    private final RoomRepository repository;

    public void run(String... args) throws Exception{
        List.of(
                new Room(UUID.randomUUID().toString(), "23",2,"HotelNumberOne",
                        new ArrayList<>(List.of(new Review(UUID.randomUUID().toString(),"Good",5, "Andrei"))),
                        new ArrayList<>(List.of(new Cleanup(UUID.randomUUID().toString(),LocalDate.now(), new CleaningProcedure("Deep clean", 120)))),
                        new RoomFacilities(true,false)),

                new Room(UUID.randomUUID().toString(), "41",1,"HotelNumberOne",
                        new ArrayList<>(List.of(new Review(UUID.randomUUID().toString(),"Ok",6, "Paul"))),
                        new ArrayList<>(List.of(new Cleanup(UUID.randomUUID().toString(),LocalDate.now(), new CleaningProcedure("Basic clean", 60)))),
                        new RoomFacilities(true,false)),

                new Room(UUID.randomUUID().toString(), "3",0,"HotelNumberTwo",
                        new ArrayList<>(List.of(new Review(UUID.randomUUID().toString(),"Best",10, "Alex"))),
                        new ArrayList<>(List.of(new Cleanup(UUID.randomUUID().toString(),LocalDate.now(), new CleaningProcedure("Deep clean", 120)))),
                        new RoomFacilities(true,true)),

                new Room(UUID.randomUUID().toString(), "53",5,"HotelNumberThree",
                        new ArrayList<>(List.of(new Review(UUID.randomUUID().toString(),"Good",8, "Ana"))),
                        new ArrayList<>(List.of(new Cleanup(UUID.randomUUID().toString(),LocalDate.now(), new CleaningProcedure("Basic Clean", 120)))),
                        new RoomFacilities(true,false)),

                new Room(UUID.randomUUID().toString(), "35",3,"HotelNumberFour",
                        new ArrayList<>(List.of(new Review(UUID.randomUUID().toString(),"Not Good",3, "Adrian"))),
                        new ArrayList<>(List.of(new Cleanup(UUID.randomUUID().toString(),LocalDate.now(), new CleaningProcedure("Basic clean", 120)))),
                        new RoomFacilities(false,false)),

                new Room(UUID.randomUUID().toString(), "71",7,"HotelNumberOne",
                        new ArrayList<>(List.of(new Review(UUID.randomUUID().toString(),"Alright",6, "Ana"))),
                        new ArrayList<>(List.of(new Cleanup(UUID.randomUUID().toString(),LocalDate.now(), new CleaningProcedure("Basic clean", 60)))),
                        new RoomFacilities(false,true)),

                new Room(UUID.randomUUID().toString(), "124",10,"HotelNumberFour",
                        new ArrayList<>(List.of(new Review(UUID.randomUUID().toString(),"Nice view",8, "Ionut"))),
                        new ArrayList<>(List.of(new Cleanup(UUID.randomUUID().toString(),LocalDate.now(), new CleaningProcedure("Deep clean", 120)))),
                        new RoomFacilities(true,false))
        ).forEach(repository::save);
    }
}
