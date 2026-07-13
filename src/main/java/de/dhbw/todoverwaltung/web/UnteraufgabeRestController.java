package de.dhbw.todoverwaltung.web;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import de.dhbw.todoverwaltung.db.AufgabeEntity;
import de.dhbw.todoverwaltung.db.AufgabeRepository;
import de.dhbw.todoverwaltung.db.Prioritaet;
import de.dhbw.todoverwaltung.db.Status;
import de.dhbw.todoverwaltung.db.UnteraufgabeEntity;
import de.dhbw.todoverwaltung.db.UnteraufgabeRepository;

// REST-Controller für die CRUD-Endpunkten der Unteraufgaben
@RestController
@RequestMapping("/api")
public class UnteraufgabeRestController {

    private static final Logger LOG = LoggerFactory.getLogger(UnteraufgabeRestController.class);

    private final UnteraufgabeRepository unteraufgabeRepository;

    private final AufgabeRepository aufgabeRepository;

    // Konstruktor für die DI der Repositories
    public UnteraufgabeRestController(UnteraufgabeRepository unteraufgabeRepository,
                                      AufgabeRepository aufgabeRepository) {
        this.unteraufgabeRepository = unteraufgabeRepository;
        this.aufgabeRepository = aufgabeRepository;
    }

 
    @GetMapping("/aufgaben/{aufgabeId}/unteraufgaben")
    public List<UnteraufgabeDto> holeUnteraufgaben(@PathVariable Long aufgabeId) {
        ladeAufgabeOderWirfException(aufgabeId);
        return unteraufgabeRepository.findByAufgabeAufgabeIdOrderByFaelligkeitAsc(aufgabeId)
                .stream()
                .map(DtoMapper::zuDto)
                .toList();
    }

    @PostMapping("/aufgaben/{aufgabeId}/unteraufgaben")
    @ResponseStatus(HttpStatus.CREATED)
    public UnteraufgabeDto erzeugeUnteraufgabe(@PathVariable Long aufgabeId,
                                               @RequestBody UnteraufgabeEingabeDto eingabe) {
        AufgabeEntity aufgabe = ladeAufgabeOderWirfException(aufgabeId);
        pruefeFaelligkeit(eingabe);
        Prioritaet prioritaet = parsePrioritaet(eingabe.prioritaet());
        Status status = parseStatus(eingabe.status());
        UnteraufgabeEntity neueUnteraufgabe =
                new UnteraufgabeEntity(prioritaet, status, eingabe.faelligkeit(), aufgabe);
        aktualisiereErledigtAm(neueUnteraufgabe, status);
        return DtoMapper.zuDto(unteraufgabeRepository.save(neueUnteraufgabe));
    }

    @PutMapping("/unteraufgaben/{todoId}")
    public UnteraufgabeDto aktualisiereUnteraufgabe(@PathVariable Long todoId,
                                                    @RequestBody UnteraufgabeEingabeDto eingabe) {
        UnteraufgabeEntity unteraufgabe = ladeUnteraufgabeOderWirfException(todoId);
        pruefeFaelligkeit(eingabe);
        Status neuerStatus = parseStatus(eingabe.status());
        unteraufgabe.setPrioritaet(parsePrioritaet(eingabe.prioritaet()));
        unteraufgabe.setStatus(neuerStatus);
        unteraufgabe.setFaelligkeit(eingabe.faelligkeit());
        aktualisiereErledigtAm(unteraufgabe, neuerStatus);
        return DtoMapper.zuDto(unteraufgabeRepository.save(unteraufgabe));
    }

  
    @DeleteMapping("/unteraufgaben/{todoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void loescheUnteraufgabe(@PathVariable Long todoId) {
        UnteraufgabeEntity unteraufgabe = ladeUnteraufgabeOderWirfException(todoId);
        unteraufgabeRepository.delete(unteraufgabe);
    }

    private AufgabeEntity ladeAufgabeOderWirfException(Long aufgabeId) {
        return aufgabeRepository.findById(aufgabeId)
                .orElseThrow(() -> new NichtGefundenException(
                        "Aufgabe mit ID " + aufgabeId + " wurde nicht gefunden."));
    }


    private UnteraufgabeEntity ladeUnteraufgabeOderWirfException(Long todoId) {
        return unteraufgabeRepository.findById(todoId)
                .orElseThrow(() -> new NichtGefundenException(
                        "Unteraufgabe mit ID " + todoId + " wurde nicht gefunden."));
    }

    private void pruefeFaelligkeit(UnteraufgabeEingabeDto eingabe) {
        if (eingabe == null || eingabe.faelligkeit() == null) {
            throw new UngueltigeEingabeException("Bitte ein Fälligkeitsdatum angeben.");
        }
    }


    private Prioritaet parsePrioritaet(String wert) {
        if (wert == null || wert.isBlank()) {
            throw new UngueltigeEingabeException("Bitte eine Priorität angeben (NIEDRIG, MITTEL oder HOCH).");
        }
        try {
            return Prioritaet.valueOf(wert.trim().toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException exception) {
            LOG.warn("Ungültige Priorität im Request: '{}'", wert);
            throw new UngueltigeEingabeException(
                    "Ungültige Priorität \"" + wert + "\" (erlaubt: NIEDRIG, MITTEL, HOCH).");
        }
    }

    private Status parseStatus(String wert) {
        if (wert == null || wert.isBlank()) {
            return Status.OFFEN;
        }
        try {
            return Status.valueOf(wert.trim().toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException exception) {
            LOG.warn("Ungültiger Status im Request: '{}'", wert);
            throw new UngueltigeEingabeException(
                    "Ungültiger Status \"" + wert + "\" (erlaubt: OFFEN, IN_ARBEIT, ERLEDIGT).");
        }
    }

    private void aktualisiereErledigtAm(UnteraufgabeEntity unteraufgabe, Status status) {
        if (status == Status.ERLEDIGT) {
            if (unteraufgabe.getErledigtAm() == null) {
                unteraufgabe.setErledigtAm(LocalDate.now());
            }
        } else {
            unteraufgabe.setErledigtAm(null);
        }
    }
}
