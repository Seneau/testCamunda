package com.seneau.service;

import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.keycloak.representations.AccessToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Service
public class AuthService {

    @Autowired
    private HttpServletRequest request;

    public String getCurrentUserMatricule(){
        String matricule="";

        Map<String, Object> attributes= getAccessToken().getOtherClaims();
        if (attributes.containsKey("matricule")) {
            matricule = String.valueOf(attributes.get("matricule"));
        }
       return matricule;
    }

    private AccessToken getAccessToken(){
        KeycloakAuthenticationToken token = (KeycloakAuthenticationToken) request.getUserPrincipal();
        KeycloakPrincipal principal=(KeycloakPrincipal)token.getPrincipal();
        KeycloakSecurityContext session = principal.getKeycloakSecurityContext();

        return session.getToken();
    }

    public String getCurrentUserEmail(){
        return getAccessToken().getEmail();
    }
}
