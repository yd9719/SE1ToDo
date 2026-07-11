package de.dhbw.todoverwaltung.web;

import de.dhbw.todoverwaltung.db.AufgabeEntity;
import de.dhbw.todoverwaltung.db.UnteraufgabeEntity;


//  Hilfsklasse um die Entity-Objekte auf die DTOs zu mappen 
 
public final class DtoMapper {


    private DtoMapper() {
    }


    public static AufgabeDto zuDto(AufgabeEntity entity) {
        return new AufgabeDto(entity.getAufgabeId(),
                entity.getName(),
                entity.getBeschreibung(),
                entity.getKategorie(),
                entity.getErstelldatum(),
                entity.getUnteraufgaben().size());
    }

     public static UnteraufgabeDto zuDto(UnteraufgabeEntity entity) {
        return new UnteraufgabeDto(entity.getTodoId(),
                entity.getAufgabe().getAufgabeId(),
                entity.getPrioritaet().name(),
                entity.getStatus().name(),
                entity.getFaelligkeit(),
                entity.getErledigtAm());
    }
}
