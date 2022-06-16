package com.seneau.agileo;


import com.seneau.agileo.dtos.CongeDTO;
import com.seneau.agileo.dtos.WebhookDTO;
import com.seneau.domain.Agent;
import com.seneau.helpers.Constant;
import com.seneau.repository.AbsenceRepository;
import com.seneau.repository.CongeRepository;
import com.seneau.repository.RecuperationRepository;
import com.seneau.rest.AbsenceResource;
import org.keycloak.adapters.springsecurity.client.KeycloakRestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AgileoServiceImpl implements AgileoService{


    private final Logger log = LoggerFactory.getLogger(AbsenceResource.class);

    @Value("${agent.baseurl}")
    private String baseUrl;

    @Value("${agileo.webhookurl}")
    private String baseWebhook;

    private final RecuperationRepository recuperationRepository;

    private final AbsenceRepository absenceRepository;

    private final CongeRepository congeRepository;

    private final KeycloakRestTemplate restTemplate;

    public AgileoServiceImpl(CongeRepository congeRepository, KeycloakRestTemplate restTemplate, RecuperationRepository recuperationRepository, AbsenceRepository absenceRepository) {
        this.congeRepository = congeRepository;
        this.restTemplate = restTemplate;
        this.recuperationRepository = recuperationRepository;
        this.absenceRepository = absenceRepository;
    }


    @Override
    public List<CongeDTO> getAllConge() {

        ResponseEntity<Agent[]> response = restTemplate.getForEntity
                (baseUrl, Agent[].class);

        List<Agent> agents =  Arrays.asList(response.getBody());

        List<Long> matriculesAgents =   agents.stream().map(Agent::getMatricule).collect(Collectors.toList());
        if(matriculesAgents.isEmpty()){
            return Collections.EMPTY_LIST;
        }
        List<CongeDTO> congeDTOList = congeRepository.findCongeDtoByMatriculesIn(matriculesAgents);
        List<CongeDTO> absenceDtoList = absenceRepository.findCongeDtoByMatriculesIn(matriculesAgents);
        List<CongeDTO> recuperation = recuperationRepository.findCongeDtoByMatriculesIn(matriculesAgents);

        List<CongeDTO> total = new ArrayList<>(congeDTOList);

        total.addAll(absenceDtoList);

        total.addAll(recuperation);

        return  total;
    }

    @Override
    public CongeDTO getCongeByAgent(Long id, String type) {
        CongeDTO congeDTO;
        switch (type) {
            case Constant.CONGE : congeDTO = new CongeDTO(congeRepository.findById(id).orElse(null));
                break;
            case Constant.ABSENCE : congeDTO = new CongeDTO(absenceRepository.findById(id).orElse(null));
                break;
            case Constant.RECUPERATION : congeDTO = new CongeDTO(recuperationRepository.findById(id).orElse(null));
                break;
            default: congeDTO= new CongeDTO();
        }
        return congeDTO;
    }

    @Override
    public void sendWebhook(WebhookDTO webhookDTO) {
        restTemplate.postForEntity(URI.create(baseWebhook), webhookDTO, Object.class );
    }
}
