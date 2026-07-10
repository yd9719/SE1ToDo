package de.dhbw.todoverwaltung.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Klasse zur Behandlung der Exceptions aus dem Controller
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(GlobalExceptionHandler.class);

 
    @ExceptionHandler(NichtGefundenException.class)
    public ResponseEntity<FehlerDto> behandleNichtGefunden(NichtGefundenException exception) {
        LOG.warn("Datensatz nicht gefunden: {}", exception.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new FehlerDto(exception.getMessage()));
    }


    @ExceptionHandler(UngueltigeEingabeException.class)
    public ResponseEntity<FehlerDto> behandleUngueltigeEingabe(UngueltigeEingabeException exception) {
        LOG.warn("Ungültige Eingabe: {}", exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new FehlerDto(exception.getMessage()));
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<FehlerDto> behandleAllgemeinenFehler(Exception exception) {
        LOG.error("Unerwarteter Fehler bei der Verarbeitung eines Requests.", exception);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new FehlerDto("Es ist ein unerwarteter Fehler aufgetreten."));
    }
}
