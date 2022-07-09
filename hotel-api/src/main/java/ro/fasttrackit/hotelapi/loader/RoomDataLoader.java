package ro.fasttrackit.hotelapi.loader;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ro.fasttrackit.hotelapi.model.*;
import ro.fasttrackit.hotelapi.repository.RoomRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class RoomDataLoader implements CommandLineRunner {
    private final RoomRepository repository;

    public void run(String... args) throws Exception{
        List.of(
                new Room(UUID.randomUUID().toString(), "23",2,"HotelNumberOne",
                        new Review("Good",5, "Andrei"),new Cleanup(LocalDate.now(),
                        new CleaningProcedure("Deep clean", 120)),
                        new RoomFacilities(true,false)),
                new Room(UUID.randomUUID().toString(), "41",1,"HotelNumberOne",
                        new Review("Ok",6, "Paul"),new Cleanup(LocalDate.now(),
                        new CleaningProcedure("Basic clean", 60)),
                        new RoomFacilities(true,false)),
                new Room(UUID.randomUUID().toString(), "3",0,"HotelNumberTwo",
                        new Review("Best",10, "Alex"),new Cleanup(LocalDate.now(),
                        new CleaningProcedure("Deep clean", 120)),
                        new RoomFacilities(true,true)),
                new Room(UUID.randomUUID().toString(), "53",5,"HotelNumberThree",
                        new Review("Good",8, "Ana"),new Cleanup(LocalDate.now(),
                        new CleaningProcedure("Basic Clean", 120)),
                        new RoomFacilities(true,false)),
                new Room(UUID.randomUUID().toString(), "35",3,"HotelNumberFour",
                        new Review("Not Good",3, "Adrian"),new Cleanup(LocalDate.now(),
                        new CleaningProcedure("Basic clean", 120)),
                        new RoomFacilities(false,false)),
                new Room(UUID.randomUUID().toString(), "71",7,"HotelNumberOne",
                        new Review("Alright",6, "Ana"),new Cleanup(LocalDate.now(),
                        new CleaningProcedure("Basic clean", 60)),
                        new RoomFacilities(false,true)),
                new Room(UUID.randomUUID().toString(), "124",10,"HotelNumberFour",
                        new Review("Nice view",8, "Ionut"),new Cleanup(LocalDate.now(),
                        new CleaningProcedure("Deep clean", 120)),
                        new RoomFacilities(true,false))
        ).forEach(repository::save);
        System.out.println(repository.findAll());
    }
}
