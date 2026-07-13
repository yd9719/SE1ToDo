package de.dhbw.todoverwaltung;

import java.time.LocalDate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import de.dhbw.todoverwaltung.db.AufgabeEntity;
import de.dhbw.todoverwaltung.db.AufgabeRepository;
import de.dhbw.todoverwaltung.db.Prioritaet;
import de.dhbw.todoverwaltung.db.Status;
import de.dhbw.todoverwaltung.db.UnteraufgabeEntity;
import de.dhbw.todoverwaltung.db.UnteraufgabeRepository;

/**
 *Beim ersten Start sollen Beispieldaten in die Datenbank importiert werden 
 Befinden sich bereits daten in den Datenbank bzw. Tabelle, wird dieser Import übersprungen 
 */
@Component
public class BeispielDatenImporter implements CommandLineRunner {

    private static final Logger LOG = LoggerFactory.getLogger(BeispielDatenImporter.class);

    private final AufgabeRepository aufgabeRepository;

    private final UnteraufgabeRepository unteraufgabeRepository;


    public BeispielDatenImporter(AufgabeRepository aufgabeRepository,
                                 UnteraufgabeRepository unteraufgabeRepository) {
        this.aufgabeRepository = aufgabeRepository;
        this.unteraufgabeRepository = unteraufgabeRepository;
    }


    @Override
    public void run(String... args) {
        long anzahlAufgaben = aufgabeRepository.count();
        if (anzahlAufgaben > 0) {
            LOG.info("Datenbank enthält bereits {} Aufgabe(n), Beispieldaten werden nicht importiert.",
                    anzahlAufgaben);
            return;
        }

        AufgabeEntity einkauf = legeAufgabeAn("Wocheneinkauf",
                "Einkäufe für die kommende Woche erledigen.", "Privat");
        legeUnteraufgabeAn(einkauf, Prioritaet.MITTEL, Status.OFFEN, LocalDate.now().plusDays(2));
        legeUnteraufgabeAn(einkauf, Prioritaet.NIEDRIG, Status.OFFEN, LocalDate.now().plusDays(5));

        AufgabeEntity assignment = legeAufgabeAn("Assignment Software Engineering",
                "Web-Anwendung mit Spring Boot und H2 umsetzen.", "Studium");
        legeUnteraufgabeAn(assignment, Prioritaet.HOCH, Status.IN_ARBEIT, LocalDate.now().plusDays(7));
        legeUnteraufgabeAn(assignment, Prioritaet.HOCH, Status.OFFEN, LocalDate.now().plusDays(14));
        legeUnteraufgabeAn(assignment, Prioritaet.MITTEL, Status.OFFEN, LocalDate.now().plusDays(21));

        LOG.info("Beispieldaten importiert: {} Aufgaben, {} Unteraufgaben.",
                aufgabeRepository.count(), unteraufgabeRepository.count());
    }

   // Anlegen einer Beispielausgabe 
    private AufgabeEntity legeAufgabeAn(String name, String beschreibung, String kategorie) {
        AufgabeEntity aufgabe = new AufgabeEntity(name, beschreibung, kategorie, LocalDate.now());
        return aufgabeRepository.save(aufgabe);
    }

    // Anlegen einer Beispiel-Unteraufgabe 
    private void legeUnteraufgabeAn(AufgabeEntity aufgabe, Prioritaet prioritaet, Status status,
                                    LocalDate faelligkeit) {
        UnteraufgabeEntity unteraufgabe = new UnteraufgabeEntity(prioritaet, status, faelligkeit, aufgabe);
        unteraufgabeRepository.save(unteraufgabe);
    }
}
