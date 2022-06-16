package com.seneau.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EmailDto {
    private Integer step;
    private Long matriculeAgent;
    private Long matriculeAgentReponsable;
    private String emailSender;
    private String type;
    private String status;
    private String pivot;

  /*  public EmailDto(Long matriculeAgent, Long matriculeAgentReponsable,String emailSender, String type, String status, String pivot){
        this.matriculeAgent = matriculeAgent;
        this.matriculeAgentReponsable= matriculeAgentReponsable;
        this.emailSender = emailSender;
        this.type = type;
        this.status = status;
        this.pivot = pivot;
    }*/
}
