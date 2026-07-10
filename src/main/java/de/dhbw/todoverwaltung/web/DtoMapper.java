package de.dhbw.todoverwaltung.web;

import de.dhbw.todoverwaltung.db.AufgabeEntity;


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
}
