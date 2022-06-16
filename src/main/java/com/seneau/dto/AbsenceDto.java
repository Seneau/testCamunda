package com.seneau.dto;

import com.seneau.domain.Agent;
import com.seneau.domain.Tracker;
import lombok.Data;

import java.time.LocalDate;

@Data
public class AbsenceDto {

    private Long id;

    private Long matricule;

    private LocalDate dateAbsence;

    private LocalDate dateRetourAbsence;

    private Integer nbrJour;

    private String motif;

    private Boolean deductible;

    private Tracker tracker;

    private Agent agent;
}
