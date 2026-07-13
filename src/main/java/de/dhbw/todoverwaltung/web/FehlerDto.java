package de.dhbw.todoverwaltung.web;

/**
 * Datentransportklasse (Record) für Fehlermeldungen, die als JSON an das
 * Frontend geschickt und dort dem Nutzer angezeigt werden.
 *
 * @param nachricht Fehlermeldung in deutscher Sprache
 */
public record FehlerDto(String nachricht) {
}
