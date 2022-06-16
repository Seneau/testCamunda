package com.seneau.agileo.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.seneau.domain.Absence;
import com.seneau.domain.Conge;
import com.seneau.domain.Recuperation;
import com.seneau.helpers.Constant;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class CongeDTO {

    @JsonProperty("ID")
    private Long id;

    @JsonProperty("MATRICULE")
    private String matricule;

    @JsonProperty("DATE_DEBUT")
    private LocalDate dateDebut;

    @JsonProperty("DATE_FIN")
    private LocalDate dateFin;

    @JsonProperty("ID_EXT")
    private String idExt;

    @JsonProperty("STATUS")
    private String status;

    public CongeDTO( Conge conge) {
        this.id = conge.getId();
        this.matricule = String.valueOf(conge.getMatricule());
        this.dateDebut = conge.getDateConge();
        this.dateFin = conge.getDateRetourProvisoire();
        this.idExt = Constant.CONGE;
        this.status = "status";
    }

    public CongeDTO( Absence absence) {
        this.id = absence.getId();
        this.matricule= String.valueOf(absence.getMatricule());
        this.dateDebut= absence.getDateAbsence();
        this.dateFin= absence.getDateRetourAbsence();
        this.idExt= Constant.ABSENCE;
        this.status= "status";
    }

    public CongeDTO( Recuperation rec) {
        this.id = rec.getId();
        this.matricule = String.valueOf(rec.getMatricule());
        this.dateDebut = rec.getDateRecup();
        this.dateFin = rec.getDateRetourRecup();
        this.idExt = Constant.RECUPERATION;
        this.status = "status";
    }
}
