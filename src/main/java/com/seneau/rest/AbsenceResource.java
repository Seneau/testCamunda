package com.seneau.rest;

import com.seneau.domain.Absence;
import com.seneau.dto.AbsenceDto;
import com.seneau.dto.AbsencesStatsDTO;
import com.seneau.repository.AbsenceRepository;
import com.seneau.service.AbsenceService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link Absence}.
 */
@RestController
@CrossOrigin
@RequestMapping("/api")
@Transactional
public class AbsenceResource {

    private final Logger log = LoggerFactory.getLogger(AbsenceResource.class);

    private static final String ENTITY_NAME = "mycongeAbsence";

    @Value("${spring.application.name}")
    private String applicationName;
    @Autowired
    private org.springframework.web.client.RestTemplate RestTemplate;

    public final ModelMapper modelMapper;
    private final AbsenceRepository absenceRepository;
    private final AbsenceService  absenceService;

    public AbsenceResource(AbsenceRepository absenceRepository,
                           ModelMapper modelMapper,
                           AbsenceService absenceService) {
        this.absenceRepository = absenceRepository;
        this.modelMapper = modelMapper;
        this.absenceService = absenceService;
    }

    @PostMapping("/absences")
    public ResponseEntity<Absence> createAbsence(@Valid @RequestBody Absence absence) throws URISyntaxException {
        log.debug("REST request to save Absence : {}", absence);
        if (absence.getId() != null) {
            new ResponseEntity<>("cette requette a deja ete éxecuté ", HttpStatus.FORBIDDEN);
        }
        Absence result = absenceRepository.save(absence);
        if (result == null)
            return ResponseEntity.notFound().build();

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(absence.getId())
                .toUri();
        return ResponseEntity.created(uri)
                .body(absence);
    }

    @PutMapping("/absences")
    public ResponseEntity<Absence> updateAbsence(@Valid @RequestBody Absence absence) throws URISyntaxException {
        log.debug("REST request to update Absence : {}", absence);

        return absenceService.updateAbsence(absence);
    }

    @GetMapping("/absences")
    public List<Absence> getAllabsences() {
        log.debug("REST request to get all absences");
        return absenceRepository.findAll();
    }

    @GetMapping("/absences/matricule/{matricule}")
    public List<Absence> getAllabsencesByMatricule(@PathVariable Long matricule) {
        log.debug("REST request to get all absences");
        return absenceRepository.findAllByMatricule(matricule);
    }

    @GetMapping("/absences/{id}")
    public ResponseEntity<Absence> getAbsence(@PathVariable Long id) {
        log.debug("REST request to get Absence : {}", id);
        Optional<Absence> Absence = absenceRepository.findById(id);
        if (Absence.isPresent())
            return ResponseEntity.ok(Absence.get());
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/absences/{id}")
    public ResponseEntity<Void> deleteAbsence(@PathVariable Long id) {
        log.debug("REST request to delete Absence : {}", id);
            Optional<Absence> Absence = absenceRepository.findById(id);
            if (!Absence.isPresent())
                return ResponseEntity.notFound().build();
            absenceRepository.deleteById(id);
            return ResponseEntity.noContent().build();
    }

    @GetMapping("/absences/responsable/{matricule}")
    public List<AbsenceDto> getAbsenceResponsable(@PathVariable int matricule, @RequestParam(required = false) Optional<Integer> step) {
        log.debug("REST request to get Absence : {}", matricule);
            return absenceService.getAbsenceByEntity(matricule, step, "responsable");
    }

    @GetMapping(value = "/absences/direction/{matricule}")
    public List<AbsenceDto> getAbsenceDirection(@PathVariable int matricule, @RequestParam(required = false) Optional<Integer> step) {
        log.debug("REST request to get Absence : {}", matricule);
        return absenceService.getAbsenceByEntity(matricule, step, "direction");
    }
    @GetMapping(value = "/absences/direction_dga/{matricule}")
    public List<AbsenceDto> getAbsenceDirectionDGA(@PathVariable int matricule, @RequestParam(required = false) Optional<Integer> step) {
        log.debug("REST request to get Absence : {}", matricule);
        return absenceService.getAbsenceByEntity(matricule, step, "direction_dga");
    }

    @GetMapping({"/absences/an", "/absences/an/{an}"})
    public List<AbsenceDto> getAllAbsencesAn(@PathVariable(required = false)  Optional<String> an) {
        log.debug("REST request to get all Absences an ");
        if(an.isPresent()){
            return absenceService.getAbsenceByYear(an.get());
        }
        else{
            return absenceService.getAbsenceByYear(null);
        }
    }

    @GetMapping("/absences/stats")
    public AbsencesStatsDTO getAbsencesStats(){
        return absenceService.getStatsAbsence();
    }

}
