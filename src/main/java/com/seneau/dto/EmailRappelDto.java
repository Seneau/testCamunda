package com.seneau.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class EmailRappelDto {
    private int matriculeAgent;

    private String emailSender;

    private LocalDate dateRetour;

    private Integer rappelePour;

    private String motif;


}
