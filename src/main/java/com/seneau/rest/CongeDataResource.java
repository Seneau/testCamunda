package com.seneau.rest;

import com.seneau.domain.*;
import com.seneau.domain.Agent;
import com.seneau.dto.CongeDataDto;
import com.seneau.service.CongeDataService;
import org.keycloak.adapters.springsecurity.client.KeycloakRestTemplate;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import com.seneau.repository.CongeDataRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST controller for managing {@link CongeData}.
 */
@RestController
@CrossOrigin
@RequestMapping("/api")
@Transactional
public class CongeDataResource {

    private final Logger log = LoggerFactory.getLogger(CongeDataResource.class);

    private static final String ENTITY_NAME = "myCongeCongeData";
    @Value("${agent.baseurl}")
    private String baseUrl;

    @Autowired
    private CongeDataService congeDataService;

    public final ModelMapper modelMapper;

    private final KeycloakRestTemplate restTemplate;
    private final CongeDataRepository congeDataRepository;

    public CongeDataResource(CongeDataRepository congeDataRepository, ModelMapper modelMapper, KeycloakRestTemplate restTemplate) {
        this.congeDataRepository = congeDataRepository;
        this.modelMapper = modelMapper;
        this.restTemplate = restTemplate;
    }

    /**
     * {@code POST  /conge-data} : Create a new congeData.
     *
     * @param congeData the congeData to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new congeData, or with status {@code 400 (Bad Request)} if the congeData has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/conge-data")
    public ResponseEntity<CongeData> createCongeData( @RequestBody CongeData congeData)
            throws URISyntaxException {
        log.debug("REST request to save CongeData : {}", congeData);
        if (congeData.getId() != null) {
            new ResponseEntity<>("pas besion du id ", HttpStatus.FORBIDDEN);
        }
        CongeData result = congeDataRepository.save(congeData);

        if (result == null) {
            return ResponseEntity.notFound().build();
        } else {
            URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(congeData.getId())
                    .toUri();

            return ResponseEntity.created(uri)
                    .body(congeData);
        }
    }

    /**
     * {@code PUT  /conge-data} : Updates an existing congeData.
     *
     * @param congeData the congeData to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated congeData,
     * or with status {@code 400 (Bad Request)} if the congeData is not valid,
     * or with status {@code 500 (Internal Server Error)} if the congeData couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/conge-data")
    public ResponseEntity<CongeData> updateCongeData( @RequestBody CongeData congeData)
            throws URISyntaxException {
        log.debug("REST request to update CongeData : {}", congeData);
        if (congeData.getId() == null)
            return ResponseEntity.notFound().build();
        CongeData result = congeDataRepository.save(congeData);
        return ResponseEntity.ok(result);

    }

    /**
     * {@code GET  /conge-data} : get all the congeData.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of congeData in body.
     */
    @GetMapping("/conge-data")
    public List<CongeDataDto> getAllCongeData() {
        log.debug("REST request to get all CongeData");

        ResponseEntity<Agent[]> response = restTemplate.getForEntity
                (baseUrl , Agent[].class);

        List<Agent> agents =  Arrays.asList(response.getBody());

        List<CongeDataDto> listCD= (congeDataRepository.findAll()).stream().map(this::convertToDto).collect(Collectors.toList());
        listCD.forEach( cd -> {
            Agent agent = findAgentByMatricule(agents, Long.valueOf(cd.getMatricule()));
            System.out.println(agent);
            cd.setAgent(agent);
        });

        return listCD;
    }

    /**
     * {@code GET  /conge-data} : get all the congeData.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of congeData in body.
     */
    @GetMapping("/conge-data/matricule/{matricule}")
    public ResponseEntity<CongeData> getCongeDataByMatricule(@PathVariable Long matricule) {
        log.debug("REST request to get CongeData");
        Optional<CongeData> cd = congeDataRepository.findByMatricule(matricule);

        if (cd.isPresent())
            return ResponseEntity.ok(cd.get());
        return ResponseEntity.notFound().build();
    }


    /**
     * {@code GET  /conge-data/:id} : get the "id" congeData.
     *
     * @param id the id of the congeData to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the congeData, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/conge-data/{id}")
    public ResponseEntity<CongeData> getCongeData(@PathVariable Long id) {
        log.debug("REST request to get CongeData : {}", id);
        Optional<CongeData> conge = congeDataRepository.findById(id);
        if (conge.isPresent())
            return ResponseEntity.ok(conge.get());
        return ResponseEntity.notFound().build();


    }
    /**
     * {@code DELETE  /conge-data/:id} : delete the "id" congeData.
     *
     * @param id the id of the congeData to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/conge-data/{id}")
    public ResponseEntity<Void> deleteCongeData(@PathVariable Long id) {
        log.debug("REST request to delete CongeData : {}", id);
        congeDataRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // after modification //
    @GetMapping("/conge-data/listCongeDataByMatricules/{listmatricules}")
    public List<Object>  getlistmatricules(@PathVariable()  Long[] listmatricules) {
        log.debug("REST request to get all Agents");


        List<Object> congeData = congeDataRepository.findByListMatricule(listmatricules);

        return congeData ;
    }

    private Agent findAgentByMatricule(List<Agent> agents, Long idAgent){
        Optional<Agent> optAgent = agents.stream().filter(agent -> agent.getMatricule().equals(idAgent)).findFirst();

        return optAgent.isPresent()? optAgent.get() : null;
    }

    private CongeDataDto convertToDto(CongeData congeData) {
        CongeDataDto congeDataDto = modelMapper.map(congeData, CongeDataDto.class);
        return congeDataDto;
    }


    @PostMapping("/conge-data/recuperations/file")
    public Object addRecuperationCongeDataByFile(@RequestParam("recuperationsFile") MultipartFile file) throws IOException {
        log.debug("REST request to save conge-data content  infile: {}", file);

        return congeDataService.saveRecuperationByFile(file);

    }

    @PostMapping("/conge-data/file")
    public Object updateCongeDataByFile(@RequestParam("congeDataFile") MultipartFile congeDataFile) throws IOException {
        log.debug("REST request to update conge-data content  in file: {}", congeDataFile);
        return congeDataService.updateCongeDataByFile(congeDataFile);

    }

}
