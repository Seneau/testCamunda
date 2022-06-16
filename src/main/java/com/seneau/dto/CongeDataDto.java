package com.seneau.dto;

import com.seneau.domain.Agent;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CongeDataDto {
    private Long id;

    private Long matricule;

    private Long nbrJour;

    private Long nbrJourRecup;

    private Long nbrJourAbsence;

    private Long nbrJourAbsenceNonDeductible;

    private LocalDate dateRetour;

    private LocalDate dateDepartConge;

    private LocalDate dateRetourProvisoire;

    private Agent agent;

}
