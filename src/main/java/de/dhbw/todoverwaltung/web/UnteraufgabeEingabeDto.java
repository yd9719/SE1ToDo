package de.dhbw.todoverwaltung.web;

import java.time.LocalDate;

// Datentransportklasse für das Bearbeiten oder Erstellen einer Unteraufgabe
public record UnteraufgabeEingabeDto(String prioritaet, String status, LocalDate faelligkeit) {
}
