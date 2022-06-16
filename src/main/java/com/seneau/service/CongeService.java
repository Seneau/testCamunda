package com.seneau.service;

import com.seneau.agileo.AgileoService;
import com.seneau.domain.Agent;
import com.seneau.domain.Conge;
import com.seneau.dto.CongeAgentStepDTO;
import com.seneau.dto.CongeDto;
import com.seneau.dto.CongesStatsDTO;
import com.seneau.helpers.Converter;
import com.seneau.helpers.DaterParser;
import com.seneau.repository.CongeDataRepository;
import com.seneau.repository.CongeRepository;
import lombok.val;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.keycloak.adapters.springsecurity.client.KeycloakRestTemplate;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CongeService {
    private final org.springframework.web.client.RestTemplate RestTemplate;

    final
    Converter converter;

    @Autowired
    public final RuntimeService runtimeService;

    @Autowired
    public final TaskService taskService;

    public final ModelMapper modelMapper;

    final
    DaterParser daterParser;
    @Value("${agent.baseurl}")
    private String baseUrl;

    private final AgileoService agileoService;

    private final AuthService authService;

    private final EmailService emailTreatment;

    private final CongeRepository congeRepository;


    private final KeycloakRestTemplate restTemplate;

    private final CongeDataRepository congeDataRepository;


    public CongeService(CongeRepository congeRepository, CongeDataRepository congeDataRepository, RestTemplate RestTemplate, Converter converter, RuntimeService runtimeService, TaskService taskService, ModelMapper modelMapper, DaterParser daterParser, AuthService authService, EmailService emailTreatment, KeycloakRestTemplate restTemplate, AgileoService agileoService) {
        this.congeRepository = congeRepository;
        this.congeDataRepository = congeDataRepository;
        this.RestTemplate = RestTemplate;
        this.converter = converter;
        this.runtimeService = runtimeService;
        this.taskService = taskService;
        this.modelMapper = modelMapper;
        this.daterParser = daterParser;
        this.authService = authService;
        this.emailTreatment = emailTreatment;
        this.restTemplate = restTemplate;
        this.agileoService = agileoService;
    }
    private CongeDto convertToDto(Conge conge) {
        CongeDto congeDto = modelMapper.map(conge, CongeDto.class);
        return congeDto;
    }

    public List<CongeDto> getCongeWithAgent(String path){

        List<CongeDto> congeDTOsWithoutEmptyUser = new ArrayList<>();
        ResponseEntity<Agent[]> response = restTemplate.getForEntity
                (baseUrl+path, Agent[].class);

        List<Agent> agents =  Arrays.asList(response.getBody());

        List<Long> matriculesAgents =   agents.stream().map(Agent::getMatricule).collect(Collectors.toList());
        if(matriculesAgents.isEmpty()){
            return Collections.EMPTY_LIST;
        }
        List<CongeDto> congeDepartment = (congeRepository.findCongeByMatriculesList(matriculesAgents)).stream().map(this::convertToDto).collect(Collectors.toList());
        congeDepartment.forEach( conge -> {
            Agent agent = findAgentByMatricule(agents, conge.getMatricule());
            conge.setAgent(agent);
            if(agent!=null){
                congeDTOsWithoutEmptyUser.add(conge);
            }
        });

        return congeDTOsWithoutEmptyUser;

    }

    private Agent findAgentByMatricule(List<Agent> agents, Long idAgent){
        Optional<Agent> optAgent = agents.stream().filter(agent -> agent.getMatricule().equals(idAgent)).findFirst();

        return optAgent.isPresent()? optAgent.get() : null;
    }

    public List<Task> saveProcessConge(){
       Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("niveau", "four");

        List<Task> taskList = taskService.createTaskQuery().processInstanceId("b3e584a4-d600-11ec-90cb-f60bc6507d8b").list();

        //return  runtimeService.startProcessInstanceByKey("MycongeWorkflow",variables);
      //  ProcessInstance instance = runtimeService.startProcessInstanceById("MycongeWorkflow:23:cbb7dcee-d5f9-11ec-9e4e-5293d245d0ce2",variables);
             //   .startBeforeActivity("SendInvoiceReceiptTask")
              // .setVariable("niveau", "four")
             //   .startBeforeActivity("DeliverPizzaSubProcess")
             //   .setVariableLocal("destination", "12 High Street")
               // .execute();
       // taskService.
  //      taskService.complete("05f2b147-d215-11ec-af97-be049d13397b");
            return taskList ;

    }


    public ResponseEntity<Conge>  saveConge(Conge conge){

        val matriculeAgent = conge.getMatricule();
        List<Integer> precedentMonth3= new ArrayList<>();
        List<Integer> listAns= new ArrayList<>();
        listAns.add(conge.getAnnee());
        if(conge.getMois() - 3 < 0) {
            for (int i = 1; i <= conge.getMois(); i++) {
                precedentMonth3.add(i);
            }
            for (int j = 12; j > (9 + conge.getMois()); j--) {
                precedentMonth3.add(j);
            }
            listAns.add(conge.getAnnee()-1);
        }else {

            for (int k = (conge.getMois()-2); k <= conge.getMois(); k++) {
                precedentMonth3.add(k);
            }
        }

            if(!congeRepository.finCongeAgentInDate(matriculeAgent, listAns, precedentMonth3).isEmpty()){
               return new ResponseEntity("L'utilisateur a eu un congé pendant les 3 mois précédant ce mois ce choisi!", HttpStatus.BAD_REQUEST);
            }

        if (conge.getId() != null) {
            new ResponseEntity<>("cette requette a deja ete éxecuté ", HttpStatus.BAD_REQUEST);
        }

        /*
        // Ajouter le matricule de l'utilisateur encours
        String str_matricule =authService.getCurrentUserMatricule();
        if(str_matricule==""){
            new ResponseEntity<>("un utilisateur sans matricule ne peut ajouter un congé - Veuillez contactez votre administrateur", HttpStatus.BAD_REQUEST);
        }
        Long matricule_current_user = Long.valueOf(authService.getCurrentUserMatricule());

        conge.setMatriculeCreator(matricule_current_user);


         */
        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("niveau",4);
        ProcessInstance process =  runtimeService.startProcessInstanceByKey("MycongeWorkflow",variables);
        conge.setProcessInstanceId(process.getProcessInstanceId());

        Conge result = congeRepository.save(conge);
        if (result == null)
            return ResponseEntity.notFound().build();

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(conge.getId())
                .toUri();

        // envoyé un email pour validation;
       if(conge.getTracker().getStep() == 1 || conge.getTracker().getStep() == 2 ) {
           emailTreatment.mailTreatment(conge, "new");
       }
        return ResponseEntity.created(uri)
                .body(conge);

    }

    public Object updateConge(Conge conge){

        if (conge.getId() == null)
            return ResponseEntity.notFound().build();
        int old_step = congeRepository.findById(conge.getId()).get().getTracker().getStep();
        Conge result = congeRepository.save(conge);
        int new_step = result.getTracker().getStep();
        if(old_step != new_step && (new_step== 0 || new_step == 3 || new_step == 4 )   ){
            emailTreatment.mailTreatment(result,"update");
        }

        if(old_step != new_step && (new_step== 2)   ){
            emailTreatment.mailTreatment(conge,"new");
        }

        //if(new_step == 6){
          //  agileoService.sendWebhook(new WebhookDTO(conge));
       // }
        return result;
    }




    public List<Conge> getAllCongesByAgent(int matricule,String type){

        List<Conge> Conges = congeRepository.findAll();
        String URL = baseUrl + "equipe/" + type + "/" + matricule;

        ResponseEntity<Agent[]> response = restTemplate.getForEntity
                (URL, Agent[].class);

        List<Agent> agents =  Arrays.asList(response.getBody());


        List<Long> matriculesAgents =   agents.stream().map(Agent::getMatricule).collect(Collectors.toList());


        if(matriculesAgents.isEmpty()){
            return Collections.EMPTY_LIST;
        }
        // List<Agent> matriculeAgents = Agents.stream().filter(M -> M.getMatricule()).collect(Collectors.toList());

        List<Conge>   FilteredCongesDirections = Conges.stream()
                .filter( congesfiltre -> matriculesAgents.contains(congesfiltre.getMatricule()))
                .collect(Collectors.toList());
        return FilteredCongesDirections;
    }


    private Boolean isUserHasCurrentRequestConge(Long matricule){
      return congeRepository.findIfUserHasCCR(matricule).isPresent() ? true : false;

    }

    public List<CongeDto> getAllCongesByYear(Integer year, Integer mois, Integer step) {
        List<CongeDto> congeDTOsWithoutEmptyUser = new ArrayList<>();
        List<Conge> congeDoneYear = congeRepository.findCongeDoneByYear(year,mois,step);

        if(congeDoneYear.isEmpty()) return Collections.EMPTY_LIST;


        List<Long> matriculesAgents =   congeDoneYear.stream().map(Conge::getMatricule).collect(Collectors.toList());

        if(matriculesAgents.isEmpty()){
            return Collections.EMPTY_LIST;
        }
        String listStr= matriculesAgents.toString();
        listStr= listStr.replace('[',' ').replace(']', ' ');

        ResponseEntity<Agent[]> response = restTemplate.getForEntity
                (baseUrl+"listAgentmatricules/"+listStr, Agent[].class);

        List<Agent> agents =  Arrays.asList(response.getBody());
        List<CongeDto> congeDoneDTO = (congeDoneYear).stream().map(this::convertToDto).collect(Collectors.toList());
        congeDoneDTO.forEach( conge -> {
            Agent agent = findAgentByMatricule(agents, conge.getMatricule());
            conge.setAgent(agent);
            if(agent!=null){
                congeDTOsWithoutEmptyUser.add(conge);
            }
        });

        return congeDTOsWithoutEmptyUser;
    }

    public List<Conge> getAllCongesByMonth(Integer year, Integer month, String type) {
        List<Conge>  conges = congeRepository.findAllByYearAndMonth(year, month);
        switch (type) {
            case "direction":
                ResponseEntity<Agent[]> response = restTemplate.getForEntity
                        (baseUrl+"equipe/responsable/"  , Agent[].class);

                List<Agent> Agents =  Arrays.asList(response.getBody());


                List<Long> matriculesAgents =   Agents.stream().map(Agent::getMatricule).collect(Collectors.toList());
                break;
            case "responsable":

                break;
            case "n1":

                break;
        }
        return conges;
    }

    public List<Object> getCountCongeDirection(Integer step) {

        LocalDate date= LocalDate.now();
        Integer annee= date.getYear();

        List<Conge>  congeDoneYear = congeRepository.findCongeDoneByYear(annee, null,step);
        List<Long> matriculesAgents =   congeDoneYear.stream().map(Conge::getMatricule).collect(Collectors.toList());

        if(matriculesAgents.isEmpty()){
            return Collections.EMPTY_LIST;
        }
        String listStr= matriculesAgents.toString();
        listStr= listStr.replace('[',' ').replace(']', ' ');

        ResponseEntity<Object[]> response = restTemplate.getForEntity
                (baseUrl+"listmatricules/"+listStr, Object[].class);

        List<Object> agents =  Arrays.asList(response.getBody());

        return agents;
    }

    public Object getCountCongeByMoisResponsable(int matricule) {

        ResponseEntity<Agent[]> response = restTemplate.getForEntity
                (baseUrl+"equipe/responsable/" + matricule , Agent[].class);

        List<Agent> Agents =  Arrays.asList(response.getBody());


        List<Long> matriculesAgents =   Agents.stream().map(Agent::getMatricule).collect(Collectors.toList());


        return congeRepository.findCongesMonthByResponsable(matriculesAgents);
    }

    public Object getCountCongesMoisDirecteur(int matricule) {

        ResponseEntity<Agent[]> response = restTemplate.getForEntity
                (baseUrl+"equipe/direction/" + matricule , Agent[].class);

        List<Agent> agents =  Arrays.asList(response.getBody());


        List<Long> matricules =   agents.stream().map((Agent::getMatricule)).collect(Collectors.toList());

        return congeRepository.findCountCongesMonthByDirection(matricules);
    }

    public ResponseEntity<CongeAgentStepDTO> getCongeAgentCloture(Long matricule) {

        CongeAgentStepDTO congeAgentStepDTO = new CongeAgentStepDTO();
        congeAgentStepDTO.setConges(congeRepository.findByMatriculeStep(matricule, 6));
        congeAgentStepDTO.setAgent(getAgentByMatricule(matricule));

        return ResponseEntity.ok(congeAgentStepDTO);
    }

    private Agent getAgentByMatricule(Long matricule){

        return restTemplate.getForEntity(baseUrl + matricule +"/info", Agent.class).getBody();
    }

    public void cancelConge() {
        int year = Calendar.getInstance().get(Calendar.YEAR);
        int month = Calendar.getInstance().get(Calendar.MONTH);

        congeRepository.cancelConge();

    }

    public CongesStatsDTO getStatsConge() {
         CongesStatsDTO congesStatsDTO = congeRepository.findNbrJoursCongesPris();

        List<Conge> conges = congeRepository.findCongeDoneByYear();

         congesStatsDTO.setNbrCongesDirection(getCongeByDirection(conges));
         congesStatsDTO.setNbrCongesCurrentMonth(((Long) congeRepository.findCountCongeByCurrentMois()).intValue());
         return congesStatsDTO;
    }

    private List<Object> getCongeByDirection(List<Conge> conges){

        List<Long> matriculesAgents =   conges.stream().map(Conge::getMatricule).collect(Collectors.toList());
        if(matriculesAgents.isEmpty()){
            return Collections.EMPTY_LIST;
        }
        String listStr= matriculesAgents.toString();
        listStr= listStr.replace('[',' ').replace(']', ' ');

        ResponseEntity<Object[]> response = restTemplate.getForEntity
                (baseUrl+"listmatricules/"+listStr, Object[].class);

        List<Object> result =  Arrays.asList(response.getBody());

     /*   Map<String, DirectionNombreDTO>  mapDirection = new HashMap<>();
        agents.forEach( agent ->{
            if ( mapDirection.get(agent.getDirection())== null) {
                mapDirection.put(agent.getDirection(), new DirectionNombreDTO());
            }
          DirectionNombreDTO directionNombreDTO = mapDirection.get(agent.getDirection());
          Integer nombreJrConge = (nombreJrsCongeAgent(conges, ((Number) agent.getMatricule()).longValue()).getNombreJourConge()).intValue();
          directionNombreDTO.setNbrEntity(directionNombreDTO.getNbrEntity() + nombreJrConge);
          int nbrConge =  directionNombreDTO.getNbrEntity();
          directionNombreDTO.setNbrEntity(nbrConge++);

          mapDirection.put(agent.getDirection(), directionNombreDTO);
        });*/
        System.out.println(result);
       return  result;

    }

    private Conge nombreJrsCongeAgent(List<Conge> conges, Long matricule ){
      return  conges.stream().findFirst().filter(conge -> conge.getMatricule()==matricule).get();
    }

    public Object findCountCongesMoisDirection(String direction) {
        List<Conge> conges =congeRepository.findCongeDoneByYear();
        List<Agent> agents =getListAgentsByConge(conges);
        List<CongeDto> congesAgents = mergeCongesAgents(conges, agents);
        Map<Integer, Integer>  mapCount = new HashMap<>();
        congesAgents.forEach( congeAgent ->{
           if((!direction.equals("none") && congeAgent.getAgent().getDirection().equals(direction)) || direction.equals("none")) {
                if(mapCount.get(congeAgent.getMois())==null){
                    mapCount.put(congeAgent.getMois(),1);
                }else{
                    Integer nbrConge = mapCount.get(congeAgent.getMois()) +1;
                    mapCount.put(congeAgent.getMois(), nbrConge);
                }
           }
        });
        return mapCount;
    }

    public Object findCountCongesMois() {
        // return congeRepository.findCountCongesMois();
        return null;
    }

    private List<Agent> getListAgentsByConge(List<Conge> conges ){
        List<Long> matriculesAgents =   conges.stream().map(Conge::getMatricule).collect(Collectors.toList());

        if(matriculesAgents.isEmpty()){
            return Collections.EMPTY_LIST;
        }
        String listStr= matriculesAgents.toString();
        listStr= listStr.replace('[',' ').replace(']', ' ');

        ResponseEntity<Agent[]> response = restTemplate.getForEntity
                (baseUrl+"listAgentmatricules/"+listStr, Agent[].class);

        List<Agent> agents =  Arrays.asList(response.getBody());
        return agents;
    }

    private List<Agent> getAgentByDirection(String direction){
        ResponseEntity<Agent[]> response = restTemplate.getForEntity
                (baseUrl+"directions/"+direction+"/agents", Agent[].class);

        List<Agent> agents =  Arrays.asList(response.getBody());
        return agents;

    }

    private List<CongeDto> mergeCongesAgents(List<Conge> conges, List<Agent> agents){
        List<CongeDto> congeDTOsWithoutEmptyUser = new ArrayList<>();
        List<CongeDto> congeDoneDTO = (conges).stream().map(this::convertToDto).collect(Collectors.toList());
        congeDoneDTO.forEach( conge -> {
            Agent agent = findAgentByMatricule(agents, conge.getMatricule());
            conge.setAgent(agent);
            if(agent!=null){
                congeDTOsWithoutEmptyUser.add(conge);
            }
        });

        return congeDTOsWithoutEmptyUser;
    }

    public List<CongeDto> findCurrentYearCongeCloture() {
        List<Conge> conges= congeRepository.findCongeDoneByYear();
        List<Agent> agents = getListAgentsByConge(conges);
        return mergeCongesAgents(conges,agents);
    }

    public List<CongeDto> findAllConges() {
        List<CongeDto> congeDTOsWithoutEmptyUser = new ArrayList<>();
        List<Conge> conges = congeRepository.findAll();
        List<CongeDto> congeDtos = conges.stream().map(this::convertToDto).collect(Collectors.toList());
        List<Long> matriculesAgents =   conges.stream().map(Conge::getMatricule).collect(Collectors.toList());

        if(matriculesAgents.isEmpty()){
            return Collections.EMPTY_LIST;
        }
        String listStr= matriculesAgents.toString();
        listStr= listStr.replace('[',' ').replace(']', ' ');

        ResponseEntity<Agent[]> response = restTemplate.getForEntity
                (baseUrl+"listAgentmatricules/"+listStr, Agent[].class);
      /*
        congeDtos.forEach(congeDto -> {
            ResponseEntity<Agent> reponse = restTemplate.getForEntity
                (baseUrl + congeDto.getMatricule() , Agent.class);
            congeDto.setAgent(reponse.getBody());
        });*/
        List<Agent> agents =  Arrays.asList(response.getBody());
        congeDtos.forEach( conge -> {
            Agent agent = findAgentByMatricule(agents, conge.getMatricule());
            conge.setAgent(agent);
            if(agent!=null){
                congeDTOsWithoutEmptyUser.add(conge);
            }
        });
        return congeDTOsWithoutEmptyUser;
    }

    public List<CongeDto> getCongeN1WithAgent(int matricule) {

        List<CongeDto> congeDTOsWithoutEmptyUser = new ArrayList<>();
        ResponseEntity<Agent[]> response = restTemplate.getForEntity
                (baseUrl + "n1/" + matricule, Agent[].class);

        List<Agent> agents =  Arrays.asList(response.getBody());

        List<Long> matriculesAgents =   agents.stream().map(Agent::getMatricule).collect(Collectors.toList());
        
        List<CongeDto> congeDepartment = (congeRepository.findCongeByMatriculesList(matriculesAgents)).stream().map(this::convertToDto).collect(Collectors.toList());
        congeDepartment.forEach( conge -> {
            Agent agent = findAgentByMatricule(agents, conge.getMatricule());
            conge.setAgent(agent);
            if(agent!=null){
                congeDTOsWithoutEmptyUser.add(conge);
            }
        });

        return congeDTOsWithoutEmptyUser;
    }
}
