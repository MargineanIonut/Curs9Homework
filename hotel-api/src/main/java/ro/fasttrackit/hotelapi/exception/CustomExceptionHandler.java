package ro.fasttrackit.hotelapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler({ResourceNotFoundException.class})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResourceNotFoundException handleException(ResourceNotFoundException e){
        return new ResourceNotFoundException(e.getMessage());
    }
}
