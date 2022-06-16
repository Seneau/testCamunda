package com.seneau.rest;

import com.seneau.domain.Conge;
import com.seneau.dto.CongeAgentStepDTO;
import com.seneau.dto.CongeDto;
import com.seneau.dto.CongesStatsDTO;
import com.seneau.helpers.Converter;
import com.seneau.helpers.DaterParser;
import com.seneau.repository.CongeRepository;
import com.seneau.service.CongeService;
import org.camunda.bpm.engine.task.Task;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.keycloak.representations.AccessToken;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.net.URISyntaxException;
import java.util.*;

/**
 * REST controller for managing {@link Conge}.
 */
@RestController
@CrossOrigin
@RequestMapping("/api")
@Transactional
public class CongeResource {

    private final Logger log = LoggerFactory.getLogger(CongeResource.class);

    final
    Converter converter;
    private final HttpServletRequest request;

    public final ModelMapper modelMapper;

    public final CongeService congeService;

    final
    DaterParser daterParser;

    private final CongeRepository congeRepository;

    public CongeResource(CongeRepository congeRepository,
                         Converter converter,
                         HttpServletRequest request,
                         ModelMapper modelMapper,
                         CongeService congeService,
                         DaterParser daterParser) {
        this.congeRepository = congeRepository;
        this.converter = converter;
        this.request = request;
        this.modelMapper = modelMapper;
        this.congeService = congeService;
        this.daterParser = daterParser;
    }

    /**
     * {@code POST  /conges} : Create a new conge.
     *
     * @param conge the conge to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new conge, or with status {@code 400 (Bad Request)} if the conge has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/conges")
    public ResponseEntity<Conge> createConge( @RequestBody Conge conge) throws Exception {
        log.debug("REST request to save Conge : {}", conge);
        return congeService.saveConge(conge);

    }

    /**
     * {@code POST  /conges} : Create a new conge.
     *
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new conge, or with status {@code 400 (Bad Request)} if the conge has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @GetMapping("/congesProcess/")
    public List<Task> createCongeProcess() throws Exception {
        log.debug("REST request to save Conge : {}");
        return congeService.saveProcessConge();

    }

    /**
     * {@code PUT  /conges} : Updates an existing conge.
     *
     * @param conge the conge to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated conge,
     * or with status {@code 400 (Bad Request)} if the conge is not valid,
     * or with status {@code 500 (Internal Server Error)} if the conge couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/conges")
    public Object updateConge( @RequestBody Conge conge) throws Exception {
        log.debug("REST request to update Conge : {}", conge);

        return congeService.updateConge(conge);
    }


    @GetMapping(path = "/logout")
    public String logout(HttpServletRequest request) throws ServletException {
        request.logout();
        return "/";
    }
    /**
     * {@code GET  /conges} : get all the conges.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of conges in body.
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/conges")
    public List<CongeDto> getAllConges() {
        log.debug("REST request to get all Conges");
        return congeService.findAllConges();
    }


    @GetMapping("/conges/cloture")
    public List<CongeDto> getAllCongesCloture() {
        log.debug("REST request to get all Conges");
        return congeService.findCurrentYearCongeCloture();
    }



    @GetMapping("/conges/responsable/{matricule}")
    public List<Conge> getAllCongesByResponsable(@PathVariable int matricule) {
        log.debug("REST request to get all Conges");

        return congeService.getAllCongesByAgent(matricule, "responsable");
    }

    @GetMapping("/conges/direction/{matricule}")
    public List<Conge> getAllCongesByDirection(@PathVariable int matricule) {
        log.debug("REST request to get all Conges by Direction");

        return congeService.getAllCongesByAgent(matricule, "direction");
    }


    /**
     * {@code GET  /conges/:id} : get the "id" conge.
     *
     * @param id the id of the conge to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the conge, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/conges/{id}")
    public ResponseEntity<Conge> getConge(@PathVariable long id) {
        log.debug("REST request to get Conge : {}", id);
        Optional<Conge> conge = congeRepository.findById(id);
        if (conge.isPresent())
            return ResponseEntity.ok(conge.get());
        return ResponseEntity.notFound().build();

    }

    /**
     * {@code GET  /conges/:matricule} : get the "matricule" conge.
     *
     * @param matricule the id of the conge to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the conge, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/conges/agent/cloture/{matricule}")
    public ResponseEntity<CongeAgentStepDTO> getCongeAgent(@PathVariable Long matricule) {
        log.debug("REST request to get Conge : {}", matricule);
        return congeService.getCongeAgentCloture(matricule);


    }

    @GetMapping("/conges/matricule/{matricule}")
    public List<Conge> getCongebyMatricule(@PathVariable Long matricule) {
        log.debug("REST request to get Conge : {}", matricule);
        List<Conge> conge = congeRepository.findAllByMatricule(matricule);
        return conge;
    }


    /**
     * {@code DELETE  /conges/:id} : delete the "id" conge.
     *
     * @param id the id of the conge to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/conges/{id}")
    public ResponseEntity<Void> deleteConge(@PathVariable Long id) {
        log.debug("REST request to delete Conge : {}", id);
        Optional<Conge> conge = congeRepository.findById(id);
        if (!conge.isPresent()){
        return ResponseEntity.notFound().build();}
        congeRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    //------------------------//
    //   after modification   //
    //------------------------//

    /**
     * {@code GET  /conges/an} : get all the conges by year.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of conges in body.
     */
    @GetMapping({"/conges/an/{year}"})
    public List<CongeDto> getAllCongesByYear(@PathVariable Integer year, @RequestParam(required = false) Integer mois, @RequestParam(required = false) Integer step ) throws  Exception{
        log.debug("REST request to get all Conges by year");

        return congeService.getAllCongesByYear(year,mois,step);
    }

