package de.dhbw.todoverwaltung.web;

import java.time.LocalDate;
import java.util.List;

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

/**
 * REST-Controller mit den CRUD-Endpunkten für Aufgaben
 * Wird vomJavaScript-Code der statischen HTML-Seiten aufgerufen
 */
@RestController
@RequestMapping("/api/aufgaben")
public class AufgabeRestController {

    private final AufgabeRepository aufgabeRepository;


    public AufgabeRestController(AufgabeRepository aufgabeRepository) {
        this.aufgabeRepository = aufgabeRepository;
    }

   
    @GetMapping
    public List<AufgabeDto> holeAlleAufgaben() {
        return aufgabeRepository.findAllByOrderByErstelldatumDesc()
                .stream()
                .map(DtoMapper::zuDto)
                .toList();
    }

    
    @GetMapping("/{id}")
    public AufgabeDto holeAufgabe(@PathVariable Long id) {
        return DtoMapper.zuDto(ladeAufgabeOderWirfException(id));
    }

    
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AufgabeDto erzeugeAufgabe(@RequestBody AufgabeEingabeDto eingabe) {
        pruefeEingabe(eingabe);
        AufgabeEntity neueAufgabe = new AufgabeEntity(eingabe.name().trim(),
                textOderNull(eingabe.beschreibung()),
                textOderNull(eingabe.kategorie()),
                LocalDate.now());
        return DtoMapper.zuDto(aufgabeRepository.save(neueAufgabe));
    }

    
    @PutMapping("/{id}")
    public AufgabeDto aktualisiereAufgabe(@PathVariable Long id, @RequestBody AufgabeEingabeDto eingabe) {
        pruefeEingabe(eingabe);
        AufgabeEntity aufgabe = ladeAufgabeOderWirfException(id);
        aufgabe.setName(eingabe.name().trim());
        aufgabe.setBeschreibung(textOderNull(eingabe.beschreibung()));
        aufgabe.setKategorie(textOderNull(eingabe.kategorie()));
        return DtoMapper.zuDto(aufgabeRepository.save(aufgabe));
    }

    
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void loescheAufgabe(@PathVariable Long id) {
        AufgabeEntity aufgabe = ladeAufgabeOderWirfException(id);
        aufgabeRepository.delete(aufgabe);
    }

  
    private AufgabeEntity ladeAufgabeOderWirfException(Long id) {
        return aufgabeRepository.findById(id)
                .orElseThrow(() -> new NichtGefundenException("Aufgabe mit ID " + id + " wurde nicht gefunden."));
    }

    
    private void pruefeEingabe(AufgabeEingabeDto eingabe) {
        if (eingabe == null || eingabe.name() == null || eingabe.name().isBlank()) {
            throw new UngueltigeEingabeException("Der Name der Aufgabe darf nicht leer sein.");
        }
        if (eingabe.name().trim().length() > 255) {
            throw new UngueltigeEingabeException("Der Name der Aufgabe darf höchstens 255 Zeichen lang sein.");
        }
    }

    
    private String textOderNull(String text) {
        if (text == null || text.isBlank()) {
            return null;
        }
        return text.trim();
    }
}
