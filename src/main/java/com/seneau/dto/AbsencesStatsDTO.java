package com.seneau.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AbsencesStatsDTO {
    private Integer nbrAbsencesPris=0;
    private List<Object> nbrAbsencesDirection;
    private Integer nbrJoursAbsencesPris=0;
    private Integer nbrAbsencesCurrentMonth=0;

    public AbsencesStatsDTO(Long nbrAbsencesPris, Long nbrJoursAbsencesPris){
        this.nbrAbsencesPris= nbrAbsencesPris.intValue();
        if(nbrJoursAbsencesPris!=null){
            this.nbrJoursAbsencesPris = nbrJoursAbsencesPris.intValue();
        }
    }
}
