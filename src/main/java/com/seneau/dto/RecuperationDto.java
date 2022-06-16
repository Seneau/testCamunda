package com.seneau.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.seneau.domain.Agent;
import com.seneau.domain.Tracker;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
public class RecuperationDto {

    private Long id;

    private Long matricule;

    private LocalDate dateRecup;

    private LocalDate dateRetourRecup;

    private Integer nbrJour;

    private String motif;

    private Tracker tracker;
    private Agent agent;
}
