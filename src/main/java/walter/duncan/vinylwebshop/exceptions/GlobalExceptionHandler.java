package walter.duncan.vinylwebshop.exceptions;

import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.net.URI;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = ResourceNotFoundException.class)
    public ResponseEntity<ProblemDetail> resourceNotFoundExceptionHandler(ResourceNotFoundException exception) {
        var problemDetail = this.createProblemDetailBase(HttpStatus.NOT_FOUND);
        problemDetail.setType(URI.create("urn:vinyl-webshop:api:problem:resource:not-found"));
        problemDetail.setTitle("Resource Not Found");
        problemDetail.setDetail(exception.getMessage());

        return ResponseEntity.status(problemDetail.getStatus()).body(problemDetail);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> requestValidationExceptionHandler(MethodArgumentNotValidException exception) {
        var problemDetail = this.createProblemDetailBase(HttpStatus.BAD_REQUEST);
        problemDetail.setType(URI.create("urn:vinyl-webshop:api:problem:validation-error"));
        problemDetail.setTitle("Bad Request");
        problemDetail.setDetail("One or more validation errors occurred.");

        Map<String, String> validationErrors = new HashMap<>();
        exception.getBindingResult()
                .getFieldErrors()
                .forEach(fieldError -> validationErrors.put(fieldError.getField(), fieldError.getDefaultMessage()));

        // Nesting the errors like so allows for different types of errors in the future e.g. business rule violation errors, while keeping the response uniform.
        Map<String, Map<String, String>> errors = new HashMap<>();
        errors.put("validation", validationErrors);

        problemDetail.setProperty("errors", errors);

        return ResponseEntity.status(problemDetail.getStatus()).body(problemDetail);
    }

    // Spring boot wraps the MismatchedInputException in a HttpMessageNotReadableException exception. This took some time to figure out...
    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    public ResponseEntity<ProblemDetail> typeMismatchExceptionHandler(HttpMessageNotReadableException exception) {
        var problemDetail = this.createProblemDetailBase(HttpStatus.BAD_REQUEST);
        problemDetail.setType(URI.create("urn:vinyl-webshop:api:problem:type-mismatch"));
        problemDetail.setTitle("Bad Request");

        if (exception.getMostSpecificCause() instanceof MismatchedInputException mismatchedInputException) {
            problemDetail.setDetail(this.getTypeMismatchMessage(mismatchedInputException));
        } else {
            return this.uncaughtExceptionHandler(exception);
        }

        return ResponseEntity.status(problemDetail.getStatus()).body(problemDetail);
    }

    // TODO: Investigate if a notification collector approach is more sustainable and efficient.
    @ExceptionHandler(value = BusinessRuleViolationException.class)
    public ResponseEntity<ProblemDetail> businessRuleViolationExceptionHandler(BusinessRuleViolationException exception) {
        var problemDetail = this.createProblemDetailBase(HttpStatus.UNPROCESSABLE_ENTITY);
        problemDetail.setType(URI.create("urn:vinyl-webshop:api:problem:business-rule-violation"));
        problemDetail.setTitle("Unprocessable Entity");
        problemDetail.setDetail("A business rule was violated.");

        Map<String, String> businessRuleErrors = new HashMap<>();
        businessRuleErrors.put(exception.getViolation().toString(), exception.getMessage());

        /* TODO: Look into collecting multiple business rule violations during one response.
            At the moment this can't be achieved yet since a BusinessRuleViolationException is thrown when it is found.
            Perhaps this can be fixed with the a notification collector?
         */
        Map<String, Map<String, String>> errors = new HashMap<>();
        errors.put("businessRuleViolation", businessRuleErrors);
        problemDetail.setProperty("errors", errors);

        return ResponseEntity.status(problemDetail.getStatus()).body(problemDetail);
    }

    @ExceptionHandler(value = RuntimeException.class)
    public ResponseEntity<ProblemDetail> uncaughtExceptionHandler(RuntimeException exception) {
        // TODO: Log exception stacktrace and other details somewhere so that the application can be improved upon.

        var problemDetail = this.createProblemDetailBase(HttpStatus.INTERNAL_SERVER_ERROR);
        problemDetail.setType(URI.create("urn:vinyl-webshop:api:problem:internal-server-error"));
        problemDetail.setTitle("Internal Server Error");
        problemDetail.setDetail("An internal server error occurred while processing your request. Please contact support if the issue persists.");

        return ResponseEntity.status(problemDetail.getStatus()).body(problemDetail);
    }

    private String getTypeMismatchMessage(MismatchedInputException mismatchedInputException) {
        String field = mismatchedInputException.getPath().isEmpty()
                ? "unknown"
                : mismatchedInputException.getPath().getFirst().getFieldName();

        String expectedType = mismatchedInputException.getTargetType() != null
                ? mismatchedInputException.getTargetType().getSimpleName()
                : "unknown";

        return String.format("Field '%s' must be of type %s.", field, expectedType);
    }

    private ProblemDetail createProblemDetailBase(HttpStatus httpStatus) {
        var problemDetail = ProblemDetail.forStatus(httpStatus);
        problemDetail.setProperty("timestamp", Instant.now());

        return problemDetail;
    }
}