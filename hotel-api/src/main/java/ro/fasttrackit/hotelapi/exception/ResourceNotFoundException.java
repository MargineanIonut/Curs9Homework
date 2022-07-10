package ro.fasttrackit.hotelapi.exception;

import lombok.Data;

@Data
public class ResourceNotFoundException  extends RuntimeException{
    private final String message;
}
