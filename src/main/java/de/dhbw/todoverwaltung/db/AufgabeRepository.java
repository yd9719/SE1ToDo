package de.dhbw.todoverwaltung.db;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

// Das Repository für den Zugriff auf die Aufgaben-Tabelle

public interface AufgabeRepository extends JpaRepository<AufgabeEntity, Long> {

    //iefert alle Aufgaben
    // Es wird nach dem Erstelldatum sortiert 
    List<AufgabeEntity> findAllByOrderByErstelldatumDesc();
}
