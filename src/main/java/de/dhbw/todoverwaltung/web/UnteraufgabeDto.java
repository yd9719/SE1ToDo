package de.dhbw.todoverwaltung.web;

import java.time.LocalDate;

// Datentransportklasse die eine Unteraufgabe als JSON an das Frontend sendet 
public record UnteraufgabeDto(Long todoId,
                              Long aufgabeId,
                              String prioritaet,
                              String status,
                              LocalDate faelligkeit,
                              LocalDate erledigtAm) {
}
