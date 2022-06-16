package com.seneau.rest;

import com.seneau.domain.Rappel;
import com.seneau.dto.RappelDto;
import com.seneau.dto.RappelStatsDTO;
import com.seneau.helpers.DaterParser;
import com.seneau.repository.RappelRepository;

import com.seneau.service.EmailService;
import com.seneau.service.RappelService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

/**
 * REST controller for managing {@link Rappel}.
 */
@RestController
@CrossOrigin
@RequestMapping("/api")
@Transactional
public class RappelResource {

    private final Logger log = LoggerFactory.getLogger(RappelResource.class);

    private static final String ENTITY_NAME = "mycongeRappel";

    @Value("${spring.application.name}")
    private String applicationName;

    @Value("${agent.baseurl}")
    private String baseUrl;

    private final EmailService emailService;

    private final RappelRepository rappelRepository;

    final
    DaterParser daterParser;

    public final ModelMapper modelMapper;
    private final RappelService rappelService;

    public RappelResource(RappelRepository rappelRepository, EmailService emailService, DaterParser daterParser, ModelMapper modelMapper, RappelService rappelService) {
        this.rappelRepository = rappelRepository;
        this.emailService = emailService;
        this.daterParser = daterParser;
        this.modelMapper = modelMapper;
        this.rappelService = rappelService;
    }

    /**
     * {@code POST  /rappels} : Create a new rappel.
     *
     * @param rappel the rappel to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new rappel, or with status {@code 400 (Bad Request)} if the rappel has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/rappels")
    public ResponseEntity<Rappel> createRappel(@Valid @RequestBody Rappel rappel) throws URISyntaxException {
        log.debug("REST request to save Rappel : {}", rappel);
        if (rappel.getId() != null) {
            new ResponseEntity<>("cette requette a deja ete éxecuté ", HttpStatus.FORBIDDEN);
        }
        Rappel result = rappelRepository.save(rappel);
        if (result == null)
            return ResponseEntity.notFound().build();

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(rappel.getId())
                .toUri();
       // send mail to agent rappelle

          if(rappel.getTracker().getStep() == 5) {
              emailService.mailRappel(rappel);
          }

        return ResponseEntity.created(uri)
                .body(rappel);
    }

    /**
     * {@code PUT  /rappels} : Updates an existing rappel.
     *
     * @param rappel the rappel to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated rappel,
     * or with status {@code 400 (Bad Request)} if the rappel is not valid,
     * or with status {@code 500 (Internal Server Error)} if the rappel couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/rappels")
    public ResponseEntity<Rappel> updateRappel(@Valid @RequestBody Rappel rappel) throws URISyntaxException {
        log.debug("REST request to update Rappel : {}", rappel);
        if (rappel.getId() == null)
            return ResponseEntity.notFound().build();
        Rappel result = rappelRepository.save(rappel);
        if(rappel.getTracker().getStep() == 5){
            emailService.mailRappel(rappel);
        }
        return ResponseEntity.ok(result);
    }

    /**
     * {@code GET  /rappels} : get all the rappels.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of rappels in body.
     */
    @GetMapping("/rappels")
    public List<Rappel> getAllRappels() {
        log.debug("REST request to get all Rappels");
        return rappelRepository.findAll();
    }



    /**
     * {@code GET  /rappels/:id} : get the "id" rappel.
     *
     * @param id the id of the rappel to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the rappel, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/rappels/{id}")
    public ResponseEntity<Rappel> getRappel(@PathVariable Long id) {
        log.debug("REST request to get Rappel : {}", id);
        Optional<Rappel> rappel = rappelRepository.findById(id);
        if (rappel.isPresent())
            return ResponseEntity.ok(rappel.get());
        return ResponseEntity.notFound().build();
    }

    /**
     * {@code DELETE  /rappels/:id} : delete the "id" rappel.
     *
     * @param id the id of the rappel to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/rappels/{id}")
    public ResponseEntity<Void> deleteRappel(@PathVariable Long id) {
        log.debug("REST request to delete Rappel : {}", id);
        Optional<Rappel> rappel = rappelRepository.findById(id);
        if (rappel.isPresent())

            return ResponseEntity.notFound().build();
        rappelRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
    //--------------------//
    // DEBUT MODFICATION  //
    //--------------------//
    /**
     * {@code GET  /rappels/an} : get all the rappels.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of rappels in body.
     */
    @GetMapping({"/rappels/an", "/rappels/an/{an}"})
    public List<RappelDto> getAllRappelsAn(@PathVariable(required = false)  String an) {
        log.debug("REST request to get all Rappels an ");

        return rappelService.getAllRappelsYear(an);

    }

    /**
     * {@code GET  /rappels/mois} : get all the rappels.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of rappels in body.
     */
    @GetMapping({"/rappels/mois", "/rappels/mois/{mois_an}"})
    public List<Rappel> getAllRappelsMois(@PathVariable(required = false) String mois_an) {
        log.debug("REST request to get all Rappels an ");
        return rappelService.getAllRappelsMOis(mois_an);
    }


    /**
     * {@code GET  /rappels/manager/mois} : get all the rappels.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of rappels in body.
     */
    @GetMapping({"/rappels/manager/count/mois/{matricule}"})
    public Object getCountRappelsMoisManager(@PathVariable int matricule) {
        log.debug("REST request to get all Rappels mois  responsable");

        return rappelService.getAllRappelsMonthByResponsable(matricule);
    }



    /**
     * {@code GET  /rappels/manager/year} : get all the rappels.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of rappels in body.
     */
    @GetMapping({"/rappels/manager/{matricule}"})
    public List<RappelDto> getAllRappelsYearManager(@PathVariable int matricule) {
        log.debug("REST request to get all Rappels an ");

        return  rappelService.getAllRappelsYearByEntity(matricule, "responsable");

    }



    /**
     * {@code GET  /rappels/directeur/mois} : get all the rappels directeur.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of rappels in body.
     */
    @GetMapping({"/rappels/directeur/count/mois/{matricule}"})
    public Object getCountRappelsMoisDirecteur(@PathVariable int matricule) {
        log.debug("REST request to get all Rappels mois directeur ");

        return rappelService.getAllRappelsMonthByEntity(matricule);


    }



    /**
     * {@code GET  /rappels/direction/year} : get all the rappels.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of rappels in body.
     */
    @GetMapping({"/rappels/direction/{matricule}"})
    public List<RappelDto> getAllRappelsYearDirection(@PathVariable int matricule) {
        log.debug("REST request to get all Rappels an ");

        return  rappelService.getAllRappelsYearByEntity(matricule, "direction");
    }

    /**
     * {@code GET  /rappels/direction/year} : get all the rappels.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of rappels in body.
     */
    @GetMapping({"/rappels/direction_dga/{matricule}"})
    public List<RappelDto> getAllRappelsYearDirectionDGA(@PathVariable int matricule) {
        log.debug("REST request to get all Rappels an ");

        return  rappelService.getAllRappelsYearByEntity(matricule, "direction_dga");
    }

    @GetMapping("/rappels/stats")
    public RappelStatsDTO getStatsRappel(){
        return rappelService.getStatsRappel();
    }

    // FIN MODIFICATION
}
