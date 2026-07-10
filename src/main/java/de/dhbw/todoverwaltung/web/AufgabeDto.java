package de.dhbw.todoverwaltung.web;

import java.time.LocalDate;

/**
 * Datentransportklasse
 * Schickt Aufgabe als JSON an das Frontend
 */
public record AufgabeDto(Long aufgabeId,
                         String name,
                         String beschreibung,
                         String kategorie,
                         LocalDate erstelldatum,
                         int anzahlUnteraufgaben) {
}
