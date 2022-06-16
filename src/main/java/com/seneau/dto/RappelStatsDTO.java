package com.seneau.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RappelStatsDTO {

    private Integer nbrRappelsPris;
    private Integer nbrRappelsCurrentMonth=0;

}
