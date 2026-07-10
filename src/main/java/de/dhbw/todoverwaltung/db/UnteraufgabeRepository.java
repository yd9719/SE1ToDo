package de.dhbw.todoverwaltung.db;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

// Repository für den Zugriff auf die Unteraufgaben-Tabelle 
 
public interface UnteraufgabeRepository extends JpaRepository<UnteraufgabeEntity, Long> {

    // Die Unteraufgaben werden nach ihrer Fälligkeit sortiert
    List<UnteraufgabeEntity> findByAufgabeAufgabeIdOrderByFaelligkeitAsc(Long aufgabeId);
}
