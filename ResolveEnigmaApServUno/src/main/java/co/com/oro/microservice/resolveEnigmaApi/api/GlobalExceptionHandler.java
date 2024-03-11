package co.com.oro.microservice.resolveEnigmaApi.api;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import co.com.oro.microservice.resolveEnigmaApi.model.ErrorDetail;
import co.com.oro.microservice.resolveEnigmaApi.model.JsonApiBodyResponseErrors;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionHandler {
	
	@Autowired
	private HttpServletRequest request;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<?> handleValidationException(MethodArgumentNotValidException ex) {
        // Your logic for handling validation exception
        // ...
    	 
        JsonApiBodyResponseErrors errorResponse = new JsonApiBodyResponseErrors();
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();

        for (FieldError fieldError : fieldErrors) {
            ErrorDetail errorDetail = new ErrorDetail()
                    .code("Code")
                    .id("id")
                    .title("Validation Error in Request Body")
                    .detail(fieldError.getDefaultMessage())
                    .source(request.getRequestURI())
                    .status(HttpStatus.BAD_REQUEST.toString());
            errorResponse.addErrorsItem(errorDetail);
        }

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    // Add more @ExceptionHandler methods for other types of exceptions if needed
}