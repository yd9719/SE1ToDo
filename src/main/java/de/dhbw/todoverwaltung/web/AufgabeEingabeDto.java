package de.dhbw.todoverwaltung.web;

/**
 * Datentransportklasse für die Eingaben beim Anlegen oder
 * Bearbeiten einer Aufgabe
 * as Erstelldatum wird nicht vom Client sondern vom Backend gesetzt.
 */
public record AufgabeEingabeDto(String name, String beschreibung, String kategorie) {
}
