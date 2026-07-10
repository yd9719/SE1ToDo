package de.dhbw.todoverwaltung.web;

// Separate Klasse für ungültige Nutzereingabe-Exceptions 
public class UngueltigeEingabeException extends RuntimeException {

   
    public UngueltigeEingabeException(String nachricht) {
        super(nachricht);
    }
}
