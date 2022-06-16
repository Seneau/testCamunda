package com.seneau.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.seneau.domain.Agent;
import com.seneau.domain.Tracker;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
public class RappelDto {

    private Long id;

    private Integer matricule;

    private LocalDate dateRappel;

    private LocalDate dateRetour;

    private Integer rappelePour;

    private String motif;

    private Tracker tracker;
    private Agent agent;
}
