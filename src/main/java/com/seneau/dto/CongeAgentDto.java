package com.seneau.dto;

import com.seneau.domain.*;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CongeAgentDto {

    private Long id;

    private Long matricule;

    private LocalDate dateConge;

    private LocalDate dateDepartConge;

    private LocalDate dateRetourProvisoire;

    private Long  nombreJourConge ;

    private Long  totalConge ;

    private Integer mois;

    private Integer annee;

    private Long  nombreJourRecup ;

    private LocalDate dateRetourEffective;

    private LocalDate dateRetourHR;

    private Tracker tracker;
    private Agent agent;
    private Rappel rappel;

    public static CongeAgentDto mergingWithConge(Conge conge) {
        final CongeAgentDto conbinationWithConge = new CongeAgentDto();
        conbinationWithConge.setId(conge.getId());
        conbinationWithConge.setMatricule(conge.getMatricule());
        conbinationWithConge.setDateConge(conge.getDateConge());
        conbinationWithConge.setNombreJourConge(conge.getNombreJourConge());
        conbinationWithConge.setDateRetourEffective(conge.getDateRetourEffective());
        conbinationWithConge.setTracker(conge.getTracker());
        conbinationWithConge.setRappel(conge.getRappel());

        return conbinationWithConge;
    }

    public static CongeAgentDto mergingWithCongeData(CongeData congeData) {

        final CongeAgentDto conbinationWithConge = new CongeAgentDto();
        conbinationWithConge.setTotalConge(congeData.getNbrJour());
        conbinationWithConge.setDateRetourHR(congeData.getDateRetour());
        return conbinationWithConge;
    }

}
