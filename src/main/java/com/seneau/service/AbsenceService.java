package com.seneau.service;

import com.seneau.agileo.AgileoService;
import com.seneau.agileo.dtos.WebhookDTO;
import com.seneau.domain.Absence;
import com.seneau.domain.Agent;
import com.seneau.domain.Conge;
import com.seneau.dto.AbsenceDto;
import com.seneau.dto.AbsencesStatsDTO;
import com.seneau.repository.AbsenceRepository;
import com.seneau.repository.RecuperationRepository;
import org.keycloak.adapters.springsecurity.client.KeycloakRestTemplate;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AbsenceService {
    final
    AbsenceRepository absenceRepository;

    private final AgileoService agileoService;

    final
    KeycloakRestTemplate restTemplate;

    final
    ModelMapper modelMapper;

    @Value("${agent.baseurl}")
    private String baseUrl;

    public AbsenceService(AbsenceRepository absenceRepository, AgileoService agileoService, KeycloakRestTemplate restTemplate, ModelMapper modelMapper) {
        this.absenceRepository = absenceRepository;
        this.agileoService = agileoService;
        this.restTemplate = restTemplate;
        this.modelMapper = modelMapper;
    }

    public List<AbsenceDto> getAbsenceByEntity(int matricule, Optional<Integer> step, String type) {

        List<AbsenceDto> absenceDTOsWithoutEmptyUser=new ArrayList<>();
        ResponseEntity<Agent[]> response = restTemplate.getForEntity(baseUrl+"equipe/"+type+"/" + matricule , Agent[].class);

        List<Agent> agents =  Arrays.asList(response.getBody());
        List<Long> matriculesAgents =   agents.stream().map(Agent::getMatricule).collect(Collectors.toList());
        if(matriculesAgents.isEmpty()){
            return Collections.EMPTY_LIST;
        }
        List<AbsenceDto> listAbs= (absenceRepository.findAbsencesByYear(matriculesAgents, step)).stream().map(this::convertToDto).collect(Collectors.toList());
        listAbs.forEach( abs -> {
            Agent agent = findAgentByMatricule(agents, Long.valueOf(abs.getMatricule()));
            abs.setAgent(agent);
            if(agent!=null){
                absenceDTOsWithoutEmptyUser.add(abs);
            }
        });
        return absenceDTOsWithoutEmptyUser;
    }

    private Agent findAgentByMatricule(List<Agent> agents, Long idAgent){
        Optional<Agent> optAgent = agents.stream().filter(agent -> agent.getMatricule().equals(idAgent)).findFirst();

        return optAgent.isPresent()? optAgent.get() : null;
    }

    private AbsenceDto convertToDto(Absence Absence) {
        AbsenceDto AbsenceDto = modelMapper.map(Absence, AbsenceDto.class);
        return AbsenceDto;
    }

    public List<AbsenceDto> getAbsenceByYear(String an) {

        List<AbsenceDto> absenceDTOsWithoutEmptyUser=new ArrayList<>();
        List<Absence> listAbsences = absenceRepository.findAll();
        List<Long> matriculesAgents =   listAbsences.stream().map(Absence::getMatricule).collect(Collectors.toList());
        if(matriculesAgents.isEmpty()){
            return Collections.EMPTY_LIST;
        }
        String listStr= matriculesAgents.toString();
        listStr= listStr.replace('[',' ').replace(']', ' ');

        ResponseEntity<Agent[]> response = restTemplate.getForEntity
                (baseUrl + "listAgentmatricules/"+listStr, Agent[].class);

        List<Agent> agents =  Arrays.asList(response.getBody());

        List<AbsenceDto> absencesDoneDTO = (listAbsences).stream().map(this::convertToDto).collect(Collectors.toList());
        absencesDoneDTO.forEach( abs -> {
            Agent agent = findAgentByMatricule(agents, Long.valueOf(abs.getMatricule()));
            abs.setAgent(agent);

            if(agent!=null){
                absenceDTOsWithoutEmptyUser.add(abs);
            }
        });

        return absenceDTOsWithoutEmptyUser;
    }

    public AbsencesStatsDTO getStatsAbsence() {
        AbsencesStatsDTO absencesStatsDTO = absenceRepository.findNbrAbsences();
        absencesStatsDTO.setNbrAbsencesDirection(getAbsenceDirection());
        absencesStatsDTO.setNbrAbsencesCurrentMonth(((Long) absenceRepository.findCountAbsenceCurrentMois()).intValue());
        return absencesStatsDTO;
    }

    private List<Object> getAbsenceDirection(){
        List<Absence> absences = absenceRepository.findAbsencesYear();

        if(absences.isEmpty()){
            return    new ArrayList<>();

        }
        List<Long> matriculesAgents =   absences.stream().map(Absence::getMatricule).collect(Collectors.toList());
        if(matriculesAgents.isEmpty()){
            return Collections.EMPTY_LIST;
        }
        String listStr= matriculesAgents.toString();
        listStr= listStr.replace('[',' ').replace(']', ' ');

        ResponseEntity<Object[]> response = restTemplate.getForEntity
                (baseUrl+"listmatricules/"+listStr, Object[].class);

        return  Arrays.asList(response.getBody());



    }

    public ResponseEntity<Absence> updateAbsence(Absence absence) {

        if (absence.getId() == null)
            return ResponseEntity.notFound().build();
        Absence result = absenceRepository.save(absence);

        //if(absence.getTracker().getStep()==2 || absence.getTracker().getStep()==3 ){
          //  agileoService.sendWebhook(new WebhookDTO(absence));
        //}
        return ResponseEntity.ok(result);
    }
}
