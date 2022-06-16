package com.seneau.service;

import com.seneau.domain.Agent;
import com.seneau.domain.Rappel;
import com.seneau.dto.RappelDto;
import com.seneau.dto.RappelStatsDTO;
import com.seneau.helpers.DaterParser;
import com.seneau.repository.RappelRepository;
import org.keycloak.adapters.springsecurity.client.KeycloakRestTemplate;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class RappelService {

    final
    RappelRepository rappelRepository;

    final
    KeycloakRestTemplate restTemplate;

    final
    ModelMapper modelMapper;
    final
    DaterParser daterParser;

    @Value("${agent.baseurl}")
    private String baseUrl;

    public RappelService(DaterParser daterParser,
                         ModelMapper modelMapper,
                         KeycloakRestTemplate restTemplate,
                         RappelRepository rappelRepository) {
        this.daterParser = daterParser;
        this.modelMapper = modelMapper;
        this.restTemplate = restTemplate;
        this.rappelRepository = rappelRepository;
    }

    public List<RappelDto> getAllRappelsYear(String an) {
        LocalDate startDate, endDate;
        if(an != null){

            startDate = LocalDate.parse(an + "-01-01");
            endDate = LocalDate.parse(an + "-12-31");
        }
        else{

            Date current= new Date();
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(current);
            startDate = LocalDate.parse(calendar.get(Calendar.YEAR)+"-01-01");
            endDate = LocalDate.parse(calendar.get(Calendar.YEAR)+"-12-31");
        }
        List<Rappel> listRappel = rappelRepository.findAllByDateRetourBetween(startDate, endDate);
        List<Integer> matriculesAgents =   listRappel.stream().map(Rappel::getMatricule).collect(Collectors.toList());

        if(matriculesAgents.isEmpty()){
            return Collections.EMPTY_LIST;
        }
        String listStr= matriculesAgents.toString();
        listStr= listStr.replace('[',' ').replace(']', ' ');

        System.out.println(listStr);
        ResponseEntity<Agent[]> response = restTemplate.getForEntity(baseUrl+"listAgentmatricules/"+listStr, Agent[].class);

        List<Agent> agents =  Arrays.asList(response.getBody());

        List<RappelDto> rappelDoneDTO = (listRappel).stream().map(this::convertToDto).collect(Collectors.toList());
        rappelDoneDTO.forEach( rappel -> {
            Agent agent = findAgentByMatricule(agents, Long.valueOf(rappel.getMatricule()));
            rappel.setAgent(agent);
        });

        return rappelDoneDTO;
    }



    public List<RappelDto> getAllRappelsYearByEntity(int matricule, String type) {
        List<RappelDto> rappelDtosWithoutEmptyUser = new ArrayList<>();
        ResponseEntity<Agent[]> response = restTemplate.getForEntity
                (baseUrl + "equipe/" + type + "/" + matricule , Agent[].class);

        List<Agent> agents =  Arrays.asList(response.getBody());

        List<Long> matriculesAgents =   agents.stream().map(Agent::getMatricule).collect(Collectors.toList());
        List<Integer> matricules = matriculesAgents.stream().mapToInt(Long::intValue).boxed().collect(Collectors.toList());


        List<RappelDto> listRapel= (rappelRepository.findRappelsYearByDirection(matricules)).stream().map(this::convertToDto).collect(Collectors.toList());

        listRapel.forEach( rappel -> {
            Agent agent = findAgentByMatricule(agents, Long.valueOf(rappel.getMatricule()));
            rappel.setAgent(agent);
            if(agent!=null){
                rappelDtosWithoutEmptyUser.add(rappel);
            }
        });

        return rappelDtosWithoutEmptyUser;
    }


    public Object getAllRappelsMonthByEntity(int matricule) {
        ResponseEntity<Agent[]> response = restTemplate.getForEntity
                (baseUrl+"equipe/direction/" + matricule , Agent[].class);

        List<Agent> Agents =  Arrays.asList(response.getBody());


        List<Long> matriculesAgents =   Agents.stream().map(Agent::getMatricule).collect(Collectors.toList());
        List<Integer> matricules = matriculesAgents.stream().mapToInt(Long::intValue).boxed().collect(Collectors.toList());

        return rappelRepository.findCountRappelsMonthByDirection(matricules);
    }


    public Object getAllRappelsMonthByResponsable(int matricule) {

        ResponseEntity<Agent[]> response = restTemplate.getForEntity
                (baseUrl+"equipe/responsable/" + matricule , Agent[].class);

        List<Agent> Agents =  Arrays.asList(response.getBody());

        List<Long> matriculesAgents =   Agents.stream().map((Agent::getMatricule)).collect(Collectors.toList());
        List<Integer> matricules = matriculesAgents.stream().mapToInt(Long::intValue).boxed().collect(Collectors.toList());

        return rappelRepository.findCountRappelsMonthByResponsable(matricules);
    }




    private Agent findAgentByMatricule(List<Agent> agents, Long idAgent){
        Optional<Agent> optAgent = agents.stream().filter(agent -> agent.getMatricule().equals(idAgent)).findFirst();

        return optAgent.isPresent()? optAgent.get() : null;
    }

    private RappelDto convertToDto(Rappel rappel) {
        RappelDto rappelDto = modelMapper.map(rappel, RappelDto.class);
        return rappelDto;
    }

    public List<Rappel> getAllRappelsMOis(String mois_an) {

        LocalDate startDate, endDate;
        if(mois_an != null){
            startDate = daterParser.getDate("start", mois_an);
            endDate = daterParser.getDate("end", mois_an);
        }
        else {
            startDate = daterParser.getDate("start");
            endDate = daterParser.getDate("end");
        }
        return rappelRepository.findAllByDateRetourBetween(startDate, endDate);
    }

    public RappelStatsDTO getStatsRappel() {

        return new RappelStatsDTO(rappelRepository.getNombreRappelYear().intValue(), rappelRepository.getNombreRappelCurrentMonth().intValue());
    }
}
