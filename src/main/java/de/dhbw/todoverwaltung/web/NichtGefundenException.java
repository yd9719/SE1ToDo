package de.dhbw.todoverwaltung.web;


 // Separate Klasse für Datensatz-nicht-gefunden Exceptions 

public class NichtGefundenException extends RuntimeException {


    public NichtGefundenException(String nachricht) {
        super(nachricht);
    }
}
