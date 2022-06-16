package com.seneau.rest;

import com.seneau.domain.Recuperation;
import com.seneau.dto.RecuperationDto;
import com.seneau.repository.RecuperationRepository;
import com.seneau.service.RecuperationService;
import org.keycloak.adapters.springsecurity.client.KeycloakRestTemplate;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URISyntaxException;
import java.util.List;

/**
 * REST controller for managing {@link Recuperation}.
 */
@RestController
@CrossOrigin
@RequestMapping("/api")
@Transactional
public class RecuperationResource {

    private final Logger log = LoggerFactory.getLogger(RecuperationResource.class);

    private static final String ENTITY_NAME = "mycongeRecuperation";

    final RecuperationService recuperationService;
    public final ModelMapper modelMapper;
    private final RecuperationRepository recuperationRepository;
    private final KeycloakRestTemplate restTemplate;

    @Value("${agent.baseurl}")
    private String baseUrl;



    public RecuperationResource(RecuperationRepository recuperationRepository,
                                KeycloakRestTemplate restTemplate,
                                ModelMapper modelMapper,
                                RecuperationService recuperationService) {
        this.recuperationRepository = recuperationRepository;
        this.restTemplate = restTemplate;
        this.modelMapper = modelMapper;
        this.recuperationService = recuperationService;
    }

    /**
     * {@code POST  /recuperations} : Create a new recuperation.
     *
     * @param recuperation the recuperation to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new recuperation, or with status {@code 400 (Bad Request)} if the recuperation has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/recuperations")
    public ResponseEntity<Recuperation> createRecuperation(@Valid @RequestBody Recuperation recuperation) throws URISyntaxException {
        log.debug("REST request to save Recuperation : {}", recuperation);

        return recuperationService.saveRecuperation(recuperation);

    }

    /**
     * {@code PUT  /recuperations} : Updates an existing recuperation.
     *
     * @param recuperation the recuperation to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated recuperation,
     * or with status {@code 400 (Bad Request)} if the recuperation is not valid,
     * or with status {@code 500 (Internal Server Error)} if the recuperation couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/recuperations")
    public ResponseEntity<Recuperation> updateRecuperation(@Valid @RequestBody Recuperation recuperation) throws URISyntaxException {
        log.debug("REST request to update Recuperation : {}", recuperation);
        return recuperationService.updateRecuperation(recuperation);
    }

    /**
     * {@code GET  /recuperations} : get all the recuperations.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of recuperations in body.
     */
    @GetMapping("/recuperations/an")
    public List<Recuperation> getAllRecuperations() {
        log.debug("REST request to get all Recuperations");
        return recuperationRepository.findAll();
    }
    /**
     * {@code GET  /recuperations/matricule/{matricule}} : get all the recuperations.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of recuperations in body.
     */
    @GetMapping("/recuperations/matricule/{matricule}")
    public List<Recuperation> getAllRecuperationsByMatricule(@PathVariable Long matricule) {
        log.debug("REST request to get all Recuperations");
        return recuperationRepository.findAllByMatricule(matricule);
    }

    /**
     * {@code GET  /recuperations/:id} : get the "id" recuperation.
     *
     * @param id the id of the recuperation to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the recuperation, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/recuperations/{id}")
    public ResponseEntity<Recuperation> getRecuperation(@PathVariable Long id) {
        log.debug("REST request to get Recuperation : {}", id);

        return recuperationService.getRecuperationById(id);
    }

    /**
     * {@code DELETE  /recuperations/:id} : delete the "id" recuperation.
     *
     * @param id the id of the recuperation to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/recuperations/{id}")
    public ResponseEntity<Void> deleteRecuperation(@PathVariable Long id) {
        log.debug("REST request to delete Recuperation : {}", id);
        return recuperationService.removeRecuperation(id);
    }


    @GetMapping("/recuperations/responsable/{matricule}")
    public List<RecuperationDto> getRecuperationResponsable(@PathVariable int matricule) {
        log.debug("REST request to get Recuperation : {}", matricule);

        return recuperationService.getRecuperationByTypeEntity(matricule, "responsable");

    }


    @GetMapping("/recuperations/direction/{matricule}")
    public List<RecuperationDto> getRecuperationDirection(@PathVariable int matricule) {
        log.debug("REST request to get Recuperation : {}", matricule);

        return recuperationService.getRecuperationByTypeEntity(matricule, "direction");

    }

    @GetMapping("/recuperations/direction_dga/{matricule}")
    public List<RecuperationDto> getRecuperationByDGA(@PathVariable int matricule) {
        log.debug("REST request to get Recuperation : {}", matricule);

        return recuperationService.getRecuperationByTypeEntity(matricule, "direction_dga");

    }


    @GetMapping("/recuperations/reporting")
    public Object getReporting() {
        log.debug("REST request to get reporting by direction {}");

        return recuperationService.getMoyenRecuperationDirection();
    }



}
