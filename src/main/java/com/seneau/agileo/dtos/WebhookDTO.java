package com.seneau.agileo.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.seneau.domain.Absence;
import com.seneau.domain.Conge;
import com.seneau.domain.Recuperation;
import com.seneau.helpers.Constant;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class WebhookDTO {

    @JsonProperty("ID")
    private Long id;

    @JsonProperty("TYPE")
    private String type;

    public WebhookDTO(Conge conge) {
        this.id = conge.getId();
        this.type = Constant.CONGE;
    }

    public WebhookDTO(Absence absence) {
        this.id = absence.getId();
        this.type = Constant.ABSENCE;
    }

    public WebhookDTO(Recuperation recup) {
        this.id = recup.getId();
        this.type = Constant.RECUPERATION;
    }
}
