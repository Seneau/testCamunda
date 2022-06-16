package com.seneau.dto;

import com.seneau.domain.Agent;
import com.seneau.domain.Conge;
import lombok.Data;

import java.util.List;

@Data
public class CongeAgentStepDTO {

    private Agent agent;
    private List<Conge> conges;
}
