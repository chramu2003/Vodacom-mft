package za.co.vodacom.vodacommft.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;
import java.util.stream.Collectors;


@ControllerAdvice
public class CustomizedResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<Object> handleAllException(Exception ex, WebRequest request){

        ExceptionResponseBean exceptionResponseBean = new ExceptionResponseBean(new Date(), ex.getMessage(), request.getDescription(false));

        return  new ResponseEntity(exceptionResponseBean, HttpStatus.INTERNAL_SERVER_ERROR );
    }

    @ExceptionHandler(NotFoundException.class)
    public final ResponseEntity<Object> handleUserNotFoundException(NotFoundException ex, WebRequest request){

        ExceptionResponseBean exceptionResponseBean = new ExceptionResponseBean(new Date(), ex.getMessage(), request.getDescription(false));

        return  new ResponseEntity(exceptionResponseBean, HttpStatus.NOT_FOUND);
    }

    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        String errorMessage = ex.getBindingResult().getAllErrors().stream().map(s -> ((FieldError) s).getField() + ":" + s.getDefaultMessage()).collect(Collectors.joining(","));

        ExceptionResponseBean exceptionResponseBean = new ExceptionResponseBean(new Date(), "Validation failed ",  errorMessage);

        return  new ResponseEntity(exceptionResponseBean, HttpStatus.BAD_REQUEST);
    }
}
