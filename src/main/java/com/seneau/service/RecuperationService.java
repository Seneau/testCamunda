package com.seneau.service;

import com.seneau.agileo.AgileoService;
import com.seneau.agileo.dtos.WebhookDTO;
import com.seneau.domain.Agent;
import com.seneau.domain.Recuperation;
import com.seneau.dto.RecuperationDto;
import com.seneau.repository.RecuperationRepository;
import lombok.val;
import org.keycloak.adapters.springsecurity.client.KeycloakRestTemplate;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class RecuperationService {

    private final AgileoService agileoService;

    final
    RecuperationRepository recuperationRepository;

    final
    KeycloakRestTemplate restTemplate;

    final
    ModelMapper modelMapper;

    @Value("${agent.baseurl}")
    private String baseUrl;

    public RecuperationService(KeycloakRestTemplate restTemplate,
                               RecuperationRepository recuperationRepository,
                               ModelMapper modelMapper, AgileoService agileoService) {
        this.restTemplate = restTemplate;
        this.recuperationRepository = recuperationRepository;
        this.modelMapper = modelMapper;
        this.agileoService = agileoService;
    }


    /*
     | REPORTING MOYENNE JOUR DE RECUPERATION PAR DIRECTION
     */

    public Object getMoyenRecuperationDirection(){
        List<Recuperation> recuperations = recuperationRepository.findAllRecuperationValidateByYear();
        List<Long> matriculesAgents =   recuperations.stream().map(Recuperation::getMatricule).collect(Collectors.toList());

        if(matriculesAgents.isEmpty()){
            return Collections.EMPTY_LIST;
        }
       String listStr= matriculesAgents.toString();
        listStr= listStr.replace('[',' ').replace(']', ' ');

        ResponseEntity<Agent[]> response = restTemplate.getForEntity
                (baseUrl+"listAgentmatricules/"+listStr, Agent[].class);

        List<Agent> agents =  Arrays.asList(response.getBody());

        Map<String, Integer> listDirection = new HashMap<>();
        Map<String, Integer> nbrAgentDirection = new HashMap<>();
        recuperations.forEach( recuperation -> {
            Long matricule = recuperation.getMatricule();
            int value = recuperation.getNbrJour();
            String direction =getAgentByMatricule(agents, matricule).getDirection();
            if(listDirection.containsKey(direction)){
                value = value + listDirection.get(direction);
            }


            Integer  increm = 1;
            if(nbrAgentDirection.containsKey(direction)){
                increm = 1 + nbrAgentDirection.get(direction);
            }

            nbrAgentDirection.put(direction, increm);

            listDirection.put(direction, value);
        });


        /*
         | Croisement des tableaux contenants le total des nombres de congés
         */
        listDirection.forEach( (key,direction) ->{
            int moy= (direction / nbrAgentDirection.get(key));
            listDirection.put(key,moy);
        });
        return listDirection;
    }

    private Integer getNbrRecupFromRecuperationByMatricule(List<Recuperation> recuperation, Long matricule){

    return  recuperation.stream().filter( el -> el.getMatricule().equals(matricule)).collect(Collectors.toList()).stream().findFirst().get().getNbrJour();

    }

    private Agent getAgentByMatricule(List<Agent> agents, Long matricule){
        return  agents.stream().filter( el -> el.getMatricule().equals(matricule)).collect(Collectors.toList()).get(0);
    }


    /*
     | List des recuperation par rapport au matricule d'un manager ou d'un directeur
     */

    public List<RecuperationDto> getRecuperationByTypeEntity(int matricule, String type) {
        List<RecuperationDto> recupDtosWithoutEmptyUser= new ArrayList<>();
        ResponseEntity<Agent[]> response = restTemplate.getForEntity
                (baseUrl + "equipe/"+ type+"/" + matricule , Agent[].class);
        val response1 = restTemplate.getForEntity(baseUrl + "equipe/"+ type+"/" + matricule , Agent[].class);

        val agents =  Arrays.asList(response.getBody());


        List<Long> matriculesAgents =   agents.stream().map(Agent::getMatricule).collect(Collectors.toList());

        List<RecuperationDto> listRecup= (recuperationRepository.findRecuparationYear(matriculesAgents)).stream().map(this::convertToDto).collect(Collectors.toList());
        listRecup.forEach( recup -> {
            Agent agent = findAgentByMatricule(agents, Long.valueOf(recup.getMatricule()));
            recup.setAgent(agent);
            if(agent!=null){
                recupDtosWithoutEmptyUser.add(recup);
            }
        });

        return recupDtosWithoutEmptyUser;
    }


    /*
      | Fonctions utils à mettre dans un fichier séparé!!!
     */
    private Agent findAgentByMatricule(List<Agent> agents, Long idAgent){
        Optional<Agent> optAgent = agents.stream().filter(agent -> agent.getMatricule().equals(idAgent)).findFirst();

        return optAgent.isPresent()? optAgent.get() : null;
    }

    private RecuperationDto convertToDto(Recuperation recuperation) {
        RecuperationDto recuperationDto = modelMapper.map(recuperation, RecuperationDto.class);
        return recuperationDto;
    }


    /*
      | Methode ajouter une recuperation!!!
     */
    public ResponseEntity<Recuperation> saveRecuperation(Recuperation recuperation) {

        if (recuperation.getId() != null) {
            new ResponseEntity<>("cette requette a deja ete éxecuté ", HttpStatus.FORBIDDEN);
        }

        Recuperation result = recuperationRepository.save(recuperation);
        if (result == null) {

            return ResponseEntity.notFound().build();
        }

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(recuperation.getId())
                .toUri();

        return ResponseEntity.created(uri)
                .body(recuperation);
    }

    /*
     | Method pour la suppression dune recuperation une recuperation!!!
    */
    public ResponseEntity<Void> removeRecuperation(Long id) {

        Optional<Recuperation> recuperation = recuperationRepository.findById(id);
        if (!recuperation.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        recuperationRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    /*
     | GET recuperation by Id !!
    */
    public ResponseEntity<Recuperation> getRecuperationById(Long id) {
        Optional<Recuperation> recuperation = recuperationRepository.findById(id);
        if (recuperation.isPresent()){
            return ResponseEntity.ok(recuperation.get());
        }
        return ResponseEntity.notFound().build();
    }

    public ResponseEntity<Recuperation> updateRecuperation(Recuperation recuperation) {
        if (recuperation.getId() == null){
            return ResponseEntity.notFound().build();
        }


        Recuperation result = recuperationRepository.save(recuperation);

        //if(recuperation.getTracker().getStep()==2 ||recuperation.getTracker().getStep()==3){
         //   agileoService.sendWebhook(new WebhookDTO(recuperation));
        // }

        return ResponseEntity.ok(result);
    }
}

