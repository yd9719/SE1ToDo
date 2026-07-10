package de.dhbw.todoverwaltung.db;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

/**
 * Entity-Klasse für die Aufgaben-Tabelle
 * Eine Aufgabe kann aus beliebig vielen Unteraufgaben bestehen (1:n-Beziehung)
 */
@Entity
@Table(name = "aufgabe")
public class AufgabeEntity {

    /** PK, wird von der Datenbank  vergeben. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "aufgabe_id")
    private Long aufgabeId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "beschreibung", length = 2000)
    private String beschreibung;

    @Column(name = "kategorie")
    private String kategorie;

    @Column(name = "erstelldatum", nullable = false)
    private LocalDate erstelldatum;

    // Unteraufgaben sollen beim Löschen der Aufgabe auch entfernt werden
    @OneToMany(mappedBy = "aufgabe", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UnteraufgabeEntity> unteraufgaben = new ArrayList<>();


    public AufgabeEntity() {
    }

    public AufgabeEntity(String name, String beschreibung, String kategorie, LocalDate erstelldatum) {
        this.name = name;
        this.beschreibung = beschreibung;
        this.kategorie = kategorie;
        this.erstelldatum = erstelldatum;
    }

    public Long getAufgabeId() {
        return aufgabeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBeschreibung() {
        return beschreibung;
    }

    public void setBeschreibung(String beschreibung) {
        this.beschreibung = beschreibung;
    }

    public String getKategorie() {
        return kategorie;
    }

    public void setKategorie(String kategorie) {
        this.kategorie = kategorie;
    }

    public LocalDate getErstelldatum() {
        return erstelldatum;
    }

    public void setErstelldatum(LocalDate erstelldatum) {
        this.erstelldatum = erstelldatum;
    }

    public List<UnteraufgabeEntity> getUnteraufgaben() {
        return unteraufgaben;
    }

    
     //wei Aufgaben sind gleich, wenn ihre Attribute gleich sind
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof AufgabeEntity andereAufgabe)) {
            return false;
        }
        return Objects.equals(name, andereAufgabe.name)
                && Objects.equals(beschreibung, andereAufgabe.beschreibung)
                && Objects.equals(kategorie, andereAufgabe.kategorie)
                && Objects.equals(erstelldatum, andereAufgabe.erstelldatum);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, beschreibung, kategorie, erstelldatum);
    }

    @Override
    public String toString() {
        return "Aufgabe \"" + name + "\" (Kategorie: " + kategorie + ", erstellt am: " + erstelldatum + ")";
    }
}
