package com.seneau.service;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.seneau.domain.Conge;
import com.seneau.domain.Rappel;
import com.seneau.dto.EmailDto;
import com.seneau.dto.EmailRappelDto;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.adapters.springsecurity.client.KeycloakRestTemplate;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.keycloak.representations.AccessToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Service
public class EmailService {
    @Autowired
    private HttpServletRequest request;

    @Autowired
    private KeycloakRestTemplate keycloakRestTemplate;

    @Value("${agent.baseurl}")
    private String baseUrl;

    public Object mailTreatment(Conge conge, String pivot){
        String type = "conge";
        String status = conge.getTracker().getLibelle();
        Long matriculeAgent = conge.getMatriculeCreator();
        Long matriculeAgentReponsable = conge.getMatriculeCreator();

        EmailDto emailDto=new EmailDto(conge.getTracker().getStep(), matriculeAgent,
                matriculeAgentReponsable,
                "no-reply@seneau.sn",
                type,
                status,
                pivot);

       Object response =
               keycloakRestTemplate.postForEntity(baseUrl + "email", emailDto, Object.class);


        return response;
    }

    public Object mailRappel(Rappel rappel){
        String emailSender = "no-reply@seneau.sn";
       /* KeycloakAuthenticationToken token = (KeycloakAuthenticationToken) request.getUserPrincipal();
        KeycloakPrincipal principal=(KeycloakPrincipal)token.getPrincipal();
        KeycloakSecurityContext session = principal.getKeycloakSecurityContext();
        AccessToken accessToken = session.getToken();
        emailSender = accessToken.getEmail();*/

        return keycloakRestTemplate.postForEntity(
                baseUrl + "email/rappel",
                new EmailRappelDto(rappel.getMatricule(),
                        emailSender,
                        rappel.getDateRetour(),
                        rappel.getRappelePour(),
                        rappel.getMotif()),
                Object.class);
       // return null;
    }
}