    /**
     * {@code GET  /conges/mois} : get all the conges by mois.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of conges in body.
     */
    @GetMapping("/conges/mois/{year}/{month}")
    public List<Conge> getAllCongesByMonth(@PathVariable() Integer year, @PathVariable Integer month, @RequestParam(required = false) String type) throws  Exception{
        log.debug("REST request to get all Conges by months");
        return congeService.getAllCongesByMonth(year, month, type);
    }


    @GetMapping("/conges/direction1/{matricule}")
    public List<CongeDto> getAllCongesByDirectionNew(@PathVariable int matricule) {
        log.debug("REST request to get all Conges by Direction");

        return congeService.getCongeWithAgent("equipe/direction/"+ matricule);

    }

    @GetMapping("/conges/direction_dga/{matricule}")
    public List<CongeDto> getAllCongesByDirection_dga(@PathVariable int matricule) {
        log.debug("REST request to get all Conges by DFC");

        return congeService.getCongeWithAgent("equipe/direction_dga/"+ matricule);
    }
    @GetMapping("/conges/n1/{matricule}")
    public List<CongeDto> getAllCongesN1(@PathVariable int matricule) {
        log.debug("REST request to get all Conges by Direction");

        return congeService.getCongeN1WithAgent(matricule);

    }


    @GetMapping("/conges/responsable1/{matricule}")
    public List<CongeDto> getAllCongesByResponsableNew(@PathVariable int matricule) {
        log.debug("REST request to get all Conges by Direction");

        return congeService.getCongeWithAgent("equipe/responsable/"+ matricule);
    }


    //-------------------------------------------
    //      GET nombre de conge par direction
    //-------------------------------------------
    @GetMapping("/conges/direction/count")
    public List<Object>  getCountCongeDirection(@RequestParam(required = false) Integer step) {
        log.debug("REST request to get countconge by Direction");

        return congeService.getCountCongeDirection(step);

    }


    /**
     * {@code GET  /conges/mois} : get all the conges number by departement.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of conges in body.
     */
    @GetMapping({"/conges/count/state"})
    public List<Object[]> getCountCongeByState() {

        log.debug("REST request to g number Conges by  Department");
        return congeRepository.findCountCongeByState();
    }

    /**
     * {@code GET  /conges/mois} : get all the conges number by departement.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of conges in body.
     */
    @GetMapping({"/conges/count/mois"})
    public List<Object> getCountCongeByMois() {

        log.debug("REST request to g number Conges by  mois");
        return congeRepository.findCountCongeByMois();
    }
    /**
     * {@code GET  /conges/mois/responsable"} : get all the conges number by departement.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of conges in body.
     */
    @GetMapping({"/conges/mois/responsable/{matricule}"})
    public Object getCountCongeByMoisResponsable(@PathVariable int matricule) {

        log.debug("REST request to get Conges by  ");

        return congeService.getCountCongeByMoisResponsable(matricule);
    }
    // fin after modification

    // route pour conge count direction mois

    /**
     * {@code GET  /conges/direction/count/mois} : get all the rappels.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of rappels in body.
     */
    @GetMapping({"/conges/direction/count/mois/{matricule}"})
    public Object getCountCongesMoisDirecteur(@PathVariable int matricule) {
        log.debug("REST request to get all Conges mois ");

        return congeService.getCountCongesMoisDirecteur(matricule);


    }

    @GetMapping({"/name"})
    @PreAuthorize("@congesCheck.check()")
    public String getCurrentUserLogin() {


        KeycloakAuthenticationToken token = (KeycloakAuthenticationToken) request.getUserPrincipal();
        KeycloakPrincipal principal=(KeycloakPrincipal)token.getPrincipal();
        KeycloakSecurityContext session = principal.getKeycloakSecurityContext();
        AccessToken accessToken = session.getToken();
        return accessToken.getPreferredUsername();
    }


    @GetMapping("/conges/stats")
    public CongesStatsDTO getStatsConge(){
        return congeService.getStatsConge();
    }

    @GetMapping("/conges/count/months")
    public ResponseEntity<?> getCongesYear(@RequestParam(required = false) Optional<String> direction){
           return ResponseEntity.ok(congeService.findCountCongesMoisDirection(direction.orElse("none")));
    }
}
