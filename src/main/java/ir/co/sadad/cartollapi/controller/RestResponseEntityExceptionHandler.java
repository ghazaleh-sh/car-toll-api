package ir.co.sadad.cartollapi.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ir.co.sadad.cartollapi.dtos.GeneralErrorResponse;
import ir.co.sadad.cartollapi.dtos.HttpClientErrorDto;
import ir.co.sadad.cartollapi.exception.CarTollException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.JDBCConnectionException;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;

import javax.validation.ConstraintViolation;
import java.net.ConnectException;
import java.util.*;

import static org.springframework.http.HttpStatus.BAD_REQUEST;


@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
public class RestResponseEntityExceptionHandler {

    private final MessageSource messageSource;

    @ExceptionHandler(CarTollException.class)
    public ResponseEntity<GeneralErrorResponse> handleCoreServiceException(CarTollException ex) {

        String localizedMessage;
        String descriptionFA = "";
        try {
            localizedMessage = messageSource.getMessage(ex.getMessage(), null, new Locale("fa"));
        } catch (NoSuchMessageException e) {
            localizedMessage = ex.getMessage();
        }

        try {
            descriptionFA = ex.getDescription() != null ? messageSource.getMessage(ex.getDescription(), null, new Locale("fa")) : "";
        } catch (NoSuchMessageException e) {
            descriptionFA = "";
        }

        GeneralErrorResponse generalErrorResponse = new GeneralErrorResponse();
        generalErrorResponse
                .setStatus(ex.getHttpStatus())
                .setTimestamp(new Date().getTime())
                .setCode("E" + (ex.getCode() == null ? ex.getHttpStatus().value() : ex.getCode()) + "CRTLL")
                .setLocalizedMessage(localizedMessage.concat(" ").concat(descriptionFA))
                .setMessage(ex.getDescription());

        return new ResponseEntity<>(generalErrorResponse, ex.getHttpStatus());

    }


    @ExceptionHandler(value = {javax.validation.ConstraintViolationException.class})
    protected ResponseEntity<GeneralErrorResponse> handleConstraintViolation(javax.validation.ConstraintViolationException ex) {
        String localizedMessage;
        try {
            localizedMessage = messageSource.getMessage(ex.getMessage(), null, new Locale("fa"));
        } catch (NoSuchMessageException e) {
            localizedMessage = ex.getMessage();
        }
        GeneralErrorResponse generalErrorResponse = new GeneralErrorResponse();
        generalErrorResponse
                .setStatus(BAD_REQUEST)
                .setTimestamp(new Date().getTime())
                .setCode(messageSource.getMessage("parameter.has.error", null, new Locale("fa")))
                .setLocalizedMessage(localizedMessage);


        for (ConstraintViolation constraintViolation : ex.getConstraintViolations()) {

            List<GeneralErrorResponse.SubError> subErrorList = new ArrayList<>();

            GeneralErrorResponse.SubError subError = new GeneralErrorResponse.SubError();
            subError.setCode("E" + HttpStatus.BAD_REQUEST.value() + "CRTLL");
            subError.setTimestamp(new Date().getTime());
            try {
                subError.setLocalizedMessage(messageSource.getMessage(Objects.requireNonNull(constraintViolation.getMessage()),
                        null, new Locale("fa")));
            } catch (NoSuchMessageException exp) {
                subError.setLocalizedMessage(constraintViolation.getMessage());
            }
            subErrorList.add(subError);
            generalErrorResponse.setSubErrors(subErrorList);
        }

        return new ResponseEntity<>(generalErrorResponse, BAD_REQUEST);
    }

    @ExceptionHandler(ConnectException.class)
    public ResponseEntity<GeneralErrorResponse> handleConnectException(ConnectException ex) {
        log.warn("Connection Timeout Exception: ", ex);

        GeneralErrorResponse generalErrorResponse = new GeneralErrorResponse();
        generalErrorResponse
                .setStatus(HttpStatus.REQUEST_TIMEOUT)
                .setTimestamp(new Date().getTime())
                .setCode("E" + HttpStatus.REQUEST_TIMEOUT.value() + "CRTLL")
                .setLocalizedMessage(messageSource.getMessage("core.service.timeout.exception", null, new Locale("fa")));

        return new ResponseEntity<>(generalErrorResponse, HttpStatus.REQUEST_TIMEOUT);

    }

