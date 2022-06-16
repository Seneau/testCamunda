package com.seneau.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class CongesStatsDTO {
    private Integer nbrCongesPris;
    private List<Object> nbrCongesDirection;
    private Integer nbrJoursCongesPris=0;
    private Integer nbrCongesCurrentMonth=0;

    public CongesStatsDTO(Long nbrCongesPris, Long nbrJoursCongesPris){
        this.nbrCongesPris = nbrCongesPris.intValue();
        if(nbrJoursCongesPris!=null){
            System.out.println("nbre"+nbrJoursCongesPris);
        this.nbrJoursCongesPris = nbrJoursCongesPris.intValue();}
    }
}
