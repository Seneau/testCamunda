package com.seneau.dto;

import com.seneau.domain.CongeData;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CongeDataExtractDTO {

    private Long matricule;
    private String jrDateDepart;
    private String moisDateDepart;
    private String anDateDepart;
    private Long nbrJoursConge;
    private String jrDateRetour;
    private String moisDateRetour;
    private String anDateRetour;


    public static CongeData toEntity(CongeDataExtractDTO dto){
        CongeData congeData = new CongeData();

        congeData.setMatricule(dto.matricule);
        congeData.setNbrJour(dto.nbrJoursConge);
        LocalDate dateDepart= LocalDate.parse(dto.anDateDepart+"-"+dto.moisDateDepart+"-"+dto.jrDateDepart);
        LocalDate dateRetour= LocalDate.parse(dto.anDateRetour+"-"+dto.moisDateRetour+"-"+dto.jrDateRetour);
        congeData.setDateRetour(dateRetour);
        congeData.setDateDepart(dateDepart);

        return congeData;
    }

}