    @ExceptionHandler(JDBCConnectionException.class)
    public ResponseEntity<GeneralErrorResponse> handleJDBCConnectionException(JDBCConnectionException ex) {
        log.warn("JDBC Connection Exception: ", ex);

        GeneralErrorResponse generalErrorResponse = new GeneralErrorResponse();
        generalErrorResponse
                .setStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                .setTimestamp(new Date().getTime())
                .setCode("E" + HttpStatus.INTERNAL_SERVER_ERROR.value() + "CRTLL")
                .setLocalizedMessage(messageSource.getMessage("database.connection.exception", null, new Locale("fa")));

        return new ResponseEntity<>(generalErrorResponse, HttpStatus.INTERNAL_SERVER_ERROR);

    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<GeneralErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        log.warn("api calling exception", ex);

        GeneralErrorResponse generalErrorResponse = new GeneralErrorResponse();
        generalErrorResponse
                .setStatus(HttpStatus.BAD_REQUEST)
                .setTimestamp(new Date().getTime())
                .setCode("E" + HttpStatus.BAD_REQUEST.value() + "CRTLL")
                .setLocalizedMessage(messageSource.getMessage("http.message.not.readable.exception", null, new Locale("fa")));

        return new ResponseEntity<>(generalErrorResponse, HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<GeneralErrorResponse> handleValidationExceptions(
            MethodArgumentNotValidException ex) {

        log.warn("validation exception", ex);
        String generalMsg = messageSource.getMessage("method.argument.not.valid", null, new Locale("fa"));

        List<GeneralErrorResponse.SubError> subErrorList = new ArrayList<>();

        ex.getBindingResult().getAllErrors().forEach((error) -> {
            GeneralErrorResponse.SubError subError = new GeneralErrorResponse.SubError();
            subError.setCode("E" + HttpStatus.BAD_REQUEST.value() + "CRTLL");
            subError.setTimestamp(new Date().getTime());
            try {
                subError.setLocalizedMessage(messageSource.getMessage(Objects.requireNonNull(error.getDefaultMessage()), null, new Locale("fa")));
            } catch (NoSuchMessageException exp) {
                subError.setLocalizedMessage(error.getDefaultMessage());
            }
            subErrorList.add(subError);
        });

        GeneralErrorResponse generalErrorResponse = new GeneralErrorResponse();
        generalErrorResponse
                .setStatus(HttpStatus.BAD_REQUEST)
                .setTimestamp(new Date().getTime())
                .setCode("E" + HttpStatus.BAD_REQUEST.value() + "CRTLL")
                .setMessage(generalMsg)
                .setLocalizedMessage(generalMsg)
                .setSubErrors(subErrorList);
        return new ResponseEntity<>(generalErrorResponse, HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<GeneralErrorResponse> handleMissingRequestHeaderExceptions(
            MissingRequestHeaderException ex) {

        log.warn("missing request header exception", ex);

        GeneralErrorResponse generalErrorResponse = new GeneralErrorResponse();
        generalErrorResponse
                .setStatus(HttpStatus.BAD_REQUEST)
                .setTimestamp(new Date().getTime())
                .setCode("E" + HttpStatus.BAD_REQUEST.value() + "CRTLL")
                .setLocalizedMessage(ex.getLocalizedMessage());
        return new ResponseEntity<>(generalErrorResponse, HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<GeneralErrorResponse> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {

        log.error("validation exception", ex.getRootCause());

        String message = Optional.ofNullable(ex.getRootCause()).map(Throwable::getMessage).orElse(null);
        if (message != null && message.contains("SQLCODE") && message.contains("-803")) {
            if (message.contains("CRTLL_USER_PLATE"))
                return duplicateException("user.plate.duplicate");
        }

        return handleCoreServiceException(new CarTollException("unknown.error", HttpStatus.INTERNAL_SERVER_ERROR));
    }

    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<GeneralErrorResponse> handleHttpClientErrorException(
            HttpClientErrorException ex) throws JsonProcessingException {

        HttpClientErrorDto httpClientErrorDto = new ObjectMapper().readValue(ex.getResponseBodyAsString(), HttpClientErrorDto.class);

        List<GeneralErrorResponse.SubError> subErrorList = new ArrayList<>();
        httpClientErrorDto.getErrors().forEach((error) -> {
            GeneralErrorResponse.SubError subError = new GeneralErrorResponse.SubError();
            subError.setCode("E" + ex.getStatusCode().value() + "CRTLL");
            subError.setTimestamp(new Date().getTime());
            subError.setLocalizedMessage(error.getField() + " " + error.getDefaultMessage());
            subErrorList.add(subError);
        });

        GeneralErrorResponse generalErrorResponse = new GeneralErrorResponse();
        generalErrorResponse
                .setStatus(ex.getStatusCode())
                .setTimestamp(new Date().getTime())
                .setCode("E" + ex.getStatusCode().value() + "CRTLL")
                .setLocalizedMessage(messageSource.getMessage("method.argument.not.valid", null, new Locale("fa")))
                .setSubErrors(subErrorList);
        return new ResponseEntity<>(generalErrorResponse, ex.getStatusCode());

    }

    private ResponseEntity<GeneralErrorResponse> duplicateException(String localizedMessage) {

        GeneralErrorResponse globalErrorResponse = new GeneralErrorResponse();
        globalErrorResponse
                .setStatus(HttpStatus.BAD_REQUEST)
                .setTimestamp(new Date().getTime())
                .setCode("E" + HttpStatus.BAD_REQUEST.value() + "CRTLL")
                .setMessage(messageSource.getMessage(localizedMessage, null, new Locale("fa")))
                .setLocalizedMessage(messageSource.getMessage(localizedMessage, null, new Locale("fa")))
//                .setSubErrors(Collections.singletonList(subError));
                .setSubErrors(null);
        return new ResponseEntity<>(globalErrorResponse, HttpStatus.BAD_REQUEST);
    }

}