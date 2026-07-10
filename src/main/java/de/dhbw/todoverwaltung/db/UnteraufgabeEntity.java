package de.dhbw.todoverwaltung.db;

import java.time.LocalDate;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * Entity-Klasse für die unteraufgaben-Tabelle 
 * Jede Unteraufgabe wird über den Fremdschlüssel mit einer Aufgabe verbunden 
 */
@Entity
@Table(name = "unteraufgabe")
public class UnteraufgabeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "todo_id")
    private Long todoId;

    @Enumerated(EnumType.STRING)
    @Column(name = "prioritaet", nullable = false)
    private Prioritaet prioritaet;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

    @Column(name = "faelligkeit", nullable = false)
    private LocalDate faelligkeit;

    @Column(name = "erledigt_am")
    private LocalDate erledigtAm;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "aufgabe_id")
    private AufgabeEntity aufgabe;

    public UnteraufgabeEntity() {
    }

   
    public UnteraufgabeEntity(Prioritaet prioritaet, Status status, LocalDate faelligkeit, AufgabeEntity aufgabe) {
        this.prioritaet = prioritaet;
        this.status = status;
        this.faelligkeit = faelligkeit;
        this.aufgabe = aufgabe;
    }

    public Long getTodoId() {
        return todoId;
    }

    public Prioritaet getPrioritaet() {
        return prioritaet;
    }

    public void setPrioritaet(Prioritaet prioritaet) {
        this.prioritaet = prioritaet;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public LocalDate getFaelligkeit() {
        return faelligkeit;
    }

    public void setFaelligkeit(LocalDate faelligkeit) {
        this.faelligkeit = faelligkeit;
    }

    public LocalDate getErledigtAm() {
        return erledigtAm;
    }

    public void setErledigtAm(LocalDate erledigtAm) {
        this.erledigtAm = erledigtAm;
    }

    public AufgabeEntity getAufgabe() {
        return aufgabe;
    }

    public void setAufgabe(AufgabeEntity aufgabe) {
        this.aufgabe = aufgabe;
    }

    // Vergleich, ob zwei Unteraufgaben gleich sind 
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof UnteraufgabeEntity andereUnteraufgabe)) {
            return false;
        }
        return prioritaet == andereUnteraufgabe.prioritaet
                && status == andereUnteraufgabe.status
                && Objects.equals(faelligkeit, andereUnteraufgabe.faelligkeit)
                && Objects.equals(erledigtAm, andereUnteraufgabe.erledigtAm);
    }

    @Override
    public int hashCode() {
        return Objects.hash(prioritaet, status, faelligkeit, erledigtAm);
    }

    @Override
    public String toString() {
        return "Unteraufgabe (Priorität: " + prioritaet + ", Status: " + status
                + ", fällig am: " + faelligkeit + ")";
    }
}
