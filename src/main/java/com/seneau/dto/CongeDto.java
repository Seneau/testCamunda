package com.seneau.dto;

import com.seneau.domain.*;
import lombok.Data;


import java.time.LocalDate;

@Data
public class CongeDto {


    private Long id;

    private Long matricule;

    private LocalDate dateConge;

    private LocalDate dateDepartConge;

    private LocalDate dateRetourProvisoire;

    private Long  nombreJourConge ;

    private Long  totalConge ;

    private Long  nombreJourRecup ;

    private LocalDate dateRetourEffective;

    private LocalDate dateRetourHR;

    private Integer mois;

    private Integer annee;

    private Integer nbrJourReel;

    private Integer nbrJourAbsence;

    private boolean isCancelled;
    private Tracker tracker;
    private Agent agent;
    private Rappel rappel;

    public static CongeDto mergingWithConge(Conge conge) {
        final CongeDto conbinationWithConge = new CongeDto();
        conbinationWithConge.setId(conge.getId());
        conbinationWithConge.setMatricule(conge.getMatricule());
        conbinationWithConge.setDateConge(conge.getDateConge());
        conbinationWithConge.setNombreJourConge(conge.getNombreJourConge());
        conbinationWithConge.setDateRetourEffective(conge.getDateRetourEffective());
        conbinationWithConge.setTracker(conge.getTracker());
        conbinationWithConge.setRappel(conge.getRappel());

        return conbinationWithConge;
    }

    public static CongeDto mergingWithCongeData(CongeData congeData) {

        final CongeDto conbinationWithConge = new CongeDto();
        conbinationWithConge.setTotalConge(congeData.getNbrJour());
        conbinationWithConge.setDateRetourHR(congeData.getDateRetour());
        return conbinationWithConge;
    }

}
