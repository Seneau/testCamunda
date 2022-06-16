package com.seneau.dto;

import lombok.Data;

@Data
public class SendEmailDto {
    String to;
    String type;
    String subject;
    String sender;
    String core;
}
